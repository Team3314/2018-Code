package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

import org.usfirst.frc.team3314.robot.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class PathOne implements Path {
	private File leftPath = new File("/home/lvuser/paths/MotionProfileOne_left_detailed.csv");
	private File rightPath = new File("/home/lvuser/paths/MotionProfileOne_right_detailed.csv");
	
	@Override
	public File getLeftPath() {
		return leftPath;
	}
	@Override
	public File getRightPath() {
		return rightPath;
	}


}
