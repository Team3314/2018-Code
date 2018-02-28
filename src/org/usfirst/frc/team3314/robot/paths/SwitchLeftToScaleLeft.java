package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class SwitchLeftToScaleLeft extends Path {
	public SwitchLeftToScaleLeft() {
		leftPath = new File("/home/lvuser/paths/SwitchLeftToScaleLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/SwitchLeftToScaleLeft_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
