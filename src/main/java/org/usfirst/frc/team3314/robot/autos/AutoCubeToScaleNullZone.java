package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToScaleNullZone extends Autonomous {
	enum State {
		START,
		DRIVE,
		RAISE_ARM,
		TURN,
		RELEASE_CUBE,
		DONE
	}
	
	private State currentState = State.START;
	
	private Path selectedPath = null;
	
	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch (currentState) {
		case START:
			selectedPath = getPath("ScaleNull");
			startPathFollower(selectedPath);
			currentState = State.DRIVE;
			startTimer();
			break;
		case DRIVE:
			if(getTime() >= 1) {
				armToScaleMid();
				resetTimer();
				currentState = State.RAISE_ARM;
			}
			break;
		case RAISE_ARM:
			if(isStopped()) {
				if(getScale().equals("ScaleR"))
					driveGyrolock(0,-90, driveMode.GYROLOCK);
				if(getScale().equals("ScaleL"))
					driveGyrolock(0, 90, driveMode.GYROLOCK);
				currentState = State.TURN;
			}
			break;
		case TURN:
			if(isTurnDone()) {
				startTimer();
				releaseCubeSlow();
				currentState = State.RELEASE_CUBE;
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
