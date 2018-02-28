package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class ScaleRightToSwitchLeft extends Path {
	public ScaleRightToSwitchLeft() {
		leftPath = new File("/home/lvuser/paths/ScaleRightToSwitchLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/ScaleRightToSwitchLeft_right_detailed.csv");
		mode = Mode.FORWARD_HIGH;
	}
}
