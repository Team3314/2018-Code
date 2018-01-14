package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.Joystick;

public class HumanInput {
	
	private static HumanInput mInstance = new HumanInput();
	
	private final Joystick gamepad;
	private final Joystick leftStick;
	private final Joystick rightStick;
	private final Joystick buttonBox;
	
	public static HumanInput getInstance() {
		return mInstance;
	}
	
	private HumanInput() {
		gamepad = new Joystick(0);
		leftStick = new Joystick(1);
		rightStick = new Joystick(2);
		buttonBox = new Joystick(3);
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
	
	public boolean getIntake() {
		return gamepad.getRawButton(1);
	}
	
	public boolean getOuttake() {
		return gamepad.getRawButton(2);
	}
	public int getBinaryOne() {
		if(buttonBox.getRawButton(13)) {
			return 1;
		}
		return 0;
	}
	
	public int getBinaryTwo() {
		if(buttonBox.getRawButton(14)) {
			return 1;
		}
		return 0;
	}
	
	public int getBinaryFour() {
		if(buttonBox.getRawButton(15)) {
			return 1;
		}
		return 0;
	}
	
	public int getBinaryEight() {
		if(buttonBox.getRawButton(16)) {
			return 1;
		}
		return 0;
	}
	public boolean getRed() {
		return buttonBox.getRawButton(12);
	}
	public boolean getBlue() {
		boolean result = false;
		if (!buttonBox.getRawButton(12)) {
			result = true;
		}
		return result;
	}
}