package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToScale extends Autonomous {

	enum State {
		START,
		DRIVE,
		RELEASE_CUBE,
		DONE
	}
	
	private State currentState = State.START;
	private int time;
	
	private Path selectedPath = null;
	
	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch (currentState) {
		case START:
			resetSensors();
			selectedPath = getPath(getStart() + getScale());
			loadPath(selectedPath);
			startPathFollower();
			currentState = State.DRIVE;
			break;
		case DRIVE:
			if (isPathDone()) {
				currentState = State.RELEASE_CUBE;
				startTimer();
				releaseCube();
			}
			break;
		case RELEASE_CUBE:
			if(getTime() >= .5) {
				stopIntake();
				currentState = State.DONE;
				resetTimer();
			}
			break;
		case DONE:
			break;
		}
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
