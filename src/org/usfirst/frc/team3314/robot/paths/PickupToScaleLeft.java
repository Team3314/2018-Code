package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class PickupToScaleLeft extends Path {
	public PickupToScaleLeft() {
		leftPath = new File("/home/lvuser/paths/PickupToScaleLeft_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/PickupToScaleLeft_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}
