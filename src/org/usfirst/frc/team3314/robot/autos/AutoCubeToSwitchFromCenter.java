package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.StartCenterToSwitchLeft;
import org.usfirst.frc.team3314.robot.paths.StartCenterToSwitchRight;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToSwitchFromCenter extends Autonomous {
	
	enum State {
		START,
		DRIVE,
		RELEASE_CUBE,
		DONE
	}
	
	private Path startToSwitchLeft = new StartCenterToSwitchLeft();
	private Path startToSwitchRight = new StartCenterToSwitchRight();
	
	private Path selectedPath = null;
	
	private State currentState = State.START;;
	
	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch (currentState) {
		case START:
			resetSensors();
			selectedPath = selectPath(startToSwitchLeft, startToSwitchRight, "switch");
			loadPath(selectedPath);
			startPathFollower();
			currentState = State.DRIVE;
			break;
		case DRIVE:
			if (isPathDone()) {
				currentState = State.RELEASE_CUBE;
			}	
			break;
		case RELEASE_CUBE:
			Intake.getInstance().setDesiredSpeed(-1);
			currentState = State.DONE;
			break;
		case DONE:
			break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
