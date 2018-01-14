package org.usfirst.frc.team3314.robot.autos;
import org.usfirst.frc.team3314.robot.paths.PathFollower;
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

	 PathOne pathOne = new PathOne();
	 PathTwo pathTwo = new PathTwo();
     PathFollower pathFollower = new PathFollower();
	
     State currentState;
     
     double time = 0;
     
     public MotionProfile() {
    	 currentState = State.START;
     }
     	@Override
    	 public void update() {
     		switch (currentState) {
			case START:
				currentState = State.RUNPROFILE;
				break;
				
			case RUNPROFILE:
				if(switchSide == 'L')
					pathFollower.followPath(pathOne);
				else if(switchSide == 'R') 
					pathFollower.followPath(pathTwo);
				if (pathFollower.isDone())  {
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
		@Override
		public void setGameData(String data) {
			// TODO Auto-generated method stub
			switchSide = data.charAt(0);
			scaleSide = data.charAt(1);
		}
}