package org.usfirst.frc.team3314.robot.autos;
import org.usfirst.frc.team3314.robot.motion.CSVParser;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.PathOne;
import org.usfirst.frc.team3314.robot.paths.PathTwo;
import org.usfirst.frc.team3314.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MotionProfile extends Autonomous {
	
	enum State {
		START,
		RUNPROFILE,
		DONE
	}
	private char switchSide = ' ';
	private char scaleSide = ' ';
	
     // Wheelbase Width = 27.5 in
	
     // Do something with the new Trajectories...

	 Path pathOne = new PathOne();
	 Path pathTwo = new PathTwo();
	
	 private Path selectedPath = null;
     
     State currentState;
     
     double time = 0;
     
     public MotionProfile() {
    	 currentState = State.START;
     }
     	@Override
    	 public void update() {
     		switch (currentState) {
			case START:
				selectedPath = selectPath(pathOne, pathTwo, "switch");
				loadPath(selectedPath);
				setHighGear(false);
				startPathFollower();
				currentState = State.RUNPROFILE;
				break;
				
			case RUNPROFILE:
				if (isPathDone())  {
					currentState = State.DONE;
				}
				break;
			case DONE:
				break;
 		}
    		time--;
    		SmartDashboard.putString("Auto state", currentState.toString());
    }
     	
     	@Override
	public void reset() {
		currentState = State.START;
	}
}