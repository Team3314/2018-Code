package org.usfirst.frc.team3314.robot;

import org.usfirst.frc.team3314.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Timer;

public class DriveTrainCharacterizer {
	
	public static enum TestMode {
		QUASI_STATIC, STEP_VOLTAGE;
	}

	public static enum Direction {
		Forward, Backward;
	}
	
	private Drive drive = Drive.getInstance();
	private HumanInput hi = HumanInput.getInstance();
	private Direction direction;
	private TestMode mode;
	double driveSpeed;
	private boolean highGear;
	private double voltageStep = 1.0 / 48.0 / 50.0;
	
	public DriveTrainCharacterizer(TestMode m, Direction d, boolean high) {
		direction = d;
		mode = m;
		highGear = high;
	}

	private double timeStamp = 0;
	private boolean stopped = false;
	
	public void initialize() {
		drive.resetSensors();
		String name;
		double scale;
		if (direction.equals(Direction.Forward)) {
			name = "Forward";
			scale = 1;
		} 
		else {
			name = "Backward";
			scale = -1;
		}
		if (mode.equals(TestMode.QUASI_STATIC)) {
			drive.newFile("QuasiStatic" + name + highGear);
			driveSpeed = 0;
			voltageStep *= scale;
		} 
		else {
			drive.newFile("Step" + name + highGear);
			double speed = 0.5;
			driveSpeed = speed * scale;
		}
		drive.setHighGear(highGear);
	}
	
	public void run() {
		if(!stopped) {
			if(mode.equals(TestMode.QUASI_STATIC))
				driveSpeed += voltageStep;
			drive.setStickInputs(driveSpeed, driveSpeed);
			drive.update();
			drive.characterizationLog();
		}
		printLooptime();
		if(hi.getIntakePressed()) {
			stop();
		}
	}
	
	public void stop() {
		drive.setStickInputs(0, 0);
		drive.stopLogger();
		stopped = true;
	}
	
	public void printLooptime() {
		System.out.println(Timer.getFPGATimestamp() - timeStamp);
		timeStamp = Timer.getFPGATimestamp();
	}
}
