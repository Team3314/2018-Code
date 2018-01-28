package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoTwoCubeScale extends Autonomous {
	
	private enum State {
		START,
		DONE
	}

	private State currentState = State.START;
	
	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch(currentState) {
			case START:
				break;
			//TODO finish state machine
			case DONE:
				break;
		}
	}

}
