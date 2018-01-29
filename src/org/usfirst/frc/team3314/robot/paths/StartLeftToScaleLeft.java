package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public class StartLeftToScaleLeft implements Path {
	private File leftPath = new File("/home/lvuser/paths/StartLeftToScaleLeft_left_detailed.csv");
	private File rightPath = new File("/home/lvuser/paths/StartLeftToScaleLeft_right_detailed.csv");
	
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
