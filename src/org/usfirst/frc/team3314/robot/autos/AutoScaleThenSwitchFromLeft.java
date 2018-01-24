package org.usfirst.frc.team3314.robot.autos;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.ScaleLeftToSwitchLeft;
import org.usfirst.frc.team3314.robot.paths.ScaleLeftToSwitchRight;
import org.usfirst.frc.team3314.robot.paths.ScaleRightToSwitchLeft;
import org.usfirst.frc.team3314.robot.paths.ScaleRightToSwitchRight;
import org.usfirst.frc.team3314.robot.paths.StartLeftToScaleLeft;
import org.usfirst.frc.team3314.robot.paths.StartLeftToScaleRight;

public class AutoScaleThenSwitchFromLeft extends Autonomous {

	enum State {
		START,
		DRIVE_TO_SCALE,
		RELEASE_CUBE,
		DRIVE_TO_SWITCH,
		PICKUP_CUBE,
		RELEASE_CUBE2,
		DONE
	}
	
	private State currentState = State.START;

	//Paths
	private Path startToScaleLeft = new StartLeftToScaleLeft();
	private Path startToScaleRight = new StartLeftToScaleRight();
	private Path scaleRightToSwitchLeft = new ScaleRightToSwitchLeft();
	private Path scaleRightToSwitchRight = new ScaleRightToSwitchRight();
	private Path scaleLeftToSwitchLeft = new ScaleLeftToSwitchLeft();
	private Path scaleLeftToSwitchRight = new ScaleLeftToSwitchRight();
	
	private Path firstPath = null;
	private Path secondPath = null;
	
	@Override
	public void reset() {
		currentState = State.START;
	}

	@Override
	public void update() {
		switch (currentState) {
		case START:
			firstPath = selectPath(startToScaleLeft, startToScaleRight, "switch");
			secondPath = selectPath(scaleLeftToSwitchLeft, scaleLeftToSwitchRight, scaleRightToSwitchLeft, scaleRightToSwitchRight);
			loadPath(firstPath);
			startPathFollower();
			break;
		case DONE:
			break;
		}
	}
}
