package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoVisionCubeFuckery extends Autonomous {
	
	enum State {
		START,
		DRIVE,
		PICKUP_CUBE,
		DONE
	}
	
	private Drive drive = Drive.getInstance();
	private State currentState;
	
	public AutoVisionCubeFuckery() {
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
			armToPickUp();
			currentState = State.DRIVE;
			startTimer();
			break;
		case DRIVE:
			drive.setDesiredSpeed(0.25);
			if (getTime() >= 2.5) {
				drive.setDesiredSpeed(0);
				resetTimer();
				currentState = State.PICKUP_CUBE;
			}
			break;
		case PICKUP_CUBE:
			startTracking();
			if (hasCube()) {
				endTracking();
				armToScaleHigh();
				currentState = State.DONE;
			}
			break;
		case DONE:
			break;
		}
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
