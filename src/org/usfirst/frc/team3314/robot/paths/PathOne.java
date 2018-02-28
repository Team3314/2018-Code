package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class PathOne extends Path {
	public PathOne() {
		leftPath = new File("/home/lvuser/paths/MotionProfileOne_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/MotionProfileOne_right_detailed.csv");
		mode = Mode.FORWARD_HIGH;
	}
}
