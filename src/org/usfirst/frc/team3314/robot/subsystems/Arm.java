package org.usfirst.frc.team3314.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Arm implements Subsystem {

	private static Arm mInstance = new Arm();
	
	public static Arm getInstance() {
		return mInstance;
	}
	
	private WPI_TalonSRX telescopingTalon, armMaster, armSlave;
	private double desiredArmPos, desiredTelescopePos;
	
	private Arm() {
		telescopingTalon = new WPI_TalonSRX(8);
    	telescopingTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
    	
    	armMaster = new WPI_TalonSRX(9);
		armMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
		
		armSlave = new WPI_TalonSRX(10);
		armSlave.follow(armMaster);
		
		resetSensors();
	}
	
	public void setArmPosition(double armPosition) {
		desiredArmPos = armPosition;
	}
	
	public void setTelescopePosition(double telescopingPosition) {
		desiredTelescopePos = telescopingPosition;
	}
		
	@Override
	public void update() {
		telescopingTalon.set(ControlMode.Position, desiredTelescopePos);
		armMaster.set(ControlMode.Position, desiredArmPos);
		outputToSmartDashboard();
	}
	
	private void outputToSmartDashboard() {
	}

	@Override
	public void resetSensors() {
		//Only for zeroing on robot init
		armMaster.set(ControlMode.Position, 0);
		telescopingTalon.set(ControlMode.Position, 0);
		armMaster.set(ControlMode.Velocity, 0);
		telescopingTalon.set(ControlMode.Velocity, 0);
		armMaster.setSelectedSensorPosition(0, 0, 0);
		telescopingTalon.setSelectedSensorPosition(0, 0, 0);
		}
	}
