package org.usfirst.frc.team3314.robot;

import org.usfirst.frc.team3314.robot.subsystems.*;
import org.usfirst.frc.team3314.robot.subsystems.Drive.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Tracking {

	enum TrackingState {
		START,
		//SEEK,
		TRACK,
		//STOP,
		DRIVE,
		DONE
	}
	
	private static Tracking mInstance = new Tracking();
	
	public static Tracking getInstance() {
		return mInstance;
	}
	
	private Drive drive = Drive.getInstance();
	private Intake intake = Intake.getInstance();
	private Camera camera = Camera.getInstance();
	
	TrackingState currentState;
	
	public Tracking() {
		currentState = TrackingState.START;
	}
	
	public void reset() {
		// TODO Auto-generated method stub
		currentState = TrackingState.START;
	}
	
	public void update() {
		// TODO Auto-generated method stub
		if (camera.getTrackingRequest() == false) {
			currentState = TrackingState.DONE;
		}
		
		switch (currentState) {
		case START:
			if (camera.getTrackingRequest() == true) {
				//drive.setDriveMode(driveMode.VISION_CONTROL);
				currentState = /*State.SEEK;*/ TrackingState.TRACK;
			}
			break;
		/*case SEEK:
			camera.setSteeringAdjust(0.3);
			if (camera.isTargetInView()) {
				currentState = State.TRACK;
			}
			break;*/
		case TRACK:
			drive.setDriveMode(driveMode.VISION_CONTROL);
			camera.setSteeringAdjust(Constants.kGyroLock_kP*camera.getError());
			if (Math.abs(camera.getError()) < 0.1) {
				currentState = /*State.STOP;*/ TrackingState.DRIVE;
			}
			break;
		/*case STOP:
			drive.setDriveMode(driveMode.GYROLOCK);
			drive.setDesiredSpeed(0);
			currentState = State.DRIVE;
			break;*/
		case DRIVE:
			drive.setDriveMode(driveMode.GYROLOCK);
			drive.setDesiredAngle(drive.getAngle());
			drive.setDesiredSpeed(0.25);
			intake.setDesiredSpeed(1);
			
			if (camera.getError() > 1) {
				currentState = TrackingState.TRACK;
			} else if (!camera.isTargetInView()) {
				currentState = TrackingState.DONE;
			}
			break;
		case DONE:
			drive.setDesiredSpeed(0);
			intake.setDesiredSpeed(0);
			drive.setDriveMode(driveMode.OPEN_LOOP);
			camera.setTrackingRequest(false);
			currentState = TrackingState.START;
			break;
		}
		
		SmartDashboard.putString("Tracking state", currentState.toString());
		SmartDashboard.putNumber("Steering adjust", camera.getSteeringAdjust());
		SmartDashboard.putBoolean("Tracking request", camera.getTrackingRequest());
	}
}
