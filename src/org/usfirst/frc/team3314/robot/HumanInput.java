package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * 
 * @author 3314Programming
 *This class defines the controls for the robot 
 *
 *LEFT STICK:
 *Upper Middle Button - High Gear
 *Lower Middle Button - Low gear
 *
 *RIGHT STICK:
 *Trigger - Gyrolock
 *
 *XBOX CONTROLLER :
 *A - Intake Cube
 *B - Release Cube
 *X - Unjam Intake
 *Right Bumper - Intake Override
 *Left Bumpers - Arm To Outer Position
 *Left Stick Y-Axis - Move Arm
 *Start + Select - PTO toggle
 *Right Trigger + Y button - climb
 *
 */

public class HumanInput {
	
	private static HumanInput mInstance = new HumanInput();
	
	private final Joystick gamepad;
	private final Joystick leftStick;
	private final Joystick rightStick;
	private final Joystick buttonBox;
	private final Joystick autoSelector;
	
	public static HumanInput getInstance() {
		return mInstance;
	}
	
	private HumanInput() {
		gamepad = new Joystick(0);
		leftStick = new Joystick(1);
		rightStick = new Joystick(2);
		buttonBox = new Joystick(3);
		autoSelector = new Joystick(4);
	}
	
	//drive
	public double getLeftThrottle() {
		return -leftStick.getRawAxis(1);
	}
	public double getRightThrottle() {
		return -rightStick.getRawAxis(1);
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
	public boolean getEngagePTO() {
		return gamepad.getRawButton(8) && gamepad.getRawButton(7) || leftStick.getRawButton(6) && leftStick.getRawButton(7);
	}
	public boolean disengagePTO() {
		return leftStick.getRawButton(10) && leftStick.getRawButton(11);
	}
	public boolean getVisionCtrl() {
		return leftStick.getRawButton(1);
		//return gamepad.getRawButton(4);
	}
	public boolean getFullSpeedForward() {
		return rightStick.getRawButton(11) && rightStick.getRawButton(10);
	}
	
	//intake
	public boolean getIntake() {
		return gamepad.getRawButton(1);
	}
	public boolean getIntakePressed() {
		return gamepad.getRawButtonPressed(1);
	}
	public boolean getOuttake() {
		return gamepad.getRawButton(2);
	}
	public boolean getUnjam() {
		return gamepad.getRawButton(3);
	}
	public boolean getUnjamPressed() {
		return gamepad.getRawButtonPressed(3);
	}
	public boolean getIntakeOverride() {
		return gamepad.getRawButton(6);
	}
	public boolean getReleaseSlow() {
		return gamepad.getRawButton(4);
	}
	public boolean getRampRelease() {
		return gamepad.getPOV() == 180 && gamepad.getRawButton(10) || rightStick.getRawButton(10) && rightStick.getRawButton(11);
	}
	public boolean getRampClose() {
		return rightStick.getRawButton(6) && rightStick.getRawButton(7);
	}
	
	//arm
	public boolean armPowerOverride() {
		return buttonBox.getRawButton(11);
	}
	public boolean telescopePowerOverride() {
		return buttonBox.getRawButton(12);
	}
	public double getArmSpeed() {
		return Math.abs(gamepad.getRawAxis(1));
	}
	public boolean getLowerArm() {
		return -gamepad.getRawAxis(1) < -.1;
	}
	public boolean getRaiseArm() {
		return -gamepad.getRawAxis(1) > .1;
	}
	public boolean getClimb() {
		return gamepad.getRawAxis(3) > .5 && getRaiseArm();
	}
	public boolean getBar() {
		return gamepad.getRawAxis(3) > .5 && getLowerArm();
	}
	public boolean getSwitch() {
		return gamepad.getRawAxis(2) > .5 && getRaiseArm();
	}
	public boolean getFlipCube() {
		return gamepad.getRawAxis(2) > .5 && getLowerArm();
	}
	public boolean getScaleHigh() {
		return getRaiseArm() && getArmOuterPosition() && !getSwitch();
	}
	public boolean getScaleLow() {
		return getRaiseArm() && !getArmOuterPosition() && !getSwitch();
	}
	public boolean getPickup() {
		return getLowerArm() && getArmOuterPosition();
	}
	public boolean getHold() {
		return getLowerArm() && !getArmOuterPosition();
	}
	public boolean getStop() {
		return !getLowerArm() && !getRaiseArm();
	}
	public boolean getArmOuterPosition() {
		return gamepad.getRawButton(5);
	}
	
	public double getArmOverrideSpeed() {
		return -gamepad.getRawAxis(1);
	}
	public double getTelescopeOverrideSpeed() {
		return -gamepad.getRawAxis(5);
	}
	
	public boolean spin() {
		return leftStick.getRawButton(6);
	}

	public int getDelayOne() {
		if(buttonBox.getRawButton(13))
			return 1;
		return 0;
	}
	
	public int getDelayTwo() {
		if(buttonBox.getRawButton(14))
			return 1;
		return 0;
	}
	
	public int getDelayFour() {
		if(buttonBox.getRawButton(15))
			return 1;
		return 0;
	}
	
	public int getDelayEight() {
		if(buttonBox.getRawButton(16))
			return 1;
		return 0;
	}
	
	public int getLLBinaryOne() {
		if(autoSelector.getRawButton(10))
			return 1;
		return 0;
	}
	public int getLLBinaryTwo() {
		if(autoSelector.getRawButton(11))
			return 1;
		return 0;
	}
	public int getLLBinaryFour() {
		if(autoSelector.getRawButton(12))
			return 1;
		return 0;
	}
	
	public int getLRBinaryOne() {
		if(autoSelector.getRawButton(7))
			return 1;
		return 0;
	}
	public int getLRBinaryTwo() {
		if(autoSelector.getRawButton(8))
			return 1;
		return 0;
	}
	public int getLRBinaryFour() {
		if(autoSelector.getRawButton(9))
			return 1;
		return 0;
	}
	public int getRLBinaryOne() {
		if(autoSelector.getRawButton(4))
			return 1;
		return 0;
	}
	public int getRLBinaryTwo() {
		if(autoSelector.getRawButton(5))
			return 1;
		return 0;
	}
	public int getRLBinaryFour() {
		if(autoSelector.getRawButton(6))
			return 1;
		return 0;
	}
	public int getRRBinaryOne() {
		if(autoSelector.getRawButton(1))
			return 1;
		return 0;
	}
	public int getRRBinaryTwo() {
		if(autoSelector.getRawButton(2))
			return 1;
		return 0;
	}
	public int getRRBinaryFour() {
		if(autoSelector.getRawButton(3))
			return 1;
		return 0;
	}
	public String getLeftRightCenter() {
		if(autoSelector.getRawButton(13)) {
			return "StartL"; // Start Left
		} else if(autoSelector.getRawButton(14)) 
			return "StartR";
		return "StartC"; // Start Right
	}
}