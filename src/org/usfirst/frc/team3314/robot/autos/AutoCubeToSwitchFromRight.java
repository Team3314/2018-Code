package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCubeToSwitchFromRight implements Autonomous {

	enum State {
		START,
		DRIVE1,
		STOP1,
		TURN1,
		STOP2,
		DRIVE2,
		STOP3,
		TURN2,
		STOP4,
		DRIVE3,
		STOP5,
		RELEASE_CUBE,
		DONE
	}
	
	private Drive drive = Drive.getInstance();
	State currentState;
	String gameData = DriverStation.getInstance().getGameSpecificMessage();
	double desiredDistance = 100; //place holder
	
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
			drive.resetSensors();
			currentState = State.DRIVE1;
			break;
		case DRIVE1:
			drive.setDesiredAngle(0);
			drive.setDriveMode(driveMode.GYROLOCK);
			drive.setDesiredSpeed(0.25);
			if (drive.getAveragePosition() > desiredDistance) {
				currentState = State.STOP1;
			}
			break;
		case STOP1:
			drive.setDesiredSpeed(0);
			currentState = State.TURN1;
			break;
		case TURN1:
			if (gameData.charAt(0) == 'L') {
				drive.setDesiredAngle(-25); //place holder
			} else {
				drive.setDesiredAngle(0); //place holder
			}
			if (drive.checkTolerance()) {
				currentState = State.STOP2;
			}
			break;
		case STOP2:
			drive.setDesiredSpeed(0);
			drive.resetSensors();
			if (gameData.charAt(0) == 'L') {
				desiredDistance = 100; //place holder
			} else {
				desiredDistance = 80; //place holder
			}
			currentState = State.DRIVE2;
			break;
		case DRIVE2:
			drive.setDesiredAngle(0);
			drive.setDesiredSpeed(.25);
			if (drive.getAveragePosition() > desiredDistance) {
				currentState = State.STOP3;
			}
			break;
		case STOP3:
			drive.setDesiredSpeed(0);
			currentState = State.TURN2;
			break;
		case TURN2:
			if (gameData.charAt(0) == 'L') {
				drive.setDesiredAngle(25);
			} else {
				drive.setDesiredAngle(0);
			}
			
			if (drive.checkTolerance()) {
				currentState = State.STOP4;
			}
			break;
		case STOP4:
			drive.setDesiredSpeed(0);
			drive.resetSensors();
			desiredDistance = 100; //place holder
			currentState = State.DRIVE3;
			break;
		case DRIVE3:
			drive.setDesiredAngle(0);
			drive.setDesiredSpeed(.25);
			if (drive.getAveragePosition() > desiredDistance) {
				currentState = State.STOP5;
			}
			break;
		case STOP5:
			drive.setDesiredSpeed(0);
			currentState = State.RELEASE_CUBE;
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
