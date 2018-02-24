package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.autos.AutoCubeToScale.State;
import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToSwitch extends Autonomous {
	
	enum State {
		START,
		DRIVE,
		RELEASE_CUBE,
		DONE
	}
	
	private State currentState = State.START;
	private double time = 0;
	
	private Path selectedPath = null;

	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch (currentState) {
			case START:
				selectedPath = getPath(getStart() + getSwitch());
				loadPath(selectedPath);
				startPathFollower();
				armToScaleLow();
				currentState = State.DRIVE;
				break;
			case DRIVE:
				if(isPathDone() && armStopped()) {
					currentState = State.RELEASE_CUBE;
					startTimer();
					releaseCube();
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
