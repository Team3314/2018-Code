package org.usfirst.frc.team3314.robot.paths;

import jaci.pathfinder.followers.EncoderFollower;

public interface Path {

	public EncoderFollower getLeftEncoderFollower();
	
	public EncoderFollower getRightEncoderFollower();
	
}
