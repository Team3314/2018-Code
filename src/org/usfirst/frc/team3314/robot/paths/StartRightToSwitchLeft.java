package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartRightToSwitchLeft extends Path {
	public StartRightToSwitchLeft() {
		leftPath = new File("/home/lvuser/paths/StartRightToSwitchLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartRightToSwitchLeft_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
