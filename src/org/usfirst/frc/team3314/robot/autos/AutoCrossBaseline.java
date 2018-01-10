
package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

enum autoCrossBaselineStates {
	START,
	DRIVE,
	STOP,
	DONE
}

public class AutoCrossBaseline implements Autonomous {
	private Drive drive = Drive.getInstance();
	autoCrossBaselineStates currentState;
	double desiredDistance;
	
	public AutoCrossBaseline() {
		currentState = autoCrossBaselineStates.START;
	}
	
	@Override
	public void reset() {
		currentState = autoCrossBaselineStates.START;
	}
	
	@Override
	public void update() {
		switch (currentState) {
		case START:
			drive.resetSensors();
			currentState = autoCrossBaselineStates.DRIVE;
			break;
		case DRIVE:
			drive.setDesiredAngle(0);
			drive.setDriveMode(driveMode.GYROLOCK);
			drive.setDesiredSpeed(0.25);
			if (drive.getAveragePosition() > 100) { //placeholder
				currentState = autoCrossBaselineStates.STOP;
			}
			break;
		case STOP:
			drive.setDesiredSpeed(0);
			currentState = autoCrossBaselineStates.DONE;
			break;
		case DONE:
			break;
		}
		SmartDashboard.putString("Auto state", currentState.toString());
	}

}
