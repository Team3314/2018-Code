package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoTwoCubeScale extends Autonomous {
	
	private enum State {
		START,
		DRIVE_TO_SCALE,
		RELEASE_CUBE,
		//DRIVE_TO_SWITCH,
		PICKUP_CUBE,
		//DRIVE_TO_SCALE2,
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
				firstPath = getPath(getStart()+getScale());
				secondPath = getPath(getScale()+getSwitch());
				thirdPath = getPath(getSwitch()+getScale());
				loadPath(firstPath);
				startPathFollower();
				break;
			//TODO finish state machine
			case DONE:
				break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}

}
