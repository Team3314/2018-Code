package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
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
				resetSensors();
				selectedPath = getPath(getStart() + getSwitch());
				loadPath(selectedPath);
				startPathFollower();
				currentState = State.DRIVE;
				break;
			case DRIVE:
				if(isPathDone()) {
					currentState = State.RELEASE_CUBE;
				}
				break;
			case RELEASE_CUBE:
				outtakeCube();
				if(!hasCube()) {
					currentState = State.DONE;
				}
				break;
			case DONE:
				break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}

}
