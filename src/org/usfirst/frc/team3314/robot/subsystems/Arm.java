package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm implements Subsystem {
	
	public enum ArmState {
		TO_INTERMEDIATE_LOW,
		TELESCOPE_IN,
		TO_HORIZONTAL,
		TO_SCALE_HIGH ,
		TO_SCALE_LOW,
		TO_SWITCH,
		TO_PICKUP,
		TO_HOLDING,
		TO_CLIMB,
		LOWER_TO_BAR,
		STOP,
		STOPPED
	}
	
	private WPI_TalonSRX telescopeTalon, armTalon;
	private static Arm mInstance = new Arm();
	
	private double telescopeEncPos;
	private double armLength;
	private double radius;
	private double radiusAngle;
	private double armEncPos;
	private double armAngularVelocity, telescopeVelocity;
   
	private double targetArmAngle = 0;
	private double targetTelescopePosition = 0;
	
	private double armAngleLimit;
	
	private int targetArmVelocity, targetTelescopeVelocity;
	
	private ArmState currentState = ArmState.STOPPED;
	private ArmState desiredState = ArmState.STOPPED;

	private double targetSpeed;
	private double armAngle;
	private double telescopePosition;
	
	public static Arm getInstance() {
		return mInstance;
	}
	
	public Arm() {
		telescopeTalon = new WPI_TalonSRX(3);
		telescopeTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		telescopeTalon.setSelectedSensorPosition(telescopeTalon.getSensorCollection().getPulseWidthPosition() + Constants.kTelescopeEncoderOffset, 0, 0);
		telescopeTalon.setSensorPhase(false);
		telescopeTalon.setInverted(false);
		telescopeTalon.selectProfileSlot(0, 0);
		telescopeTalon.config_kP(0, Constants.kTelescope_kP, 0);
		telescopeTalon.config_kI(0, Constants.kTelescope_kI, 0);
		telescopeTalon.config_kD(0, Constants.kTelescope_kD, 0);
		telescopeTalon.config_kF(0, Constants.kTelescope_kF, 0);
		telescopeTalon.configMotionAcceleration(Constants.kMaxTelescopeAcceleration, 0);
		telescopeTalon.configMotionCruiseVelocity(0, 0);
		telescopeTalon.configForwardSoftLimitThreshold(Constants.kMaxTelescopePosition, 0);
		telescopeTalon.configReverseSoftLimitThreshold(Constants.kTelescopeMinPosition, 0);
		telescopeTalon.configForwardSoftLimitEnable(true, 0);
		telescopeTalon.configReverseSoftLimitEnable(true, 0);
		
		armTalon = new WPI_TalonSRX(6);
		armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		armTalon.setSelectedSensorPosition(armTalon.getSensorCollection().getPulseWidthPosition() + Constants.kArmEncoderOffset, 0, 0);
		armTalon.setSensorPhase(false);
		armTalon.setInverted(true);
		armTalon.selectProfileSlot(0, 0);
		armTalon.config_kP(0, Constants.kArm_kP, 0); //slot, value, timeout
		armTalon.config_kI(0, Constants.kArm_kI, 0);
		armTalon.config_kD(0, Constants.kArm_kD, 0);
		armTalon.config_kF(0, Constants.kArm_kF, 0);
		armTalon.configMotionAcceleration(Constants.kMaxArmAcceleceration, 0);
		armTalon.configMotionCruiseVelocity(0, 0);
		armTalon.configForwardSoftLimitThreshold(Constants.kMaxArmPosition, 0);
		armTalon.configReverseSoftLimitThreshold(Constants.kArmMinPosition, 0);
		armTalon.configForwardSoftLimitEnable(true, 0);
		armTalon.configReverseSoftLimitEnable(true, 0);
	}
	
	public void startUp() {
		armTalon.configMotionCruiseVelocity(0, 0);
		telescopeTalon.configMotionCruiseVelocity(0, 0);
		targetArmAngle = getArmAngleTicks();
		targetTelescopePosition = getTelescopePosition();
		armTalon.set(ControlMode.MotionMagic, targetArmAngle);
		telescopeTalon.set(ControlMode.MotionMagic, targetTelescopePosition);
		currentState = ArmState.STOPPED;
		armTalon.disable();
		telescopeTalon.disable();
	}

	@Override
	public void update() {
		armEncPos = getArmAngleTicks(); // zeroed at horizontal
		telescopeEncPos = getTelescopePositionTicks();
		armAngle = getArmAngle();
		telescopePosition = getTelescopePosition();
		armLength = telescopePosition + Constants.kIntakeAdditionToLength + Constants.kMinArmLength;
		radius = Math.hypot(armLength, Constants.kShortSideOfArm);
		radiusAngle = Math.atan(Constants.kShortSideOfArm/armLength) + armAngle;
		targetArmVelocity = (int)(targetSpeed * Constants.kMaxArmAngularVelocity);
		targetTelescopeVelocity = (int)(targetSpeed * Constants.kMaxTelescopeVelocity);
		if(radius >= 43) {
			armAngleLimit = Math.acos(43/radius) - Math.atan(Constants.kShortSideOfArm/armLength);
		}
		else {
			armAngleLimit = 0;
		}
		switch(currentState) {
		case TO_HOLDING:
			targetArmAngle = Constants.kHoldAngle;
			targetTelescopePosition = Constants.kHoldTelescopePosition;
			if(inPosition()) {
				currentState = ArmState.STOP;
			}
			break;
		case TELESCOPE_IN:
			targetTelescopePosition = Constants.kTelescopeMinPosition;
			if(telescopeInPosition()) {
				currentState = ArmState.TO_HORIZONTAL;
			}
			break;
		case TO_HORIZONTAL:
			targetArmAngle = Constants.kScaleLowAngle;
			targetTelescopePosition = Constants.kTelescopeMinPosition;
			if(radiusAngle > 0) {
				currentState = desiredState;
			}
			break;
		case TO_INTERMEDIATE_LOW:
			targetArmAngle = Constants.kPickUpAngle;
			targetTelescopePosition = Constants.kHoldTelescopePosition;
			if(armAngleLimit != 0 && radiusAngle > 0) {
				targetArmAngle = armAngleLimit;
			}
			if(inPosition()) {
				currentState = desiredState;
			}
			break;
		case TO_PICKUP:
			targetArmAngle = Constants.kPickUpAngle;
			targetTelescopePosition = Constants.kPickUpTelescopePosition;
			if(inPosition()) {
				currentState = ArmState.STOP;
			}
			break;
		case TO_SWITCH:
			targetArmAngle = Constants.kSwitchAngle;
			targetTelescopePosition = Constants.kSwitchTelescopePosition;
			if(inPosition()) {
				currentState = ArmState.STOP;
			}
			break;
		case TO_SCALE_LOW:
			targetArmAngle = Constants.kScaleLowAngle;
			targetTelescopePosition = Constants.kScaleLowTelescopePosition;
			if(getArmAngle() < 10) {
				targetTelescopeVelocity = 0;
			}
			if(inPosition()) {
				currentState = ArmState.STOP;
			}
			break;
		case TO_SCALE_HIGH:
			targetArmAngle = Constants.kScaleHighAngle;
			targetTelescopePosition = Constants.kScaleHighTelescopePosition;
			if(getArmAngle() < 10) {
				targetTelescopeVelocity = 0;
			}
			if(inPosition()) {
				currentState = ArmState.STOP;
			}
			break;
		case TO_CLIMB:
			targetArmAngle = Constants.kClimbRaisedAngle;
			targetTelescopePosition = Constants.kClimbRaisedTelescopePosition;
			if(inPosition()) {
				currentState = desiredState;
			}
			break;
		case LOWER_TO_BAR:
			targetArmAngle = Constants.kBarAngle;
			targetTelescopePosition = Constants.kBarTelescopePosition;
			break;
		case STOP:
			if(getArmVelocity() == 0) {
				currentState = ArmState.STOPPED;
				targetArmAngle = getArmAngleTicks();
			}

		case STOPPED:
			break;
		}
		armTalon.set(ControlMode.MotionMagic, targetArmAngle);
		telescopeTalon.set(ControlMode.MotionMagic, targetTelescopePosition);
		armTalon.configMotionCruiseVelocity(targetArmVelocity, 0);
		telescopeTalon.configMotionCruiseVelocity(targetTelescopeVelocity, 0);
	}
	
	public void setDesiredState(ArmState desiredState) {
		this.desiredState = desiredState;
		if(desiredState == ArmState.TO_HOLDING) {
			if (getTelescopePosition() > Constants.kHoldTelescopePosition + 1) {
				currentState = ArmState.TO_INTERMEDIATE_LOW;
			}
			else {
				currentState = ArmState.TO_HOLDING;
			}
		}
		else if(desiredState == ArmState.TO_PICKUP) {
				currentState = ArmState.TO_INTERMEDIATE_LOW;
		}
		else if(desiredState == ArmState.TO_SCALE_HIGH || desiredState == ArmState.TO_SCALE_LOW) {
			if(radiusAngle < -20) {
				targetArmAngle = getArmAngleTicks();
				currentState = ArmState.TELESCOPE_IN;
			}
			else {
				currentState = desiredState;
			}
		}
		else if(desiredState  == ArmState.TO_SWITCH) {
			if(radiusAngle < -20 && getTelescopePosition() > Constants.kSwitchTelescopePosition + 1) {
				targetArmAngle = getArmAngleTicks();
				currentState = ArmState.TELESCOPE_IN;
			}
			else {
				currentState = ArmState.TO_SWITCH;
			}
		}
		else if(desiredState == ArmState.TO_CLIMB || desiredState == ArmState.LOWER_TO_BAR) {
			if(radiusAngle <-20) {
				targetArmAngle = getArmAngleTicks();
				currentState = ArmState.TELESCOPE_IN;
			}
			else {
				currentState = ArmState.TO_CLIMB;
			}
		}
		else if (desiredState == ArmState.STOP) {
			currentState = ArmState.STOP;
			targetTelescopePosition = getTelescopePositionTicks();
			targetArmAngle = getArmAngleTicks();
		}
	}
	public void setTargetSpeed(double targSpeed) {
		targetSpeed = targSpeed;
	}
	
	public double getArmAngle() {
		return armTalon.getSelectedSensorPosition(0) * Constants.kArmTicksToAngle; 
	}
	
	public double getTelescopePosition() {
		return telescopeTalon.getSelectedSensorPosition(0) * Constants.kTelescopeTicksToInches;
	}
	public double getArmAngleTicks() {
		return armTalon.getSelectedSensorPosition(0);
	}
	
	public double getTelescopePositionTicks() {
		return telescopeTalon.getSelectedSensorPosition(0);
	}
	public double getTelescopeVelocity() {
		return telescopeTalon.getSelectedSensorVelocity(0) * Constants.kTelescopeTicksToInches * 10;
	}
	public double getArmVelocity() {
		return armTalon.getSelectedSensorVelocity(0) * Constants.kArmTicksToAngle * 10;
	}
	
	public boolean inPosition() {
		return (armInPosition() && telescopeInPosition());
	}
	
	public boolean armInPosition() {
		return Math.abs(armEncPos - targetArmAngle) < Constants.kArmTargetThreshold;
	}
	
	public boolean telescopeInPosition() {
		return Math.abs(telescopeEncPos - targetTelescopePosition) < Constants.kTelescopeTargetThreshold;
	}
	public boolean  armEncoderPresent() {
		return armTalon.getSensorCollection().getPulseWidthRiseToRiseUs() != 0;
	}
	public boolean  telescopeEncoderPresent() {
		return telescopeTalon.getSensorCollection().getPulseWidthRiseToRiseUs() != 0;
	}
	
	public ArmState getState() {
		return currentState;
	}
	
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Arm Angle (Degrees)", armAngle);
		SmartDashboard.putNumber("Arm Length (Inches) ", armLength);
		SmartDashboard.putNumber("Telescope Position (Inches)", getTelescopePosition());
		SmartDashboard.putNumber("Radius (Inches)", radius);
		SmartDashboard.putNumber("Radius angle", radiusAngle);
		SmartDashboard.putNumber("Arm Angle Ticks", getArmAngleTicks());
		SmartDashboard.putNumber("Telescope Position Ticks", getTelescopePositionTicks());
		SmartDashboard.putNumber("Arm Velocity", getArmVelocity());
		SmartDashboard.putNumber("Telescope Velocity", getTelescopeVelocity());
		SmartDashboard.putString("Arm State", currentState.toString());
		SmartDashboard.putNumber("Target Arm Velocity", targetArmVelocity);
		SmartDashboard.putNumber("Target Telescsope Velocity", targetTelescopeVelocity);
		SmartDashboard.putNumber("Target Arm Position", targetArmAngle);
		SmartDashboard.putNumber("Target Telescope Position", targetTelescopePosition);
		SmartDashboard.putBoolean("Arm Encoder Present", armEncoderPresent());
		SmartDashboard.putBoolean("Telescope Encoder Present", telescopeEncoderPresent());
		
	}
	@Override
	public void resetSensors() {
	}
}