package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoScaleThenSwitch extends Autonomous {

	enum State {
		START,
		RAISE_ARM,
		DRIVE_TO_SCALE,
		RELEASE_CUBE_SCALE,
		PICKUP_CUBE,
		BACKUP,
		RAISE_ARM_TO_SWITCH,
		DRIVE_TO_SWITCH,
		RELEASE_CUBE_SWITCH,
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
		switch (currentState) {
		case START:
			String path = getStart() + getScale() + "2";
			firstPath = getPath(path);
			startPathFollower(firstPath);
			startTimer();
			currentState = State.RAISE_ARM;
			break;
		case RAISE_ARM:
			if(getTime() > 1) {
				armToScaleMid();
				resetTimer();
				currentState = State.DRIVE_TO_SCALE;
			}
			break;
		case DRIVE_TO_SCALE:
			if(isPathDone()) {
				currentState = State.RELEASE_CUBE_SCALE;
				startTimer();
				releaseCubeSlow();
			}
			break;
		case RELEASE_CUBE_SCALE:
			armToPickUp();
			if(getTime() >= .5) {
				resetSensors();
				stopIntake();
				resetTimer();
				currentState = State.PICKUP_CUBE;
				startTracking();
			}
			break;
		case PICKUP_CUBE:
			if(hasCube()) {
				endTracking();
				currentState = State.BACKUP;
				drivePower(-.25);
				startTimer();
			}
			break;
		case BACKUP:
			if(getTime() >= .5) {
				currentState = State.RAISE_ARM_TO_SWITCH;
				armToSwitch();
			}
			break;
		case RAISE_ARM_TO_SWITCH:
			if(armStopped()) {
				currentState  = State.DRIVE_TO_SWITCH;
				startTimer();
			}
			break;
		case DRIVE_TO_SWITCH:
			drivePower(.5);
			if(getTime() > 1) {
				resetTimer();
				drivePower(0);
				currentState = State.RELEASE_CUBE_SWITCH;
				startTimer();
				releaseCubeSlow();
			}
			break;
		case RELEASE_CUBE_SWITCH:
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
