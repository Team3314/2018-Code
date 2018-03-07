package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.DataLogger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
		RAISE_CUBE,
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
	
	private double armOverrideSpeed = 0, telescopeOverrideSpeed = 0;
	
	private PowerDistributionPanel pdp;
	
	private boolean armOverride = false;
	private boolean telescopeOverride = false;
	
	private DataLogger logger = new DataLogger();
	
	public static Arm getInstance() {
		return mInstance;
	}
	
	public Arm() {
		telescopeTalon = new WPI_TalonSRX(3);
		telescopeTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kCANTimeout);
		telescopeTalon.getSensorCollection().setPulseWidthPosition(0, Constants.kCANTimeout);
		if(telescopeTalon.getSensorCollection().getPulseWidthPosition() >= Math.abs(Constants.kTelescopeEncoderOffset + 220)) {
			telescopeTalon.setSelectedSensorPosition((telescopeTalon.getSensorCollection().getPulseWidthPosition() + Constants.kTelescopeEncoderOffset), 0, Constants.kCANTimeout);
		}
		else {
			telescopeTalon.setSelectedSensorPosition((telescopeTalon.getSensorCollection().getPulseWidthPosition() - Constants.kTelescopeEncoderOffset), 0, Constants.kCANTimeout);
		}
		telescopeTalon.setSensorPhase(false);
		telescopeTalon.setInverted(false);
		telescopeTalon.selectProfileSlot(0, Constants.kCANTimeout);
		telescopeTalon.config_kP(0, Constants.kTelescope_kP, Constants.kCANTimeout);
		telescopeTalon.config_kI(0, Constants.kTelescope_kI, Constants.kCANTimeout);
		telescopeTalon.config_kD(0, Constants.kTelescope_kD, Constants.kCANTimeout);
		telescopeTalon.config_kF(0, Constants.kTelescope_kF, Constants.kCANTimeout);
		telescopeTalon.configMotionAcceleration(Constants.kMaxTelescopeAcceleration, Constants.kCANTimeout);
		telescopeTalon.configMotionCruiseVelocity(0, Constants.kCANTimeout);
		telescopeTalon.configForwardSoftLimitThreshold(Constants.kMaxTelescopePosition, Constants.kCANTimeout);
		telescopeTalon.configReverseSoftLimitThreshold(Constants.kTelescopeMinPosition, Constants.kCANTimeout);
		telescopeTalon.configForwardSoftLimitEnable(true, Constants.kCANTimeout);
		telescopeTalon.configReverseSoftLimitEnable(true, Constants.kCANTimeout);
		telescopeTalon.configPeakCurrentLimit(Constants.kTelescopePeakCurrentLimit, Constants.kCANTimeout);
		telescopeTalon.configPeakCurrentDuration(Constants.kTelescopePeakCurrentDuration, Constants.kCANTimeout);
		telescopeTalon.configContinuousCurrentLimit(Constants.kTelescopeContinuousCurrentLimit, Constants.kCANTimeout);
		telescopeTalon.enableCurrentLimit(true);
		
		
		armTalon = new WPI_TalonSRX(6);
		armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kCANTimeout);
		armTalon.getSensorCollection().setPulseWidthPosition(0, Constants.kCANTimeout);
		armTalon.setSelectedSensorPosition((armTalon.getSensorCollection().getPulseWidthPosition() + Constants.kArmEncoderOffset), 0, Constants.kCANTimeout);
		armTalon.setSensorPhase(false);
		armTalon.setInverted(true);
		armTalon.selectProfileSlot(0, Constants.kCANTimeout);
		armTalon.config_kP(0, Constants.kArm_kP, Constants.kCANTimeout); //slot, value, timeout
		armTalon.config_kI(0, Constants.kArm_kI, Constants.kCANTimeout);
		armTalon.config_kD(0, Constants.kArm_kD, Constants.kCANTimeout);
		armTalon.config_kF(0, Constants.kArm_kF, Constants.kCANTimeout);
		armTalon.configMotionAcceleration(Constants.kMaxArmAcceleceration, Constants.kCANTimeout);
		armTalon.configMotionCruiseVelocity(0, Constants.kCANTimeout);
		armTalon.configForwardSoftLimitThreshold(Constants.kMaxArmPosition, Constants.kCANTimeout);
		armTalon.configReverseSoftLimitThreshold(Constants.kArmMinPosition, Constants.kCANTimeout);
		armTalon.configForwardSoftLimitEnable(true, Constants.kCANTimeout);
		armTalon.configReverseSoftLimitEnable(true, Constants.kCANTimeout);
		armTalon.configPeakCurrentLimit(Constants.kArmPeakCurrentLimit, Constants.kCANTimeout);
		armTalon.configPeakCurrentDuration(Constants.kArmPeakCurrentDuration, Constants.kCANTimeout);
		armTalon.configContinuousCurrentLimit(Constants.kArmContinuousCurrentLimit, Constants.kCANTimeout);
		armTalon.enableCurrentLimit(true);
		
	}
	
	public void startUp() {
		armTalon.configMotionCruiseVelocity(0, 0);
		telescopeTalon.configMotionCruiseVelocity(0, 0);
		targetArmAngle = getArmAngleTicks();
		targetTelescopePosition = getTelescopePositionTicks();
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
		armLength = telescopePosition + 40;
		radius = Math.hypot(armLength, Constants.kShortSideOfArm);
		radiusAngle = 12 + armAngle;
		targetArmVelocity = (int)(targetSpeed * Constants.kMaxArmAngularVelocity);
		targetTelescopeVelocity = (int)(targetSpeed * Constants.kMaxTelescopeVelocity);
		if(radius >= 43) {
			armAngleLimit = (Math.toDegrees(Math.acos(43/radius)) +12) / Constants.kArmTicksToAngle;
		}
		else {
			armAngleLimit = 0;
		}
		//armLog();
		switch(currentState) {
		case TO_HOLDING:
			targetArmAngle = Constants.kHoldAngle;
			targetTelescopePosition = Constants.kHoldTelescopePosition;
			if(inPosition()) {
				targetArmAngle = armEncPos;
				currentState = ArmState.STOPPED;
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
				targetArmAngle = armEncPos;
				currentState = ArmState.STOPPED;
			}
			break;
		case RAISE_CUBE:
			targetTelescopePosition = 0;
			targetArmAngle = Constants.raiseCubeAngle;
			if(armAngle > -49) {
				if(desiredState == ArmState.TO_HOLDING || desiredState == ArmState.TO_PICKUP ) {
					currentState = desiredState;
				}
				else{
					currentState = ArmState.TELESCOPE_IN;
				}
			}
			break;
		case TO_SWITCH:
			targetArmAngle = Constants.kSwitchAngle;
			targetTelescopePosition = Constants.kSwitchTelescopePosition;
			if(inPosition()) {
				targetArmAngle = armEncPos;
				currentState = ArmState.STOPPED;
			}
			break;
		case TO_SCALE_LOW:
			targetArmAngle = Constants.kScaleLowAngle;
			targetTelescopePosition = Constants.kScaleLowTelescopePosition;
			if(armAngle < 10) {
				targetTelescopeVelocity = 0;
			}
			if(inPosition()) {
				targetArmAngle = getArmAngleTicks();
				currentState = ArmState.STOPPED;
			}
			break;
		case TO_SCALE_HIGH:
			targetArmAngle = Constants.kScaleHighAngle;
			targetTelescopePosition = Constants.kScaleHighTelescopePosition;
			if(armAngle < 10) {
				targetTelescopeVelocity = 0;
			}
			if(inPosition()) {
				targetArmAngle =armEncPos;
				currentState = ArmState.STOPPED;
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
			targetArmVelocity = 0;
			targetTelescopeVelocity = 0;
			if(getArmVelocity() == 0) {
				currentState = ArmState.STOPPED;
				targetArmAngle = armEncPos;
			}

		case STOPPED:
			break;
		}
		if(!armOverride) {
			armTalon.set(ControlMode.MotionMagic, targetArmAngle);
			armTalon.configMotionCruiseVelocity(targetArmVelocity, 0);
		}
		else if(armOverride) {
			armTalon.set(ControlMode.PercentOutput, armOverrideSpeed);
		}
		if (!telescopeOverride) {
			telescopeTalon.set(ControlMode.MotionMagic, targetTelescopePosition);
			telescopeTalon.configMotionCruiseVelocity(targetTelescopeVelocity, 0);
		}
		else if(telescopeOverride) {
			telescopeTalon.set(ControlMode.PercentOutput, telescopeOverrideSpeed);
		}
	}
	
	public void setDesiredState(ArmState desiredState) {
		this.desiredState = desiredState;
		if(desiredState == ArmState.TO_HOLDING) {
			if (telescopeEncPos > Constants.kHoldTelescopePosition + 100) {
				currentState = ArmState.TO_INTERMEDIATE_LOW;
			}
			else {
				currentState = ArmState.TO_HOLDING;
			}
		}
		else if(desiredState == ArmState.TO_PICKUP) {
			if(armAngle < ((Constants.kPickUpAngle * Constants.kArmTicksToAngle) - 2) || armAngle > ((Constants.kPickUpAngle * Constants.kArmTicksToAngle) + 3)) {
				currentState = ArmState.TO_INTERMEDIATE_LOW;
			}
			else {
				currentState = ArmState.TO_PICKUP;;
			}
				
		}
		else if(desiredState == ArmState.TO_SCALE_HIGH || desiredState == ArmState.TO_SCALE_LOW) {
			if(radiusAngle < -20) {
				targetArmAngle = armEncPos;
				currentState = ArmState.TELESCOPE_IN;
			}
			else {
				currentState = desiredState;
			}
		}
		else if(desiredState  == ArmState.TO_SWITCH) {
			currentState = ArmState.TO_SWITCH;
		}
		else if(desiredState == ArmState.TO_CLIMB || desiredState == ArmState.LOWER_TO_BAR) {
			if(radiusAngle <-20) {
				targetArmAngle = armEncPos;
				currentState = ArmState.TELESCOPE_IN;
			}
			else {
				currentState = ArmState.TO_CLIMB;
			}
		}
		else if (desiredState == ArmState.STOP) {
			if((currentState == ArmState.STOPPED)) {
				currentState = ArmState.STOPPED;
			}
			else if(!(currentState == ArmState.STOPPED)) {
				currentState = ArmState.STOP;
				targetTelescopePosition = telescopeEncPos;
				targetArmAngle = armEncPos;
			}
		}
		if(currentState == ArmState.TELESCOPE_IN || currentState == ArmState.TO_INTERMEDIATE_LOW) {
			if(radiusAngle < 0 && telescopePosition > 10) {
				currentState = ArmState.RAISE_CUBE;
			}
		}
	}
	public void setTargetSpeed(double targSpeed) {
		targetSpeed = targSpeed;
	}
	
	public void setArmOverrideSpeed(double armSpeed) {
		armOverrideSpeed = armSpeed;
	}
	
	public void setTelescopeOverrideSpeed(double speed) {
		telescopeOverrideSpeed = speed;
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
	public double getArmVelocityTicks() {
		return armTalon.getSelectedSensorVelocity(0);
	}
	public double getTelescopeVelocityTicks() {
		return telescopeTalon.getSelectedSensorVelocity(0);
	}
	
	public void setArmOverride(boolean armOverride) {
		armTalon.configReverseSoftLimitEnable(!armOverride, 0);
		armTalon.configForwardSoftLimitEnable(!armOverride, 0);
		this.armOverride = armOverride;
	}
	public void setTelescopeOverride(boolean telescopeOverride) {
		telescopeTalon.configReverseSoftLimitEnable(!telescopeOverride, 0);
		telescopeTalon.configForwardSoftLimitEnable(!telescopeOverride, 0);
		this.telescopeOverride = telescopeOverride;
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
	
	public void newFile(String name) {
		logger.createNewFile(name);
	}
	
	public void armLog() {
		String[] names = {"Arm Position (Degrees)", "Telescope Position(Inches)", "Arm Position (Ticks)", "Telescope Position (Ticks)",
				"Arm Velocity (degrees/sec)", "Telescope Velocity (Inches / sec)", "Arm Velocity(Ticks)", "Telescope Velocity (Ticks)",
				"Arm Current", "Telescope Current", "Arm Voltage", "Telescope Voltage", "Battery Voltage", "Arm Target Position", 
				"Telescope Target Position", "Arm Target Velocity", "Telescope Target Velocity"
				};
    	String[] values = {String.valueOf(getArmAngle()), String.valueOf(getTelescopePosition()),String.valueOf(getArmAngleTicks()), String.valueOf(getTelescopePositionTicks()),
    			String.valueOf(getArmVelocity()), String.valueOf(getTelescopeVelocity()), String.valueOf(getArmVelocityTicks()), String.valueOf(getTelescopeVelocityTicks()),
    			String.valueOf(armTalon.getOutputCurrent()), String.valueOf(telescopeTalon.getOutputCurrent()), String.valueOf(armTalon.getMotorOutputVoltage()), String.valueOf(telescopeTalon.getMotorOutputVoltage()), String.valueOf(pdp.getVoltage()),
    			String.valueOf(armTalon.getActiveTrajectoryPosition()), String.valueOf(telescopeTalon.getActiveTrajectoryPosition()), String.valueOf(armTalon.getActiveTrajectoryVelocity()), String.valueOf(telescopeTalon.getActiveTrajectoryVelocity())    			
    			};
    	logger.logData(names, values);
	}
	
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Arm Angle (Degrees)", getArmAngle());
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
		SmartDashboard.putNumber("Arm current", armTalon.getOutputCurrent());
		SmartDashboard.putNumber("Telescope current", telescopeTalon.getOutputCurrent());
		SmartDashboard.putNumber("Arm Angle Limit", armAngleLimit);
	}
	@Override
	public void resetSensors() {
	}
	 public void stopLogger() {
	    	logger.stop();
	 }
}