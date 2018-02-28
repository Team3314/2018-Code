package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class ScaleLeftToSwitchRight extends Path {
	public ScaleLeftToSwitchRight() {
		leftPath = new File("/home/lvuser/paths/ScaleLeftToSwitchRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/ScaleLeftToSwitchRight_right_detailed.csv");
		mode = Mode.FORWARD_HIGH;
	}
}
