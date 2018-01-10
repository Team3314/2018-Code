package org.usfirst.frc.team3314.robot.autos;
import java.io.File;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.Robot;
import org.usfirst.frc.team3314.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

enum motionProfileStates {
	START,
	RUNPROFILE,
	DONE
}

public class MotionProfile {
	File myFile = new File("motionprofile.csv");
	
	private Drive drive = Drive.getInstance();

     Trajectory trajectory = Pathfinder.readFromCSV(myFile);
     // Wheelbase Width = 27.5 in
     TankModifier modifier = new TankModifier(trajectory).modify(27.5/12); // <- inside to inside of tracks ##  0.7493);
     

     // Do something with the new Trajectories...
     Trajectory leftTrajectory = modifier.getLeftTrajectory();
     Trajectory rightTrajectory = modifier.getRightTrajectory();
     
     EncoderFollower left = new EncoderFollower(modifier.getLeftTrajectory());
     EncoderFollower right = new EncoderFollower(modifier.getRightTrajectory());
     
     motionProfileStates currentState;
     motionProfileStates nextState;
     
     double time = 0;
     
     public MotionProfile() {
    	 currentState = motionProfileStates.START;
     }
     	
    	 public void update() {
    			calcNext();
    			doTransition();
    			currentState = nextState;
    			time--;
    		}
     	
    		public void reset() {
    			currentState = motionProfileStates.START;
    		}
    		

     	public void calcNext() {
    			nextState = currentState;
    			switch (currentState) {
    				case START:
    					nextState = motionProfileStates.RUNPROFILE;
    					break;
    					
    				case RUNPROFILE:
    					double l = left.calculate(drive.getLeftPosition());
    					double r = right.calculate(drive.getRightPosition());
    					double gyroHeading = drive.getAngle();
    					double desiredHeading = Pathfinder.r2d(left.getHeading());
    					double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
    					double turn = Constants.kMotionProfileGyro_kP  * angleDifference;
    					
    					drive.setDesiredSpeed(l - turn, r + turn);
    					if (left.isFinished() && right.isFinished())  {
    						nextState = motionProfileStates.DONE;
    					}
    					break;
    				case DONE:
    					break;
    			}
    			
    			
    		}
     	
    		public void doTransition() {
    			if (currentState == motionProfileStates.START && nextState == motionProfileStates.RUNPROFILE) {	
    				left.configureEncoder(drive.getLeftPosition(), 2048 , Constants.kPulleyDiameter / 12);
    				right.configureEncoder(drive.getRightPosition(), 2048 , Constants.kPulleyDiameter / 12);
    				left.configurePIDVA(1.0, 0.0, 0.0, 1 / Constants.kMaxVelocity, 0);
    			
    				}
    			if (currentState == motionProfileStates.RUNPROFILE && nextState == motionProfileStates.DONE) {
    				
  
    		}
    	 
     }

}