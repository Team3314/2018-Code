package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartLeftToSwitchRight implements Path {
	private File leftPath = new File("/home/lvuser/paths/StartLeftToSwitchRight_left_detailed.csv");
	private File rightPath = new File("/home/lvuser/paths/StartLeftToSwitchRight_right_detailed.csv");
	
	@Override
	public File getLeftPath() {
		// TODO Auto-generated method stub
		return leftPath;
	}

	@Override
	public File getRightPath() {
		// TODO Auto-generated method stub
		return rightPath;
	}

}