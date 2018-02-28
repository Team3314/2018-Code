package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartLeftToSwitchRight extends Path {
	public StartLeftToSwitchRight() {
		leftPath = new File("/home/lvuser/paths/StartLeftToSwitchRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartLeftToSwitchRight_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
