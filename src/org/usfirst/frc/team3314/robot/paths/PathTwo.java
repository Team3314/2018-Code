package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

import org.usfirst.frc.team3314.robot.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class PathTwo implements Path{
	private File leftPath = new File("/home/lvuser/paths/Turn_left_detailed.csv");
	private File rightPath = new File("/home/lvuser/paths/Turn_right_detailed.csv");
	
	@Override
	public File getLeftPath() {
		// TODO Auto-generated method stub
		return leftPath;
	}

	@Override
	public File getRightPath() {
		// TODO Auto-generated method stub
		return rightPath;
	}

}
