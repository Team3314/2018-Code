package org.usfirst.frc.team3314.robot;

import org.usfirst.frc.team3314.robot.subsystems.*;
import org.usfirst.frc.team3314.robot.subsystems.Drive.*;
import org.usfirst.frc.team3314.robot.subsystems.Intake.IntakeState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Tracking {

	public enum TrackingState {
		START,
		SEEK,
		TRACK,
		SETUP,
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
	private HumanInput hi = HumanInput.getInstance();
	
	private String seek;
	TrackingState currentState;
	
	public Tracking() {
		currentState = TrackingState.START;
	}
	
	public void reset() {
		currentState = TrackingState.START;
	}
	
	public void update() {
		if (camera.getTrackingRequest() == false) {
			currentState = TrackingState.DONE;
		}
		
		switch (currentState) {
		case START:
			if (camera.getTrackingRequest() == true && camera.isTargetInView() == true &&
					!intake.senseCube()) {
				drive.setDriveMode(driveMode.VISION_CONTROL);
				currentState = TrackingState.TRACK;
			}
			break;
		/*case SEEK:
			if (getSeek() == "ScaleR") {
				camera.setSteeringAdjust(0.25);//scale r = r scale l = l
			} else if (getSeek() == "ScaleL") {
				camera.setSteeringAdjust(0.25);
			}
				
			if (!hi.getVisionCtrl() || camera.isTargetInView()) {
				currentState = TrackingState.TRACK;
			}
			break;*/
		case TRACK:
			/**this is a basic implementation of turning using just the minimum speed the motor can spin at
			 *and a proportional constant. the degree deadband is a quarter degree on each side of the x axis
			 *
			 *the plan is to replace this with a more accurate calculation that incorporates the robots center
			 *of rotation to find the arc length of travel needed and puts that into a position closed loop
			 */			
			
			/*
			//hard implement
			camera.setSteeringAdjust(camera.getError());
			*/
			
			//basic implement
			if (camera.getError() > 0) {
				camera.setSteeringAdjust(camera.getError() * Constants.kVisionCtrl_kP + 
						Constants.kMinMotorCmd);
			} else if (camera.getError() < 0) {
				camera.setSteeringAdjust(camera.getError() * Constants.kVisionCtrl_kP -
						Constants.kMinMotorCmd);
			}
			
			if (Math.abs(camera.getError()) <= 0.75) {
				camera.setSteeringAdjust(0);
				currentState = TrackingState.SETUP;
			}
			break;
		case SETUP:
			if (camera.getTrackingRequest() == true && !hi.getVisionCtrl()) {
				drive.setDriveMode(driveMode.GYROLOCK);
				drive.setDesiredAngle(drive.getAngle());
				drive.setDesiredSpeed(0.5);
				intake.setDesiredState(IntakeState.INTAKING);
				currentState = TrackingState.DRIVE;
			} else {
				drive.setDriveMode(driveMode.OPEN_LOOP);
				camera.setTrackingRequest(false);
				currentState = TrackingState.DONE;
			}
			break;
		case DRIVE:
			if (intake.getState() == IntakeState.UNJAMMING || intake.senseCube()) {
				drive.setDesiredSpeed(0);
				camera.setTrackingRequest(false);
				currentState = TrackingState.DONE;
			}
			break;
		case DONE:
			currentState = TrackingState.START;
			break;
		}
		
		SmartDashboard.putString("Tracking state", currentState.toString());
	}
	
	public void setSeek(String scaleData) {
		seek = scaleData;
	}
	
	public TrackingState getState() {
		return currentState;
	}
	
	public String getSeek() {
		return seek;
	}
}