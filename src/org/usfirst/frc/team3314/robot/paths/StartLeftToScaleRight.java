package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartLeftToScaleRight extends Path {
	public StartLeftToScaleRight() {
		leftPath = new File("/home/lvuser/paths/StartLeftToScaleRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartLeftToScaleRight_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
