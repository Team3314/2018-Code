package org.usfirst.frc.team3314.robot.autos;
import org.usfirst.frc.team3314.robot.motion.CSVParser;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
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
     CSVParser csvParser = new CSVParser();
	 PathFollower pathFollower = new PathFollower();
	 Drive drive = Drive.getInstance();
     
     State currentState;
     
     double time = 0;
     
     public MotionProfile() {
    	 currentState = State.START;
     }
     	@Override
    	 public void update() {
     		switch (currentState) {
			case START:
				if(switchSide == 'L') {
					csvParser.start(pathOne.getLeftPath(), pathOne.getRightPath());
				}
				else if(switchSide == 'R') {
					csvParser.start(pathTwo.getLeftPath(), pathTwo.getRightPath());
				}
				drive.setHighGear(false);
				pathFollower.start();
				currentState = State.RUNPROFILE;
				break;
				
			case RUNPROFILE:
				pathFollower.checkDone();
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