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
		INTAKE,
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
	private double minMotorCmd = 0.095;
	
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
			if (camera.getTrackingRequest() == true) {
				camera.setCamMode(Constants.kVisionProcessorMode);
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
			
			if (Math.abs(camera.getError()) < 0.25) {
				currentState = TrackingState.DRIVE;
			}
			
			//hard implement
			drive.setDriveMode(driveMode.VISION_CONTROL);
			camera.setSteeringAdjust(camera.getArcLength()/Constants.kRevToInConvFactor*
					Constants.kDriveEncoderCodesPerRev);
			
			//basic implement
			if (camera.getError() > 0.25) {
				camera.setSteeringAdjust((camera.getError()*Constants.kGyroLock_kP)+minMotorCmd);
			} else {
				camera.setSteeringAdjust((camera.getError()*Constants.kGyroLock_kP)-minMotorCmd);
			}
			break;
		case DRIVE:
			drive.setDriveMode(driveMode.GYROLOCK);
			drive.setDesiredAngle(drive.getAngle());
			drive.setDesiredSpeed(0.25);
			
			if (camera.getError() > 1) {
				currentState = TrackingState.TRACK;
			} else if (camera.getDistance() > 22 && camera.getDistance() < 24) {
				drive.setDesiredSpeed(0);
				currentState = TrackingState.INTAKE;
			}
			break;
		case INTAKE:
			intake.setDesiredState(IntakeState.INTAKING);
			if (intake.senseCube()) {
				intake.setDesiredState(IntakeState.HOLDING);
				currentState = TrackingState.DONE;
			}
		case DONE:
			camera.setTrackingRequest(false);
			camera.setCamMode(Constants.kDriverCameraMode);
			currentState = TrackingState.START;
			break;
		}
		
		SmartDashboard.putString("Tracking state", currentState.toString());
		SmartDashboard.putNumber("Steering adjust", camera.getSteeringAdjust());
		SmartDashboard.putBoolean("Tracking request", camera.getTrackingRequest());
	}
}
