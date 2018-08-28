package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoVisionCubeFuckery extends Autonomous {
	
	enum State {
		START_TEST,
		PICKUP_CUBE,
		DONE
	}
	
	private State currentState;
	private Drive drive = Drive.getInstance();
	
	@Override
	public void reset() {
		currentState = State.START_TEST;
	}
	
	@Override
	public void update() {
		switch (currentState) {
		case START_TEST:
			drive.setDriveMode(driveMode.GYROLOCK);
			setHighGear(false);
			armToPickUp();
			currentState = State.PICKUP_CUBE;
			break;
		case PICKUP_CUBE:
			if (armStopped()) {
				startTracking();
			}
			
			if (hasCube()) {
				endTracking();
				setHighGear(true);
				armToSwitch();
				currentState = State.DONE;
			}
			break;
		case DONE:
			break;
		}
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
