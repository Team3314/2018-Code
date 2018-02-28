package org.usfirst.frc.team3314.robot.paths;

import java.io.File;

public abstract class Path {
	
	public enum Mode {
		FORWARD_HIGH,
		FORWARD_LOW,
		BACKWARD_HIGH,
		BACKWARD_LOW
	}
	
	protected File leftPath;
	protected File rightPath;
	protected Mode mode;

	public File getLeftPath() {
		return leftPath;
	}
	public File getRightPath() {
		return rightPath;
	}
	public Mode getMode() {
		return mode;
	}
	
}
