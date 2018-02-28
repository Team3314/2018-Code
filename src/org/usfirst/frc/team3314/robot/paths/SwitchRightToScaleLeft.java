package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class SwitchRightToScaleLeft extends Path {
	public SwitchRightToScaleLeft() {
		leftPath = new File("/home/lvuser/paths/SwitchRightToScaleLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/SwitchRightToScaleLeft_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
