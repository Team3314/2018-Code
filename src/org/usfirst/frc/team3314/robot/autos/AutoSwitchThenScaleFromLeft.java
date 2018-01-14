package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitchThenScaleFromLeft implements Autonomous {

	enum State {
		START,
		DONE
	}
	
	private Drive drive = Drive.getInstance();
	String gameData = DriverStation.getInstance().getGameSpecificMessage();
	State currentState;
	double desiredDistance, time;
	
	public AutoSwitchThenScaleFromLeft() {
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
			break;
		case DONE:
			break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}

}
