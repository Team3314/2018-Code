package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Arm implements Subsystem {
	
	private enum ArmState {
		HOLDING,
		MOVING
	}
	
	private WPI_TalonSRX telescopingTalon, armTalon;
	private static Arm mInstance = new Arm();
	
	private ArmState state;
	
	public static Arm getInstance() {
		return mInstance;
	}
	
	public Arm() {
		telescopingTalon = new WPI_TalonSRX(8);
		telescopingTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		telescopingTalon.setSensorPhase(true);
		telescopingTalon.selectProfileSlot(0, 0);
		telescopingTalon.config_kP(0, Constants.kTelescope_kP, 0);
		telescopingTalon.config_kI(0, Constants.kTelescope_kI, 0);
		telescopingTalon.config_kD(0, Constants.kTelescope_kD, 0);
		telescopingTalon.config_kF(0, Constants.kTelescope_kF, 0);
		
		
		
		armTalon = new WPI_TalonSRX(9);
		armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
		armTalon.setSensorPhase(true);
		armTalon.selectProfileSlot(0, 0);
		armTalon.config_kP(0, Constants.kArm_kP, 0); //slot, value, timeout
		armTalon.config_kI(0, Constants.kArm_kI, 0);
		armTalon.config_kD(0, Constants.kArm_kD, 0);
		armTalon.config_kF(0, Constants.kArm_kF, 0);
		armTalon.configMotionProfileTrajectoryPeriod(Constants.kArmMotionControlTrajectroyPeriod, 0);
		armTalon.changeMotionControlFramePeriod(Constants.kArmMotionControlFramePeriod);
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
