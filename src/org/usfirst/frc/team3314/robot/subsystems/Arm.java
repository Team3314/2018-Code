package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.motion.MotionProfileFollower;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm implements Subsystem {
	
	public enum ArmState {
		HOLDING,
		MOVE_TO_POSITION,
		FREE_MOVE
	}
	
	class PeriodicRunnable implements java.lang.Runnable {
		@Override 
		public void run() {
			if(!running) 
				notifier.stop();
			
			//The arm is modeled as a right triangle, with the hypotenuse as the distance from pivot to tip of intake
			// Arm has several constraints - rotation,
			armAngle = getArmAngle(); // zeroed at horizontal
			armLength = getTelescopePosition() + Constants.kIntakeAdditionToLength + Constants.kMinArmLength;
			radius = Math.sqrt(Math.pow(armLength, 2) + Math.pow(Constants.kShortSideOfArm, 2)); //a^2 + b^2 = c^2
			radiusAngle = armAngle + Constants.kArmToRadiusAngle;
			
			armPositionX = armLength * Math.cos(armAngle);
			armPositionY = armLength * Math.sin(armAngle) + Constants.kPivotPointHeight;
			
			intakePositionX = radius * Math.cos(radiusAngle);
			intakePositionY = (radius * Math.sin(radiusAngle)) + Constants.kPivotPointHeight;
			
			armStopTime = armAngularVelocity/Constants.kMaxArmAcceleceration;
			telescopeStopTime = telescopeVelocity / Constants.kMaxTelescopeAcceleration;
			
			armTalon.getMotionProfileStatus(armStatus);
			telescopeTalon.getMotionProfileStatus(telescopeStatus);
			
			stopAngle = (armAngularVelocity * armStopTime) + (.5 * Constants.kMaxArmAcceleceration * armStopTime * armStopTime);
			stopTelescopePosition = (telescopeVelocity * telescopeStopTime) + (.5 * Constants.kMaxTelescopeAcceleration * telescopeStopTime * telescopeStopTime);
			
			
			
			switch(state) {
				case HOLDING:
					armTalon.set(ControlMode.MotionProfile, 1);
					generating = false;
					break;
				case MOVE_TO_POSITION:
					armTalon.set(ControlMode.MotionProfile, 1);
					generating = true;
					break;
				case FREE_MOVE:
					armTalon.set(ControlMode.MotionProfile, 1);
					follower.moveAt(Constants.kMaxArmAngularVelocity * armStickInput, Constants.kMaxTelescopeVelocity * telescopeStickInput);
					generating = true;
					break;
			}
			if (generating) {
					if (armPositionSetpoint == targetArmAngle && telescopePositionSetpoint == targetTelescopePosition) {
						state = ArmState.HOLDING;
					}
					if(armStatus.btmBufferCnt < 5) {
						armTalon.pushMotionProfileTrajectory(follower.calcNextArm());
						armTalon.processMotionProfileBuffer();
					}
					if(telescopeStatus.btmBufferCnt < 5) {
						telescopeTalon.pushMotionProfileTrajectory(follower.calcNextTelescope());
						telescopeTalon.processMotionProfileBuffer();
					}
			}
				
		}
	}
	Notifier notifier = new Notifier(new PeriodicRunnable());
	
	private Timer timer = new Timer();
	
	private MotionProfileStatus armStatus, telescopeStatus;
	
	private MotionProfileFollower follower = new MotionProfileFollower();
	
	private WPI_TalonSRX telescopeTalon, armTalon;
	private static Arm mInstance = new Arm();
	
	private double armLength;
	private double radius;
	private double radiusAngle;
	private double armPositionX;
	private double armPositionY;
	private double intakePositionX;
	private double intakePositionY;
	private double armAngle;
	
	private double armStopTime, telescopeStopTime;
	
	private double armAngularVelocity, telescopeVelocity;
	
	private double targetArmVelocity = 0;
	private double targetTelescopeVeloctiy = 0;
	
	private double targetArmAngle = 0;
	private double targetTelescopePosition = 0;
	
	private double telescopeVelocitySetpoint = 0;
	private double telescopePositionSetpoint = 0;
	private TrajectoryPoint telescopePoint = new TrajectoryPoint();
	
	private double armVelocitySetpoint = 0;
	private double armPositionSetpoint = 0;
	private TrajectoryPoint armPoint = new TrajectoryPoint();
	
	private double stoppingPositionX = 0;
	private double stoppingPositionY = 0;
	
	private double stopAngle = 0;
	private double stopTelescopePosition = 0;
	
	private double armStickInput = 0;
	private double telescopeStickInput = 0;
	
	private ArmState state = ArmState.HOLDING;
	
	private boolean generating = false, running = false;
	
	public static Arm getInstance() {
		return mInstance;
	}
	
	public Arm() {
		telescopeTalon = new WPI_TalonSRX(3);
		telescopeTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
		telescopeTalon.setSensorPhase(true);
		telescopeTalon.selectProfileSlot(0, 0);
		telescopeTalon.config_kP(0, Constants.kTelescope_kP, 0);
		telescopeTalon.config_kI(0, Constants.kTelescope_kI, 0);
		telescopeTalon.config_kD(0, Constants.kTelescope_kD, 0);
		telescopeTalon.config_kF(0, Constants.kTelescope_kF, 0);
		telescopeTalon.configMotionProfileTrajectoryPeriod(Constants.kArmMotionControlTrajectroyPeriod, 0);
		telescopeTalon.changeMotionControlFramePeriod(Constants.kArmMotionControlFramePeriod);
		telescopeTalon.configForwardSoftLimitThreshold(0, 0);
		telescopeTalon.configReverseSoftLimitThreshold(0, 0);
		telescopeTalon.configForwardSoftLimitEnable(false, 0);
		telescopeTalon.configReverseSoftLimitEnable(false, 0);
		
		
		armTalon = new WPI_TalonSRX(6);
		armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
		armTalon.setSensorPhase(true);
		armTalon.selectProfileSlot(0, 0);
		armTalon.config_kP(0, Constants.kArm_kP, 0); //slot, value, timeout
		armTalon.config_kI(0, Constants.kArm_kI, 0);
		armTalon.config_kD(0, Constants.kArm_kD, 0);
		armTalon.config_kF(0, Constants.kArm_kF, 0);
		armTalon.configMotionProfileTrajectoryPeriod(Constants.kArmMotionControlTrajectroyPeriod, 0);
		armTalon.changeMotionControlFramePeriod(Constants.kArmMotionControlFramePeriod);
		armTalon.configForwardSoftLimitThreshold(0, 0);
		armTalon.configReverseSoftLimitThreshold(0, 0);
		armTalon.configForwardSoftLimitEnable(false, 0);
		armTalon.configReverseSoftLimitEnable(false, 0);
	}
	
	public void start() {
		notifier.startPeriodic(.005);
		running = true;
	}
	
	public void stop() {
		running = false;
	}

	@Override
	public void update() {
		outputToSmartDashboard();
	}

	public void setTargetArmVelocity(double targVel) {
		targetArmVelocity = targVel;
	}
	
	public void setTargetTelescopeVelocity(double targVel) {
		targetTelescopeVeloctiy = targVel;
	}
	
	public void goToScalePosition() {
		setTargetPosition(Constants.kArmScaleAngle, Constants.kArmScaleTelescopePosition);
	}
	
	public void goToPickUpPosition() { 
		setTargetPosition(Constants.kArmPickUpAngle, Constants.kArmPickUpTelescopePosition);
	}
	
	public void goToSwitchPosition() {
		setTargetPosition(Constants.kArmSwitchAngle, Constants.kArmSwitchTelescopePosition);
	}
	
	public void goToHoldingPosition() {
		setTargetPosition(Constants.kArmHoldAngle, Constants.kArmHoldTelescopePosition);
	}
	
	public void setTargetPosition(double angle, double telescopePos) {
		state = ArmState.MOVE_TO_POSITION;
		targetArmAngle = angle;
		targetTelescopePosition = telescopePos;
	}
	
	public void setStickInputs(double armStick, double telescopeStick) {
		armStickInput = armStick;
		telescopeStickInput = telescopeStick;
	}
	
	public double getArmAngle() {
		return armTalon.getSelectedSensorPosition(0) * Constants.kArmTicksToAngle + Constants.kArmEncoderOffset;
	}
	
	public double getTelescopePosition() {
		return telescopeTalon.getSelectedSensorPosition(0) * Constants.kTelescopeTicksToInches + Constants.kTelescopeEncoderOffset;
	}
	
	public ArmState getState() {
		return state;
	}
	
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Arm Angle (Degrees)", Math.toDegrees(armAngle));
		SmartDashboard.putNumber("Arm Length (Inches) ", armLength);
		SmartDashboard.putNumber("Arm Position X (Inches)", armPositionX);
		SmartDashboard.putNumber("Arm Position Y (Inches)", armPositionY);
		SmartDashboard.putNumber("Radius", radius);
		
	}

	@Override
	public void resetSensors() {

	}

}
