package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class ScaleLeftToSwitchLeft extends Path {
	public ScaleLeftToSwitchLeft() {
		leftPath = new File("/home/lvuser/paths/ScaleLeftToSwitchLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/ScaleLeftToSwitchLeft_right_detailed.csv");
		mode = Mode.FORWARD_HIGH;
	}
}
