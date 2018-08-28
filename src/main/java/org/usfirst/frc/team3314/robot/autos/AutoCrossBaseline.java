
package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;

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
	
	@Override
	public void reset() {
		currentState = State.START;
	}
	
	@Override
	public void update() {
		switch (currentState) {
		case START:
			currentState = State.DRIVE;
			setHighGear(false);
			startTimer();
			break;
		case DRIVE:
			drivePower(-0.4);
			if (getTime() >= 4) {
				currentState = State.STOP;
			}
			break;
		case STOP:
			drivePower(0);
			currentState = State.DONE;
			break;
		case DONE:
			break;
		}
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
