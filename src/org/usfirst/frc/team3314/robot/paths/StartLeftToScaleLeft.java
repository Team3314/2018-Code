package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartLeftToScaleLeft extends Path {
	public StartLeftToScaleLeft() {
		leftPath = new File("/home/lvuser/paths/StartLeftToScaleLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartLeftToScaleLeft_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
