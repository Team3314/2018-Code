package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartRightToScaleLeft extends Path {
	public StartRightToScaleLeft() {
		leftPath = new File("/home/lvuser/paths/StartRightToScaleLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartRightToScaleLeft_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
