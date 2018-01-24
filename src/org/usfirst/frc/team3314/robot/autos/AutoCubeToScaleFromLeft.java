package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.StartLeftToScaleLeft;
import org.usfirst.frc.team3314.robot.paths.StartLeftToScaleRight;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToScaleFromLeft extends Autonomous {

	enum State {
		START,
		DRIVE,
		RELEASE_CUBE,
		DONE
	}
	
	//Switch and Scale sides
	private char switchSide = ' ';
	private char scaleSide = ' ';
	
	private State currentState;
	
	private Path startToScaleRight = new StartLeftToScaleRight();
	private Path startToScaleLeft = new StartLeftToScaleLeft();
	
	private Path selectedPath = null;
	
	public AutoCubeToScaleFromLeft() {
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
			selectedPath = selectPath(startToScaleLeft, startToScaleRight, "switch");
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
	
	public void setGameData(String data) {
		// TODO Auto-generated method stub
		switchSide = data.charAt(0);
		scaleSide = data.charAt(1);
	}

}
