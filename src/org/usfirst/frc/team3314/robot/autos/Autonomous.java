package org.usfirst.frc.team3314.robot.autos;

import java.io.File;

import org.usfirst.frc.team3314.robot.HumanInput;
import org.usfirst.frc.team3314.robot.motion.CSVParser;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.PathList;
import org.usfirst.frc.team3314.robot.subsystems.Arm;
import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Intake;

public abstract class Autonomous {
	
	private char switchSide = ' ';
	private char scaleSide = ' ';
	
	private Drive drive = Drive.getInstance();
	private Intake intake = Intake.getInstance();
	private Arm arm = Arm.getInstance();
	private HumanInput hi = HumanInput.getInstance();
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
	protected String startPosition() {
		return hi.getLeftRight();
	}
	protected Path getPath(String path) {
		return PathList.getPath(path);
	}
	protected String getSwitch() {
		return "Switch" + switchSide;
	}
	protected String getScale() {
		return "Scale" + scaleSide;
	}
	protected String getStart() {
		return hi.getLeftRight();
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
	public void intakeCube() {
		intake.setDesiredSpeed(1);
	}
	public void outtakeCube() {
		intake.setDesiredSpeed(-1);
	}
	public boolean hasCube() {
		return true;
	}

}
