package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.autos.AutoScaleThenSwitch.State;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoTwoCubeScale extends Autonomous {
	
	private enum State {
		START,
		RAISE_ARM,
		DRIVE_TO_SCALE,
		RELEASE_CUBE,
		DRIVE_TO_SWITCH,
		PICKUP_CUBE,
		TURN_TO_SCALE,
		DRIVE_TO_SCALE2,
		RELEASE_CUBE2,
		DONE
	}

	private State currentState = State.START;
	
	private Path firstPath = null;
	private Path secondPath = null;
	private Path thirdPath = null;
	
	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch(currentState) {
			case START:
				firstPath = getPath(getStart() + getScale());
				loadPath(firstPath);
				startPathFollower();
				armToScaleLow();
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
					currentState = State.RELEASE_CUBE;
					startTimer();
					releaseCubeSlow();
				}
				break;
			case RELEASE_CUBE:
				if(getTime() >= .5) {
					stopIntake();
					resetTimer();
					resetSensors();
					setHighGear(false);
					currentState = State.DRIVE_TO_SWITCH;
					armToPickUp();
				}
				break;
			case DRIVE_TO_SWITCH:
				if(getScale() == "ScaleR")
					driveGyrolock(0, 45, driveMode.GYROLOCK_LEFT);
				else if (getScale() == "ScaleL")
					driveGyrolock(0, -45, driveMode.GYROLOCK_RIGHT);
				if(gyroTurnDone() && armStopped()) {
					startTimer();
					if(targetInView()) {
						startTracking();
						currentState = State.PICKUP_CUBE;
					} else if(getTime() > 3) {
						currentState = State.DONE;
					}
				}
				break;
			case PICKUP_CUBE:
				if(hasCube()) {
					endTracking();
					currentState = State.TURN_TO_SCALE;
					resetSensors();
					armToScaleLow();
				}
				break;
			case TURN_TO_SCALE:
				driveGyrolock(0, 0, driveMode.GYROLOCK);
				if(gyroTurnDone()) {
					currentState = State.DRIVE_TO_SCALE2;
					setHighGear(true);
					loadPath(thirdPath);
					startPathFollower();
				}
				break;
			case DRIVE_TO_SCALE2:
				if(isPathDone() && armStopped()) {
					currentState = State.RELEASE_CUBE2;
					startTimer();
					releaseCubeSlow();
				}
				break;
			case RELEASE_CUBE2:
				if(getTime() >= .5) {
					stopIntake();
					resetTimer();
					currentState = State.DONE;
				}
				break;
			case DONE:
				break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}

}
