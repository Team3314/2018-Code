package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartRightToScaleRight extends Path {
	public StartRightToScaleRight() {
		leftPath = new File("/home/lvuser/paths/StartRightToScaleRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartRightToScaleRight_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
