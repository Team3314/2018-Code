package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.autos.AutoCubeToScale.State;
import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoScaleThenSwitch extends Autonomous {

	enum State {
		START,
		RAISE_ARM,
		DRIVE_TO_SCALE,
		RELEASE_CUBE_SCALE,
		DRIVE_TO_PICKUP,
		PICKUP_CUBE,
		RAISE_ARM_TO_SWITCH,
		DRIVE_TO_SWITCH,
		RELEASE_CUBE_SWITCH,
		DONE
	}
	
	private State currentState = State.START;
	
	private Path firstPath = null;
	private Path secondPath = null;
	
	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch (currentState) {
		case START:
			firstPath = getPath(getStart() + getScale());
			secondPath = getPath(getScale() + getSwitch());
			loadPath(firstPath);
			startPathFollower();
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
				currentState = State.RELEASE_CUBE_SCALE;
				startTimer();
				releaseCubeSlow();
			}
			break;
		case RELEASE_CUBE_SCALE:
			if(getTime() >= .5) {
				resetSensors();
				stopIntake();
				resetTimer();
				currentState = State.DRIVE_TO_PICKUP;
				loadPath(secondPath);
				startPathFollower();
				armToPickUp();
				startTimer();
			}
			break;
		case DRIVE_TO_PICKUP:
			if(isPathDone() && getTime() >= 1) {
				resetTimer();
				currentState = State.PICKUP_CUBE;
				setHighGear(false);
			}
			break;
		case PICKUP_CUBE:
			startTracking();
			if(hasCube()) {
				endTracking();
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
			drivePower(.2);
			if(getTime() > .5) {
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
