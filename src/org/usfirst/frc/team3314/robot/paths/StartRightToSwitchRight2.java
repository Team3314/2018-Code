package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartRightToSwitchRight2 implements Path {
	
	private File leftPath = new File("/home/lvuser/paths/StartRightToSwitchRight2_left_detailed.csv");
	private File rightPath = new File("/home/lvuser/paths/StartRightToSwitchRight2_right_detailed.csv");

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
