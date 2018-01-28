package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitchThenScale extends Autonomous {

	enum State {
		START,
		DRIVE_TO_SWITCH,
		RELEASE_CUBE,
		PICKUP_CUBE,
		DRIVE_TO_SCALE,
		RELEASE_CUBE2,
		DONE
	}
	
	private State currentState = State.START;
	private double time;	 
	
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
			firstPath = getPath(getStart() + getSwitch());
			secondPath = getPath(getSwitch() + getScale());
			loadPath(firstPath);
			startPathFollower();
			break;
		case DRIVE_TO_SWITCH:
			if(isPathDone()) {
				currentState = State.RELEASE_CUBE;
				releaseCube();
				time = 25;
			}
			break;
		case RELEASE_CUBE:
			time--;
			if(time == 0) {
				currentState = State.PICKUP_CUBE;
			}
			break;
		case PICKUP_CUBE:
			intakeCube();
			if(hasCube()) {
				currentState = State.DRIVE_TO_SCALE;
				loadPath(secondPath);
				startPathFollower();
			}
			break;
		case DRIVE_TO_SCALE:
			if(isPathDone()) {
				currentState = State.RELEASE_CUBE2;
				releaseCube();
				time = 25;
			}
			break;
		case RELEASE_CUBE2:
			time--;
			if(time == 0) {
				currentState = State.DONE;
				stopIntake();
			}
		case DONE:
			break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
