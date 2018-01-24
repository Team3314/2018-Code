package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.StartLeftToSwitchLeft;
import org.usfirst.frc.team3314.robot.paths.StartLeftToSwitchRight;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToSwitchFromLeft extends Autonomous {
	
	enum State {
		START,
		DRIVE,
		RELEASE_CUBE,
		DONE
	}
	
	//Switch and Scale sides
	private char switchSide = ' ';
	private char scaleSide = ' ';
	
	State currentState;
	double time = 0;
	//TODO replace paths wiht correct ones
	private Path startToSwitchLeft = new StartLeftToSwitchLeft();
	private Path startToSwitchRight = new StartLeftToSwitchRight();
	
	private Path selectedPath = null;
	
	public AutoCubeToSwitchFromLeft() {
		currentState = State.START;
	}
	
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
				if(isPathDone()) {
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
	
	public void setGameData(String data) {
		switchSide = data.charAt(0);
		scaleSide = data.charAt(1);
	}

}
