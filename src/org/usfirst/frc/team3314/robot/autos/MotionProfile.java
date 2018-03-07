package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.paths.Path;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MotionProfile extends Autonomous {
	
	enum State {
		START,
		RUNPROFILE,
		DONE
	}
	
	 private Path selectedPath = null;
     
     State currentState = State.START;
     
     double time = 0;
     	@Override
    	 public void update() {
     		switch (currentState) {
			case START:
				selectedPath = getPath("Drive straight");
				loadPath(selectedPath);
				setHighGear(true);
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