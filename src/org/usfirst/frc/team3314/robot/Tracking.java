package org.usfirst.frc.team3314.robot;

import org.usfirst.frc.team3314.robot.subsystems.*;
import org.usfirst.frc.team3314.robot.subsystems.Drive.*;
import org.usfirst.frc.team3314.robot.subsystems.Intake.IntakeState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Tracking {

	public enum TrackingState {
		START,
		TRACK,
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
			if (camera.getTrackingRequest() == true && camera.isTargetInView() == true) {
				drive.setDriveMode(driveMode.VISION_CONTROL);
				currentState = TrackingState.TRACK;
			}
			break;
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
			if (camera.getAvgError() > 0) {
				camera.setSteeringAdjust(camera.getAvgError() * Constants.kVisionCtrl_kP + 
						Constants.kMinMotorCmd);
			} else if (camera.getAvgError() < 0) {
				camera.setSteeringAdjust(camera.getAvgError() * Constants.kVisionCtrl_kP -
						Constants.kMinMotorCmd);
			}
			
			if (Math.abs(camera.getAvgError()) <= 0.75) {
				camera.setSteeringAdjust(0);
				drive.setDriveMode(driveMode.GYROLOCK);
				drive.setDesiredAngle(drive.getAngle());
				drive.setDesiredSpeed(0.5);
				currentState = TrackingState.DRIVE;
			}
			break;
		case DRIVE:
			if (camera.getTrackingRequest() == true && hi.getVisionCtrl()) {
				intake.setDesiredState(IntakeState.INTAKING);
				
				//FIXME Get it to unjam in worst case scenario
				if (intake.motorsStalled()) {
					intake.setDesiredState(IntakeState.UNJAMMING);
				}
				
				if (intake.senseCube()) {
					//drive.setDriveMode(driveMode.MOTION_PROFILE);
					//drive.setDriveMode(driveMode.IDLE);
					//drive.setDriveMode(driveMode.OPEN_LOOP);
					drive.setDesiredSpeed(0);
					camera.setTrackingRequest(false);
					currentState = TrackingState.DONE;
				}
			} else {
				drive.setDriveMode(driveMode.OPEN_LOOP);
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
	
	public TrackingState getState() {
		return currentState;
	}
}