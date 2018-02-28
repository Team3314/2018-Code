package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartLeftToSwitchLeft extends Path {
	public StartLeftToSwitchLeft() {
		leftPath = new File("/home/lvuser/paths/StartLeftToSwitchLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartLeftToSwitchLeft_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
