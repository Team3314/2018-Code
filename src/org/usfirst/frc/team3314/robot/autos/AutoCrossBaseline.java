
package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCrossBaseline implements Autonomous {
	
	enum State {
		START,
		DRIVE,
		STOP,
		DONE
	}
	
	private Drive drive = Drive.getInstance();
	private State currentState;
	private double desiredDistance;
	
	public AutoCrossBaseline() {
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
			drive.resetSensors();
			currentState = State.DRIVE;
			break;
		case DRIVE:
			drive.setDesiredAngle(0);
			drive.setDriveMode(driveMode.GYROLOCK);
			drive.setDesiredSpeed(0.25);
			if (drive.getAveragePosition() > 100) { //placeholder
				currentState = State.STOP;
			}
			break;
		case STOP:
			drive.setDesiredSpeed(0);
			currentState = State.DONE;
			break;
		case DONE:
			break;
		}
		SmartDashboard.putString("Auto state", currentState.toString());
	}

}
