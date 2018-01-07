package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.Joystick;

public class HumanInterface {
	
	private static HumanInterface mInstance = new HumanInterface();
	
	private final Joystick gamepad;
	private final Joystick leftStick;
	private final Joystick rightStick;
	
	public static HumanInterface getInstance() {
		return mInstance;
	}
	
	private HumanInterface() {
		gamepad = new Joystick(0);
		leftStick = new Joystick(1);
		rightStick = new Joystick(2);
	}
	
	public double getLeftThrottle() {
		return leftStick.getRawAxis(1);
	}
	
	public double getRightThrottle() {
		return rightStick.getRawAxis(1);
	}
	
	public boolean getLowGear() {
		return leftStick.getRawButton(2);
	}
	
	public boolean getHighGear() {
		return leftStick.getRawButton(3);
	}
	
	public boolean getGyrolock() {
		return rightStick.getRawButton(1);
	}
	
	public boolean getFullSpeedForward() {
		return rightStick.getRawButton(11) && rightStick.getRawButton(10);
	}
	
	
	

}
