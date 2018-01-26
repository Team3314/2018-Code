package org.usfirst.frc.team3314.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
	
	private enum State {
		HOLDING,
		INTAKING,
		UNJAMMING,
		RELEASING
	}

	private static Intake mInstance = new Intake();
	
	//TODO add intake sensor, finish state machine
	
	private WPI_TalonSRX mRollerLeft, mRollerRight;
	private DigitalInput cubeSensor;
	private double leftSpeed, rightSpeed;
	
	private State currentState;
	
	private boolean hasCube = false;
	
	public static Intake getInstance() {
		return mInstance;
	}
	
	public void update() {
		switch(currentState) {
			case HOLDING:
				
				break;
			case INTAKING:
				
				break;
			case UNJAMMING:
				
				break;
			case RELEASING:
				
				break;
		}
		outputToSmartDashboard();
		mRollerLeft.set(ControlMode.PercentOutput, leftSpeed);
		mRollerRight.set(ControlMode.PercentOutput, rightSpeed);
	}
	
	private Intake() {
		mRollerLeft = new WPI_TalonSRX(6); //May be talons or victors, not decided yet
		mRollerLeft.configPeakCurrentLimit(15, 0);
		mRollerLeft.enableCurrentLimit(true);
		
		mRollerRight = new WPI_TalonSRX(7);//May be talons or victors, not decided yet
		mRollerRight.configPeakCurrentLimit(15, 0);
		mRollerRight.enableCurrentLimit(true);
		
		cubeSensor = new DigitalInput(5);
	}
	
	public void setDesiredSpeed(double speed) {
		leftSpeed = speed;
		rightSpeed = speed;
	}
	
	private boolean senseCube() {
		return true;
	}
	
	public boolean hasCube() {
		return hasCube;
	}
	
	public void setDesiredState(State desiredState) {
		currentState = desiredState;
	}
	
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Intake Roller 1 Voltage", mRollerLeft.getMotorOutputVoltage());
		SmartDashboard.putNumber("Intake Roller 2 Voltage", mRollerRight.getMotorOutputVoltage());
		SmartDashboard.putNumber("Intake Roller 1 Output Percent", mRollerLeft.getMotorOutputPercent());
		SmartDashboard.putNumber("Intake Roller 2 Output Percent", mRollerRight.getMotorOutputPercent());
	}
}
