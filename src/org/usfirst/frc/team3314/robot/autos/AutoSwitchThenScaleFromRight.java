package org.usfirst.frc.team3314.robot.autos;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.StartRightToSwitchLeft;
import org.usfirst.frc.team3314.robot.paths.StartRightToSwitchRight;
import org.usfirst.frc.team3314.robot.paths.SwitchLeftToScaleLeft;
import org.usfirst.frc.team3314.robot.paths.SwitchLeftToScaleRight;
import org.usfirst.frc.team3314.robot.paths.SwitchRightToScaleLeft;
import org.usfirst.frc.team3314.robot.paths.SwitchRightToScaleRight;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitchThenScaleFromRight extends Autonomous {

	enum State {
		START,             
		DONE				
	}
	

	private State currentState;
	private double time;
	private Path startToSwitchLeft = new StartRightToSwitchLeft();
	private Path startToSwitchRight = new StartRightToSwitchRight();
	private Path switchRightToScaleLeft = new SwitchRightToScaleLeft();
	private Path switchRightToScaleRight = new SwitchRightToScaleRight();
	private Path switchLeftToScaleLeft = new SwitchLeftToScaleLeft();
	private Path switchLeftToScaleRight = new SwitchLeftToScaleRight();
	
	private Path firstPath = null;
	private Path secondPath = null;
	
	public AutoSwitchThenScaleFromRight() {
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
			firstPath = selectPath(startToSwitchLeft, startToSwitchRight, "switch");
			secondPath = selectPath(switchLeftToScaleLeft, switchLeftToScaleRight, switchRightToScaleLeft, switchRightToScaleRight);
			loadPath(firstPath);
			startPathFollower();
			break;
			
		case DONE:
			break;
		}
		
		SmartDashboard.putString("Auto state", currentState.toString());
	}
}