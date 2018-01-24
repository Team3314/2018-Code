package org.usfirst.frc.team3314.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake implements Subsystem {

	private static Intake mInstance = new Intake();
	
	private VictorSPX rollerLeft, rollerRight;
	private double leftSpeed, rightSpeed;
	
	public static Intake getInstance() {
		return mInstance;
	}
	
	public void update() {
		outputToSmartDashboard();
		rollerLeft.set(ControlMode.PercentOutput, leftSpeed);
		rollerRight.set(ControlMode.PercentOutput, rightSpeed);
	}
	
	private Intake() {
		rollerLeft = new VictorSPX(6);
		rollerRight = new VictorSPX(7);
	}
	
	public void setDesiredSpeed(double speed) {
		leftSpeed = speed;
		rightSpeed = speed;
	}
	
	public void resetSensors() {
	}
	
	private void outputToSmartDashboard() {
		SmartDashboard.putNumber("Intake Roller 1 Voltage", rollerLeft.getMotorOutputVoltage());
		SmartDashboard.putNumber("Intake Roller 2 Voltage", rollerRight.getMotorOutputVoltage());
		SmartDashboard.putNumber("Intake Roller 1 Output Percent", rollerLeft.getMotorOutputPercent());
		SmartDashboard.putNumber("Intake Roller 2 Output Percent", rollerRight.getMotorOutputPercent());
	}
}
