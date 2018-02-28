package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartCenterToSwitchRight extends Path {
	public StartCenterToSwitchRight() {
		leftPath = new File("/home/lvuser/paths/StartCenterToSwitchRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/StartCenterToSwitchRight_right_detailed.csv");
		mode = Mode.FORWARD_HIGH;
	}
}
