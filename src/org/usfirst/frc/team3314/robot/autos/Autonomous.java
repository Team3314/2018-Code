package org.usfirst.frc.team3314.robot.autos;

import java.io.File;

import org.usfirst.frc.team3314.robot.motion.CSVParser;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.subsystems.Arm;
import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Intake;

public abstract class Autonomous {
	
	private char switchSide = ' ';
	private char scaleSide = ' ';
	
	private Drive drive = Drive.getInstance();
	private Intake intake = Intake.getInstance();
	private Arm arm = Arm.getInstance();
	private PathFollower pathFollower = new PathFollower();
	private CSVParser parser = new CSVParser();
	
	public abstract void reset();

	public abstract void update();
	
	protected void resetSensors() {
		drive.resetSensors();
	}
	protected void setHighGear(boolean highGear) {
		drive.setHighGear(highGear);
	}
	protected void loadPath(Path path) {
		pathFollower.loadPoints(path);
	}
	protected void startPathFollower() {
		pathFollower.start();
	}
	protected boolean isPathDone() {
		return pathFollower.isDone();
	}
	protected Path selectPath(Path leftPath, Path rightPath, String firstTarget) {
		if (firstTarget == "switch") {
			if(switchSide == 'L') {
				return leftPath;
			}
			else if (switchSide == 'R') {
				return rightPath;
			}
		}
		else if(firstTarget == "scale") {
			if(scaleSide == 'L') {
				return leftPath;
			}
			else if(scaleSide == 'R') {
				return rightPath;
			}
		}
		System.out.println("NO MATCH DATA FOUND WHEN SELECTING PATH");
		return null;
	}
	
	protected Path selectPath(Path leftSwitchLeftScale, Path leftSwitchRightScale, Path rightSwitchLeftScale, Path rightSwitchRightScale) {
		if(switchSide == 'L' && scaleSide == 'L') {
			return leftSwitchLeftScale;
		}
		else if (switchSide == 'L' && scaleSide == 'R') {
			return leftSwitchRightScale;
		}
		else if (switchSide == 'R' && scaleSide == 'L') {
			return rightSwitchLeftScale;
		}
		else if (switchSide == 'R' && scaleSide == 'R') {
			return rightSwitchRightScale;
		}
		System.out.println("NO MATCH DATA FOUND WHEN SELECTING PATH");
		return null;
	}
	public void setGameData(String data) {
		if(data.length() >= 2) {
			switchSide = data.charAt(0);
			scaleSide = data.charAt(1);
		}
		else {
			System.out.println("NO MATCH DATA RECIEVED");
		}
	}

}
