package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.motion.CSVParser;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.StartCenterToSwitchLeft;
import org.usfirst.frc.team3314.robot.paths.StartCenterToSwitchRight;
import org.usfirst.frc.team3314.robot.paths.StartRightToSwitchLeft;
import org.usfirst.frc.team3314.robot.paths.StartRightToSwitchRight;
import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToSwitchFromRight extends Autonomous {

	enum State {
		START,
		DRIVE,
		RELEASE_CUBE,
		DONE
	}
	
	//Switch and Scale sides
	private char switchSide = ' ';
	private char scaleSide = ' ';

	
	//TODO replace paths wiht correct ones
	private State currentState;
	private double time = 0; //place holder
	private Path startToSwitchLeft = new StartRightToSwitchLeft();
	private Path startToSwitchRight = new StartRightToSwitchRight();
	
	private Path selectedPath = null;
	
	public AutoCubeToSwitchFromRight() {
		currentState = State.START;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		currentState = State.START;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
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
				currentState = State.DONE;
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
