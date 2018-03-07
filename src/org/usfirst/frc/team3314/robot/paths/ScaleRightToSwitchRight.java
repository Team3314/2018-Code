package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class ScaleRightToSwitchRight implements Path {
	private File leftPath = new File("/home/lvuser/paths/ScaleRightToSwitchRight_left_detailed.csv");
	private File rightPath = new File("/home/lvuser/paths/ScaleRightToSwitchRight_right_detailed.csv");
	
	@Override
	public File getLeftPath() {
		
		return leftPath;
	}

	@Override
	public File getRightPath() {
		
		return rightPath;
	}

	@Override
	public boolean getBackwards() {
		
		return false;
	}
}
