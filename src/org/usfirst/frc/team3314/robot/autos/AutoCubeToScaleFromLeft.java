package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToScaleFromLeft implements Autonomous {

	enum State {
		START,
		DRIVE1,
		STOP1,
		TURN1, 
		STOP2,
		DRIVE2,		//
		STOP3,		//
		TURN2,		//
		STOP4,		// unnecessary if platform == 'L'
		DRIVE3,		//
		STOP5,		//
		TURN3,		//
		STOP6,		//
		RELEASE_CUBE,
		DONE
	}
	
	private Drive drive = Drive.getInstance();
	State currentState;
	String gameData = DriverStation.getInstance().getGameSpecificMessage();
	double desiredDistance, time = 0;
	
	public AutoCubeToScaleFromLeft() {
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
		case DRIVE1:
			break;
		case STOP1:
			break;
		case TURN1:
			break;
		case STOP2:
			break;
		case DRIVE2:
			break;
		case STOP3:
			break;
		case TURN2:
			break;
		case STOP4:
			break;
		case DRIVE3:
			break;
		case STOP5:
			break;
		case TURN3:
			break;
		case STOP6:
			break;
		case RELEASE_CUBE:
			 break;
		case DONE:
			break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}

}
