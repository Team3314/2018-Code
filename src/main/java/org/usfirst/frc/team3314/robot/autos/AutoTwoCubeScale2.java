package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoTwoCubeScale2 extends Autonomous {
	
	private enum State {
		START,
		RAISE_ARM,
		DRIVE_GYROLOCK1,
		STOP1,
		TURN1,
		DRIVE_GYROLOCK2,
		STOP2,
		TURN2,
		DRIVE_GYROLOCK3,
		STOP3,
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
					driveGyrolock(-.5, 0, driveMode.GYROLOCK);
					currentState = State.DRIVE_GYROLOCK1;
				}
				else {
					Constants.kMotionProfileHeading_kP = .025;
					Constants.kMotionProfileHeading_kF = 0;
					Constants.kMotionProfileHeading_kA = 0;
					startPathFollower(firstPath);
					startTimer();
					currentState = State.RAISE_ARM;
				}
				break;
			case RAISE_ARM:
				if(getTime() > 1) {
					armToScaleLow();
					resetTimer();
					currentState = State.DRIVE_TO_SCALE;
				}
				break;
			case DRIVE_GYROLOCK1:
				if(getDistance() >= 8) {
					currentState = State.TURN1;
					if(getStart().equals("StartL")) {
						driveGyrolock(-.5, 85, driveMode.GYROLOCK_RIGHT);
					}
					else if(getStart().equals("StartR")) {
						driveGyrolock(-.5, -85, driveMode.GYROLOCK_LEFT);
					}
				}
				break;
			case STOP1:
				if(getTime() >= .25) {
					if(getStart().equals("StartL")) {
						driveGyrolock(0, 90, driveMode.GYROLOCK_RIGHT);
					}
					else if(getStart().equals("StartR")) {
						driveGyrolock(0, -90, driveMode.GYROLOCK_LEFT);
					}
					resetTimer();
					currentState = State.TURN1;
				}
				break;
			case TURN1:
				if(isTurnDone()) {
					if(getStart().equals("StartL")) {
						driveGyrolock(-.5, 90, driveMode.GYROLOCK);
					}
					else if(getStart().equals("StartR")) {
						driveGyrolock(-.5, -90, driveMode.GYROLOCK);
					}
					resetDriveEncoders();
					currentState = State.DRIVE_GYROLOCK2;
				}
				break;
			case DRIVE_GYROLOCK2:
				if(getDistance() >= 10) {
					if(getStart().equals("StartL")) {
						driveGyrolock(-.5, 0, driveMode.GYROLOCK_LEFT);
					}
					else if(getStart().equals("StartR")) {
						driveGyrolock(-.5, 0, driveMode.GYROLOCK_RIGHT);
					}
					currentState = State.TURN2;
				}
				break;
			case STOP2:
				if(getTime() >= .25) {
					if(getStart().equals("StartL")) {
						driveGyrolock(0, 0, driveMode.GYROLOCK_LEFT);
					}
					else if(getStart().equals("StartR")) {
						driveGyrolock(0, 0, driveMode.GYROLOCK_RIGHT);
					}
					resetTimer();
					currentState = State.TURN2;
				}
				break;
			case TURN2:
				if(isTurnDone()) {
					driveGyrolock(-.25, 0, driveMode.GYROLOCK);
					currentState = State.DRIVE_GYROLOCK3;
					armToScaleMid();
					resetDriveEncoders();
				}
				break;
			case DRIVE_GYROLOCK3:
				if(getDistance() >= 4) {
					driveGyrolock(0,0,driveMode.GYROLOCK);
					if(armStopped()) {
						releaseCubeSlow();
						currentState = State.RELEASE_CUBE;
						startTimer();
					}
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