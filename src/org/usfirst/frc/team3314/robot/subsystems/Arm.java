package org.usfirst.frc.team3314.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Arm implements Subsystem {
	
	private enum State {
		HOLDING,
		MOVING
	}
	
	private WPI_TalonSRX telescopingTalon = new WPI_TalonSRX(8);
	private WPI_TalonSRX armMaster = new WPI_TalonSRX(9);
	private WPI_TalonSRX armSlave = new WPI_TalonSRX(10);
	private static Arm mInstance = new Arm();
	
	public static Arm getInstance() {
		return mInstance;
	}

	@Override
	public void update() {

	}

	@Override
	public void outputToSmartDashboard() {

	}

	@Override
	public void resetSensors() {

	}

}
