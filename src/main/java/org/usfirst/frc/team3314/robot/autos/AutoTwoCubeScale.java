package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoTwoCubeScale extends Autonomous {
	
	private enum State {
		START,
		RAISE_ARM,
		DRIVE_TO_SCALE,
		TURNBACK,
		RELEASE_CUBE,
		PICKUP_CUBE,
		DRIVEBACK,
		TURNBACK2,
		DRIVE_TO_SCALE2,
		RELEASE_CUBE2,
		DONE
	}

	private State currentState = State.START;
	
	private Path firstPath = null;
	
	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch(currentState) {
			case START:
				String path = getStart() + getScale() + "2";
				firstPath = getPath(path);
				if(getCrossScale()) {
					Constants.kMotionProfileHeading_kP = .025;
					Constants.kMotionProfileHeading_kF = .01;
					Constants.kMotionProfileHeading_kA = 10;
					Constants.kMotionProfileHeading_kD = .00;
				}
				else {
					Constants.kMotionProfileHeading_kP = .025;
					Constants.kMotionProfileHeading_kF = 0;
					Constants.kMotionProfileHeading_kA = 0;
				}
				startPathFollower(firstPath);
				startTimer();
				currentState = State.RAISE_ARM;
				break;
			case RAISE_ARM:
				if(getTime() > 1) {
					armToScaleLow();
					resetTimer();
					currentState = State.DRIVE_TO_SCALE;
				}
				break;
			case DRIVE_TO_SCALE:
				if(isPathDone()) {
					if(getStart().equals("StartL") && getScale().equals("ScaleR") || getStart().equals("StartR") && getScale().equals("ScaleL")) {
						currentState = State.TURNBACK;
						driveGyrolock(0, 0, driveMode.GYROLOCK);
					}
					else {
						startTimer();
						releaseCubeSlow();
						currentState = State.RELEASE_CUBE;
					}
				}
				break;
			case TURNBACK:
				if(isTurnDone()) {
					startTimer();
					releaseCubeSlow();
					currentState = State.RELEASE_CUBE;
				}
				break;
			case RELEASE_CUBE:
				armToPickUp();
				if(getTime() >= 2) {
					stopIntake();
					resetTimer();
					startTracking();
					currentState = State.PICKUP_CUBE;
				}
				break;
			case PICKUP_CUBE:
				if(hasCube()) {
					endTracking();
					currentState = State.DRIVEBACK;
					drivePower(-.25);
					startTimer();
				}
				break;
			case DRIVEBACK:
				if(getTime() >= .5) {
					currentState = State.TURNBACK2;
					if(getScale().equals("ScaleR"))
						driveGyrolock(0, -5, driveMode.GYROLOCK);
					else if(getScale().equals("ScaleL"))
						driveGyrolock(0, 5, driveMode.GYROLOCK);
					armToScaleLow();
				}
				break;
			case TURNBACK2:
				if(isTurnDone()) {
					resetSensors();
					driveMotionMagic(-3.5, 0, 5, false);
					currentState = State.DRIVE_TO_SCALE2;
					startTimer();
				}
				break;
			case DRIVE_TO_SCALE2:
				if(isStopped() && armStopped()) {
					currentState = State.RELEASE_CUBE2;
					resetTimer();
					startTimer();
					releaseCubeSlow();
				}
				break;
			case RELEASE_CUBE2:
				if(getTime() >= .5) {
					stopIntake();
					resetTimer();
					armToPickUp();
					currentState = State.DONE;
				}
				break;
			case DONE:
				break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}

}