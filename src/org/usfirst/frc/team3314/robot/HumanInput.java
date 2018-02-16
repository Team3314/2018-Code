package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * 
 * @author 3314Programming
 *This class defines the controls for the robot 
 *
 *XBOX CONTROLLER :
 *A - Intake Cube
 *B - Release Cube
 *X - Unjam Intake
 *Right Bumper - Intake Override
 *Left Trigger - Arm To Outer Position
 *Left Stick Y-Axis - Move Arm
 *Start + Select - PTO toggle
 *
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
	public boolean getPTO() {
		return gamepad.getRawButton(8) && gamepad.getRawButton(7);
	}
	public boolean getVisionCtrl() {
		return leftStick.getRawButton(1);
	}
	public boolean getFullSpeedForward() {
		return rightStick.getRawButton(11) && rightStick.getRawButton(10);
	}
	
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
	
	public double getArmSpeed() {
		return Math.abs(gamepad.getRawAxis(1));
	}
	public boolean getLowerArm() {
		return -gamepad.getRawAxis(1) < -.01;
	}
	public boolean getRaiseArm() {
		return -gamepad.getRawAxis(1) > .01;
	}
	public boolean getClimb() {
		return gamepad.getRawButton(4) && gamepad.getRawAxis(3) > .5;
	}
	public boolean getScaleHigh() {
		return getRaiseArm() && getArmOuterPosition();
	}
	public boolean getScaleLow() {
		return getRaiseArm() && !getArmOuterPosition();
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
	
	
	public int getLLBinaryOne() {
		if(autoSelector.getRawButton(1))
			return 1;
		return 0;
	}
	public int getLLBinaryTwo() {
		if(autoSelector.getRawButton(2))
			return 1;
		return 0;
	}
	public int getLLBinaryFour() {
		if(autoSelector.getRawButton(3))
			return 1;
		return 0;
	}
	public int getLLBinaryEight() {
		if(autoSelector.getRawButton(4))
			return 1;
		return 0;
	}
	public int getLRBinaryOne() {
		if(autoSelector.getRawButton(5))
			return 1;
		return 0;
	}
	public int getLRBinaryTwo() {
		if(autoSelector.getRawButton(6))
			return 1;
		return 0;
	}
	public int getLRBinaryFour() {
		if(autoSelector.getRawButton(7))
			return 1;
		return 0;
	}
	public int getLRBinaryEight() {
		if(autoSelector.getRawButton(8))
			return 1;
		return 0;
	}
	public int getRLBinaryOne() {
		if(autoSelector.getRawButton(9))
			return 1;
		return 0;
	}
	public int getRLBinaryTwo() {
		if(autoSelector.getRawButton(10))
			return 1;
		return 0;
	}
	public int getRLBinaryFour() {
		if(autoSelector.getRawButton(11))
			return 1;
		return 0;
	}
	public int getRLBinaryEight() {
		if(autoSelector.getRawButton(12))
			return 1;
		return 0;
	}
	public int getRRBinaryOne() {
		if(autoSelector.getRawButton(13))
			return 1;
		return 0;
	}
	public int getRRBinaryTwo() {
		if(autoSelector.getRawButton(14))
			return 1;
		return 0;
	}
	public int getRRBinaryFour() {
		if(autoSelector.getRawButton(15))
			return 1;
		return 0;
	}
	public int getRRBinaryEight() {
		if(autoSelector.getRawButton(16))
			return 1;
		return 0;
	}
	public String getLeftRight() {
		if(buttonBox.getRawButton(12)) {
			return "StartL"; // Start Left
		}
		return "StartR"; // Start Right
	}
}