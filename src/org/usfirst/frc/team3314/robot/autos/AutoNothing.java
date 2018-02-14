package org.usfirst.frc.team3314.robot.autos;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class AutoNothing extends Autonomous {

	enum State {
		START,
		DONE
	}
	
	State currentState = State.START;
	
	public void reset() {
		currentState = State.START;
	}
	
	public void update() {
		switch (currentState) {
		case START:
			System.out.println(getStart() + getSwitch() + getScale());
			currentState = State.DONE;
			break;
		case DONE:

			break;
		}

		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
