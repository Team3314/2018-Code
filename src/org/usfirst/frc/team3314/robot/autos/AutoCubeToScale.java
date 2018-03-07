package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToScale extends Autonomous {

	enum State {
		START,
		RAISE_ARM,
		DRIVE,
		RELEASE_CUBE,
		DONE
	}
	
	private State currentState = State.START;
	
	private Path selectedPath = null;
	
	@Override
	public void reset() {
		resetTimer();
		currentState = State.START;
	}

	@Override
	public void update() {
		switch (currentState) {
		case START:
			selectedPath = getPath(getStart() + getScale());
			loadPath(selectedPath);
			startPathFollower();
			startTimer();
			currentState = State.RAISE_ARM;
			break;
		case RAISE_ARM:
			if(getTime() > 1) {
				armToScaleLow();
				resetTimer();
				currentState = State.DRIVE;
			}
			break;
		case DRIVE:
			if (isPathDone() && armStopped()) {
				currentState = State.RELEASE_CUBE;
				startTimer();
				releaseCubeFast();
			}
			break;
		case RELEASE_CUBE:
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
