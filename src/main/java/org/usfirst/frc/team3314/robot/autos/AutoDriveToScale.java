package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.autos.AutoCubeToSwitch.State;
import org.usfirst.frc.team3314.robot.paths.Path;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveToScale extends Autonomous {

	enum State {
		START,
		DRIVE,
		DONE
	}
	
	private State currentState = State.START;
	
	private Path selectedPath = null;
	
	@Override
	public void reset() {
		
	}

	@Override
	public void update() {
		switch (currentState) {
		case START:
			selectedPath = getPath(getStart() + getScale() + "D");
			Constants.kMotionProfileHeading_kP = .025;
			Constants.kMotionProfileHeading_kF = .01;
			Constants.kMotionProfileHeading_kA = 0;
			Constants.kMotionProfileHeading_kD = .002;
			startPathFollower(selectedPath);
			armToScaleLow();
			currentState = State.DRIVE;
			break;
		case DRIVE:
			if(isPathDone()) {
				currentState = State.DONE;
			}
			break;
		case DONE:
			break;
	}
	
	SmartDashboard.putString("Auto state", currentState.toString());
	}

}
