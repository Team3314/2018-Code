package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class ScaleRightToSwitchRight extends Path {
	public ScaleRightToSwitchRight() {
		leftPath = new File("/home/lvuser/paths/ScaleRightToSwitchRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/ScaleRightToSwitchRight_right_detailed.csv");
		mode = Mode.FORWARD_HIGH;
	}
}
