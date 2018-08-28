package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToScale extends Autonomous {

	enum State {
		START,
		RAISE_ARM,
		DRIVE,
		RELEASE_CUBE,
		DRIVE2,
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
			startPathFollower(selectedPath);
			startTimer();
			currentState = State.RAISE_ARM;
			break;
		case RAISE_ARM:
			if(getTime() > 1) {
				armToScaleMid();
				resetTimer();
				currentState = State.DRIVE;
			}
			break;
		case DRIVE:
			if (isPathDone() && armStopped()) {
				currentState = State.RELEASE_CUBE;
				startTimer();
				releaseCubeSlow();
			}
			break;
		case RELEASE_CUBE:
			if(getTime() >= .5) {
				stopIntake();
				resetTimer();
				startTimer();
				armToHolding();
				//drivePower(.5);
				currentState = State.DRIVE2;
			}
			break;
		case DRIVE2:
			if(getTime() >= 1) {
				drivePower(0);
				currentState = State.DONE;
			}
			break;
		case DONE:
			break;
		}
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}