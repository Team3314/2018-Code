package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.StartLeftToSwitchLeft;
import org.usfirst.frc.team3314.robot.paths.StartLeftToSwitchRight;
import org.usfirst.frc.team3314.robot.paths.SwitchLeftToScaleLeft;
import org.usfirst.frc.team3314.robot.paths.SwitchLeftToScaleRight;
import org.usfirst.frc.team3314.robot.paths.SwitchRightToScaleLeft;
import org.usfirst.frc.team3314.robot.paths.SwitchRightToScaleRight;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitchThenScaleFromLeft extends Autonomous {

	enum State {
		START,
		DONE
	}
	
	private State currentState = State.START;
	private double time;
	private Path startToSwitchLeft = new StartLeftToSwitchLeft();
	private Path startToSwitchRight = new StartLeftToSwitchRight();
	private Path switchRightToScaleLeft = new SwitchRightToScaleLeft();
	private Path switchRightToScaleRight = new SwitchRightToScaleRight();
	private Path switchLeftToScaleLeft = new SwitchLeftToScaleLeft();
	private Path switchLeftToScaleRight = new SwitchLeftToScaleRight();
	
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
			firstPath = selectPath(startToSwitchLeft, startToSwitchRight, "switch");
			//secondPath = selectPath(scaleLeftToSwitchLeft, scaleLeftToSwitchRight, scaleRightToSwitchLeft, scaleRightToSwitchRight); TODO : add right paths
			loadPath(firstPath);
			startPathFollower();
			break;
		case DONE:
			break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}
