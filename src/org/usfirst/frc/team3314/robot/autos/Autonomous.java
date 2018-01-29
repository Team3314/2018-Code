package org.usfirst.frc.team3314.robot.autos;

import java.io.File;

import org.usfirst.frc.team3314.robot.HumanInput;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.PathList;
import org.usfirst.frc.team3314.robot.subsystems.Arm;
import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
import org.usfirst.frc.team3314.robot.subsystems.Intake.IntakeState;
import edu.wpi.first.wpilibj.Timer;

public abstract class Autonomous {
	
	private char switchSide = ' ';
	private char scaleSide = ' ';
	
	private Drive drive = Drive.getInstance();
	private Intake intake = Intake.getInstance();
	private Arm arm = Arm.getInstance();
	private HumanInput hi = HumanInput.getInstance();
	private PathFollower pathFollower = new PathFollower();
	private Timer timer = new Timer();
	
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
	public void resetTimer() {
		timer.reset();
	}
	public void startTimer() {
		timer.start();
	}
	public double getTime() {
		return timer.get();
	}
	public void intakeCube() {
		intake.setDesiredState(IntakeState.INTAKING);
	}
	public void releaseCube() {
		intake.setDesiredState(IntakeState.RELEASING);
	}
	public void stopIntake()  {
		intake.setDesiredState(IntakeState.HOLDING);
	}
	public boolean hasCube() {
		return intake.getState() == IntakeState.HOLDING && intake.senseCube();
	}
	public boolean hasNoCube() {
		return intake.getState() == IntakeState.HOLDING && !intake.senseCube();
	}

}
