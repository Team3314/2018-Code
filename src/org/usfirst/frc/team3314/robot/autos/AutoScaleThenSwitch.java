package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoScaleThenSwitch extends Autonomous {

	enum State {
		START,
		DRIVE_TO_SCALE,
		RELEASE_CUBE,
		DRIVE_TO_SWITCH,
		PICKUP_CUBE,
		RAISE_ARM_TO_SWITCH,
		RELEASE_CUBE2,
		DONE
	}
	
	private State currentState = State.START;
	
	private int time = 0;
	
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
			armToScale();
			currentState = State.DRIVE_TO_SCALE;
			break;
		case DRIVE_TO_SCALE:
			if(isPathDone()) {
				currentState = State.RELEASE_CUBE;
				startTimer();
				releaseCube();
			}
			break;
		case RELEASE_CUBE:
			if(getTime() >= .5) {
				stopIntake();
				resetTimer();
				currentState = State.DRIVE_TO_SWITCH;
				loadPath(secondPath);
				startPathFollower();
				armToPickUp();
			}
			break;
		case DRIVE_TO_SWITCH:
			if(isPathDone()) {
				currentState = State.PICKUP_CUBE;
			}
			break;
		case PICKUP_CUBE:
			//TODO Add cube tracking
			intakeCube();
			if(hasCube()) {
				currentState = State.RAISE_ARM_TO_SWITCH;
				armToSwitch();
			}
			break;
		case RAISE_ARM_TO_SWITCH:
			if(armStopped()) {
				currentState  = State.RELEASE_CUBE2;
				releaseCube();
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
