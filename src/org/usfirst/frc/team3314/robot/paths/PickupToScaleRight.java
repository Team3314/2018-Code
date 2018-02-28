package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class PickupToScaleRight extends Path {
	public PickupToScaleRight() {
		leftPath = new File("/home/lvuser/paths/PickupToScaleRight_left_detailed.csv");
		rightPath = new File("/home/lvuser/paths/PickupToScaleRight_right_detailed.csv");
		mode = Mode.BACKWARD_HIGH;
	}
}