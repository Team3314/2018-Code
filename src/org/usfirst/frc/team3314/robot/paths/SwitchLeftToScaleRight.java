package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class SwitchLeftToScaleRight extends Path {
	public SwitchLeftToScaleRight() {
		leftPath = new File("/home/lvuser/paths/SwitchLeftToScaleRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/SwitchLeftToScaleRight_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
