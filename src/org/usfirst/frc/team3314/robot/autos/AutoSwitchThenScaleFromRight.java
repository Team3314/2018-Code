package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitchThenScaleFromRight implements Autonomous {

	enum State {
		START,             
		/*DRIVE1_L,			
		STOP1_L	,			
		TURN1_L,				
		STOP2_L,				
		RELEASE_CUBE1_L,		
		TURN2_L,				
		STOP3_L,				
		DRIVE2_L,				
		STOP4_L,				
		TURN3_L,				
		STOP5_L,				
		DRIVE3_L,				
		STOP6_L,				
		INTAKE_CUBE_L,		
		TURN4_L,				
		STOP7_L,				
		DRIVE4_L,				
		STOP8_L,
		TURN5_LR,
		STOP9_LR,
		DRIVE5_LR,
		STOP10_LR,
		TURN6_LR,
		DRIVE6_LR,
		STOP11_LR,		
		RELEASE_CUBE2,*/
		DONE				
	}
	
	private Drive drive = Drive.getInstance();
	String gameData = DriverStation.getInstance().getGameSpecificMessage();
	State currentState;
	double desiredDistance, time;
	
	public AutoSwitchThenScaleFromRight() {
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