package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartCenterToSwitchLeft extends Path {
	public StartCenterToSwitchLeft() {
		leftPath = new File("/home/lvuser/paths/StartCenterToSwitchLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartCenterToSwitchLeft_right_detailed.csv");
		mode = Mode.FORWARD_HIGH;
	}
}
