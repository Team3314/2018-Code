
package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCrossBaseline extends Autonomous {
	
	enum State {
		START,
		DRIVE,
		STOP,
		DONE
	}
	
	private Drive drive = Drive.getInstance();
	private State currentState;
	
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
			currentState = State.DRIVE;
			startTimer();
			break;
		case DRIVE:
			drive.setDesiredSpeed(0.25);
			if (getTime() >= 10) { //placeholder
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
