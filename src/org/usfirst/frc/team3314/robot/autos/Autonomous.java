package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;

public abstract class Autonomous {
	
	private Drive drive = Drive.getInstance();
	
	public abstract void reset();

	public abstract void update();
	
	public abstract void setGameData(String data);
	
	public void resetSensors() {
		drive.resetSensors();
	}

}
