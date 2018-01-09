package org.usfirst.frc.team3314.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Intake {

	private static Intake mInstance = new Intake();
	
	private WPI_TalonSRX mRoller1, mRoller2; //sparks maybe?
	
	public static Intake getInstance() {
		return mInstance;
	}
	
	public void update() {
		
	}
	
	private Intake() {
		mRoller1 = new WPI_TalonSRX(6);
		mRoller2 = new WPI_TalonSRX(7);
	}
	
	public void outputToSmartDashboard() {
		
	}
}
