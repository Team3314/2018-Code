package org.usfirst.frc.team3314.robot.autos;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

enum autoNothingStates {
	START,
	DONE
}

public class AutoNothing implements Autonomous {
	autoNothingStates currentState;
	
	public AutoNothing() {
		currentState = autoNothingStates.START;
	}
	
	public void reset() {
		currentState = autoNothingStates.START;
	}
	
	public void update() {
		switch (currentState) {
		case START:
			currentState = autoNothingStates.DONE;
			break;
		case DONE:
			break;
		}

		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
