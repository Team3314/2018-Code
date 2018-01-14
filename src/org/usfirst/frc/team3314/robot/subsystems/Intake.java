package org.usfirst.frc.team3314.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {

	private static Intake mInstance = new Intake();
	
	private WPI_TalonSRX mRollerLeft, mRollerRight; //sparks maybe
	private double leftSpeed, rightSpeed;
	
	public static Intake getInstance() {
		return mInstance;
	}
	
	public void update() {
		outputToSmartDashboard();
		mRollerLeft.set(ControlMode.PercentOutput, leftSpeed);
		mRollerRight.set(ControlMode.PercentOutput, rightSpeed);
	}
	
	private Intake() {
		mRollerLeft = new WPI_TalonSRX(6);
		mRollerRight = new WPI_TalonSRX(7);
	}
	
	public void setDesiredSpeed(double speed) {
		leftSpeed = speed;
		rightSpeed = speed;
	}
	
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Intake Roller 1 Voltage", mRollerLeft.getMotorOutputVoltage());
		SmartDashboard.putNumber("Intake Roller 2 Voltage", mRollerRight.getMotorOutputVoltage());
		//SmartDashboard.putNumber("Intake Roller 1 Output Percent", mRollerLeft.getMotorOutputPercent());
		//SmartDashboard.putNumber("Intake Roller 2 Output Percent", mRollerRight.getMotorOutputPercent());
		SmartDashboard.putNumber("Intake Roller 1 Current", mRollerLeft.getOutputCurrent());
		SmartDashboard.putNumber("Intake Roller 2 Current", mRollerRight.getOutputCurrent());
	}
}
