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
	private HumanInput hi = HumanInput.getInstance();
	
	TrackingState currentState;
	private double minMotorCmd = 0.095;
	private double turn = camera.getError() * Constants.kVisionCtrl_kP;
	
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
			
			/*//hard implement
			drive.setDriveMode(driveMode.VISION_CONTROL);
			camera.setSteeringAdjust(camera.getArcLength() / Constants.kRevToInConvFactor *
					Constants.kDriveEncoderCodesPerRev);*/
			
			//basic implement
			if (camera.getError() > 0.25) {
				camera.setSteeringAdjust(turn + minMotorCmd);
			} else {
				camera.setSteeringAdjust(turn - minMotorCmd);
			}
			break;
		case DRIVE:
			if (camera.getTrackingRequest() == true && !hi.getVisionCtrl()) {
				drive.setDriveMode(driveMode.GYROLOCK);
				drive.setDesiredAngle(drive.getAngle());
				drive.setDesiredSpeed(0.5);
				
				if (Math.abs(camera.getError()) > 1) {
					currentState = TrackingState.TRACK;
				} else if (camera.getDistance() > 23.5 && camera.getDistance() < 24.5) {
					drive.setDesiredSpeed(0);
					currentState = TrackingState.INTAKE;
				}
			} else {
				drive.setDriveMode(driveMode.OPEN_LOOP);
				currentState = TrackingState.DONE;
			}
			break;
		case INTAKE:
			intake.setDesiredState(IntakeState.INTAKING);
			if (intake.senseCube()) {
				intake.setDesiredState(IntakeState.HOLDING);
				currentState = TrackingState.DONE;
			}
			break;
		case DONE:
			camera.setTrackingRequest(false);
			camera.setCamMode(Constants.kVisionProcessorMode);
			currentState = TrackingState.START;
			break;
		}
		
		SmartDashboard.putString("Tracking state", currentState.toString());
	}
}