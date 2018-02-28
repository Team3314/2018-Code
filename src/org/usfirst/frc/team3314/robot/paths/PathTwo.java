package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class PathTwo extends Path{
	public PathTwo() {
		leftPath = new File("/home/lvuser/paths/Turn_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/Turn_right_detailed.csv");
		mode = Mode.FORWARD_HIGH;
	}
}
