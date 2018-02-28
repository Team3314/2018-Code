package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartRightToSwitchRight extends Path {
	public StartRightToSwitchRight() {
		leftPath = new File("/home/lvuser/paths/StartRightToSwitchRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartRightToSwitchRight_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
