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
		RELEASE_CUBE2,
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
			currentState = State.DRIVE_TO_SCALE;
			break;
		case DRIVE_TO_SCALE:
			if(isPathDone()) {
				currentState = State.RELEASE_CUBE;
			}
			break;
		case RELEASE_CUBE:
			outtakeCube();
			if(!hasCube()) {
				currentState = State.DRIVE_TO_SWITCH;
				loadPath(secondPath);
				startPathFollower();
			}
			break;
		case DRIVE_TO_SWITCH:
			if(isPathDone()) {
				currentState = State.PICKUP_CUBE;
			}
			break;
		case PICKUP_CUBE:
			intakeCube();
			if(hasCube()) {
				currentState = State.RELEASE_CUBE2;
			}
			break;
		case RELEASE_CUBE2:
			//TODO add move arm command
			outtakeCube();
			if(!hasCube()) {
				currentState = State.DONE;
			}
		case DONE:
			break;
		}
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
