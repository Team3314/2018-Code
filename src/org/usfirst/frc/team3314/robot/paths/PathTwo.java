package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

import org.usfirst.frc.team3314.robot.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class PathTwo implements Path{
	private File motionProfileTwo = new File("motionProfileTwo.csv");
    Trajectory trajectoryOne = Pathfinder.readFromCSV(motionProfileTwo);
     TankModifier modifier = new TankModifier(trajectoryOne).modify(Constants.kWheelbaseWidth); // <- inside to inside of tracks ##  0.7493);
     Trajectory leftTrajectory = modifier.getLeftTrajectory();
     Trajectory rightTrajectory = modifier.getRightTrajectory();
     EncoderFollower left = new EncoderFollower(leftTrajectory);
     EncoderFollower right = new EncoderFollower(rightTrajectory);
     
     public EncoderFollower getLeftEncoderFollower() {
     	return left;
	}
     public EncoderFollower getRightEncoderFollower() {
      	return right;
 	}
}
