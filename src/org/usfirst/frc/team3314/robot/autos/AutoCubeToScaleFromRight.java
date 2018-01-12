package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoCubeToScaleFromRight implements Autonomous {

	enum State {
		START,
		DRIVE1,
		STOP1,
		TURN1, 
		STOP2,
		DRIVE2,		//
		STOP3,		//
		TURN2,		//
		STOP4,		// unnecessary if platform == 'R'
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
	double desiredDistance;
	
	public AutoCubeToScaleFromRight() {
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
	}

}
