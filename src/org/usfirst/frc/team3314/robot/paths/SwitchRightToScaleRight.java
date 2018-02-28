package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class SwitchRightToScaleRight extends Path {
	public SwitchRightToScaleRight() {
		leftPath = new File("/home/lvuser/paths/SwitchRightToScaleRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/SwitchRightToScaleRight_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
