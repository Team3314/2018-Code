package org.usfirst.frc.team3314.robot.subsystems;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.DataLogger;
import org.usfirst.frc.team3314.robot.GyroPIDOutput;


import com.cruzsbrian.robolog.Log;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Drive implements Subsystem {
	
	public enum driveMode {
		IDLE,
		OPEN_LOOP,
		GYROLOCK,
		VISION_CONTROL,
		MOTION_PROFILE
	}
	
	private static Drive mInstance = new Drive();
	
	public static Drive getInstance() {
		return mInstance;
	}
	
	//Control mode
	private driveMode currentDriveMode = driveMode.OPEN_LOOP;
	ControlMode controlMode = ControlMode.PercentOutput;
	
	//Hardware
	private WPI_TalonSRX mLeftMaster, mLeftSlave1, mLeftSlave2, mRightMaster, mRightSlave1, mRightSlave2;
	private DoubleSolenoid shifter ,pto;
	private PowerDistributionPanel pdp;
	private AHRS navx;
	
	private DigitalInput leftHighGearSensor, leftLowGearSensor, rightHighGearSensor, rightLowGearSensor;
	
	//Hardware states
	private boolean mIsHighGear;
	private boolean mIsPTO;
    private NeutralMode neutralMode;
    
    //Data Logging
    public DataLogger logger;
    
    public Camera camera;
    
    //PID
	private GyroPIDOutput gyroPIDOutput;
	private PIDController gyroControl;

    private double rawLeftSpeed, rawRightSpeed, leftStickInput, rightStickInput, desiredLeftSpeed, desiredRightSpeed, desiredAngle;
    
    private int leftDrivePositionTicks, rightDrivePositionTicks, leftDriveSpeedTicks, rightDriveSpeedTicks, motionProfileMode = 0;
    
    private double leftDrivePositionInches, rightDrivePositionInches, leftDriveSpeedRPM, rightDriveSpeedRPM;
    
    Compressor pcm1;
    
    public void update() {
    	if(mIsHighGear) {
    		shifter.set(Constants.kHighGear);
    	}
    	else {
    		shifter.set(Constants.kLowGear);
    	}
    	if(mIsPTO) {
    		pto.set(Constants.kPTOIn);
    	}
    	else {
    		pto.set(Constants.kPTOOut);
    	}
    	updateSpeedAndPosition();
    	logSpeed();
    	switch(currentDriveMode) {
    		case IDLE:
    			controlMode = ControlMode.Disabled;
    			rawLeftSpeed = 0;
    			rawRightSpeed = 0;
    			break;
    		case OPEN_LOOP:
    			rawLeftSpeed = leftStickInput;
    			rawRightSpeed = rightStickInput;
    			controlMode = ControlMode.PercentOutput;
    			break;
    		case GYROLOCK:    			
    			rawLeftSpeed = desiredLeftSpeed + gyroPIDOutput.turnSpeed;
    			rawRightSpeed = desiredRightSpeed - gyroPIDOutput.turnSpeed;
    			gyroControl.setSetpoint(desiredAngle);
    			controlMode = ControlMode.PercentOutput;
    			break;
    		case VISION_CONTROL:
    			rawLeftSpeed = leftStickInput + camera.getSteeringAdjust();
    			rawRightSpeed = rightStickInput - camera.getSteeringAdjust();
    			controlMode = ControlMode.PercentOutput;
    			//rawLeftSpeed = camera.getSteeringAdjust();
    			//rawRightSpeed = -camera.getSteeringAdjust();
    			//controlMode = ControlMode.MotionMagic;
    			break;
    		case MOTION_PROFILE:
    			log();
    			controlMode = ControlMode.MotionProfile;
    			rawLeftSpeed = motionProfileMode;
    			rawRightSpeed = motionProfileMode;
    			break;
    	}
    	if(mIsPTO) {
			rawRightSpeed = rawLeftSpeed;
		}
    	mLeftMaster.set(controlMode, rawLeftSpeed);
    	mRightMaster.set(controlMode, rawRightSpeed);
    
    }

	

    private Drive() {
    	// Logger
    	 logger = new DataLogger();
    	 camera = Camera.getInstance();
    	
		//Hardware
    	pdp  = new PowerDistributionPanel(0);
    	shifter = new DoubleSolenoid(0, 1);
    	pto = new DoubleSolenoid(2, 3);
    	navx = new AHRS(SPI.Port.kMXP);
    	
    	//Gyro PID
    	gyroPIDOutput = new GyroPIDOutput();
    	gyroControl = new PIDController(Constants.kGyroLock_kP, Constants.kGyroLock_kI, Constants.kGyroLock_kD,
    		Constants.kGyroLock_kF, navx, gyroPIDOutput);
		//Sets the PID controller to treat 180 and -180 to be the same point, 
		//so that when turning the robot takes the shortest path instead of going the long way around
		//Effectively changes PID input from a line to a circle
		gyroControl.setInputRange(-180, 180);
	    gyroControl.setContinuous(); 
		gyroControl.setOutputRange(-.7, .7);		// Limits speed of turn to prevent overshoot
    	
		
		//Talons
    	mLeftMaster = new WPI_TalonSRX(0);
    	mLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    	mLeftMaster.setInverted(false);
    	mLeftMaster.setSensorPhase(true);
    	mLeftMaster.configMotionProfileTrajectoryPeriod(Constants.kDriveMotionControlTrajectoryPeriod, 0);
    	mLeftMaster.changeMotionControlFramePeriod(Constants.kDriveMotionControlFramePeriod);
    	mLeftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic,  5, 0);
    	mLeftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0,  5, 0);

    	//motion profile gains
    	mLeftMaster.selectProfileSlot(Constants.kMotionProfileSlot, 0);
    	mLeftMaster.config_kP(0, Constants.kMotionProfile_kP, 0); //slot, value, timeout
    	mLeftMaster.config_kI(0, Constants.kMotionProfile_kI, 0);
    	mLeftMaster.config_kD(0, Constants.kMotionProfile_kD, 0);
    	mLeftMaster.config_kF(0, Constants.kMotionProfile_kF, 0);
    	
    	//vision ctrl gains
    	mLeftMaster.config_kP(Constants.kVisionCtrlSlot, Constants.kVisionCtrl_kP, 0); //slot, value, timeout
    	mLeftMaster.config_kI(Constants.kVisionCtrlSlot, Constants.kVisionCtrl_kI, 0);
    	mLeftMaster.config_kD(Constants.kVisionCtrlSlot, Constants.kVisionCtrl_kD, 0);
    	mLeftMaster.config_kF(Constants.kVisionCtrlSlot, Constants.kVisionCtrl_kF, 0);
    	mLeftMaster.configMotionCruiseVelocity(Constants.kDrivetrainCruiseVelocity, 0);
    	mLeftMaster.configMotionAcceleration(Constants.kDrivetrainAcceleration, 0);
    	
    	//Gyrolock gains
    	mLeftMaster.config_kP(Constants.kGyroLockSlot, Constants.kGyroLock_kP, 0); //slot, value, timeout
    	mLeftMaster.config_kI(Constants.kGyroLockSlot, Constants.kGyroLock_kI, 0);
    	mLeftMaster.config_kD(Constants.kGyroLockSlot, Constants.kGyroLock_kD, 0);
    	mLeftMaster.config_kF(Constants.kGyroLockSlot, Constants.kGyroLock_kF, 0);
    	
    	mLeftSlave1 = new WPI_TalonSRX(1);
    	mLeftSlave1.follow(mLeftMaster);
    	mLeftSlave1.setInverted(false);
    	
    	mLeftSlave2 = new WPI_TalonSRX(2);
    	mLeftSlave2.follow(mLeftMaster);
    	mLeftSlave2.setInverted(false);
    	
    	leftHighGearSensor = new DigitalInput(1);
    	leftLowGearSensor = new DigitalInput(0);
    	
    	mRightMaster = new WPI_TalonSRX(7);
    	mRightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    	mRightMaster.setSensorPhase(true);
    	mRightMaster.setInverted(true);
    	mRightMaster.configMotionProfileTrajectoryPeriod(Constants.kDriveMotionControlTrajectoryPeriod, 0);
    	mRightMaster.changeMotionControlFramePeriod(Constants.kDriveMotionControlFramePeriod);
    	//Motion Profile Gains
    	mRightMaster.selectProfileSlot(Constants.kMotionProfileSlot, 0);
    	mRightMaster.config_kP(Constants.kMotionProfileSlot, Constants.kMotionProfile_kP, 0); //slot, value, timeout
    	mRightMaster.config_kI(Constants.kMotionProfileSlot, Constants.kMotionProfile_kI, 0);
    	mRightMaster.config_kD(Constants.kMotionProfileSlot, Constants.kMotionProfile_kD, 0);
    	mRightMaster.config_kF(Constants.kMotionProfileSlot, Constants.kMotionProfile_kF, 0);
    	mRightMaster.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic,  5, 0);
    	mRightMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0,  5, 0);
    	
    	//vision ctrl gains
    	mRightMaster.config_kP(Constants.kVisionCtrlSlot, Constants.kVisionCtrl_kP, 0); //slot, value, timeout
    	mRightMaster.config_kI(Constants.kVisionCtrlSlot, Constants.kVisionCtrl_kI, 0);
    	mRightMaster.config_kD(Constants.kVisionCtrlSlot, Constants.kVisionCtrl_kD, 0);
    	mRightMaster.config_kF(Constants.kVisionCtrlSlot, Constants.kVisionCtrl_kF, 0);
    	mRightMaster.configMotionCruiseVelocity(Constants.kDrivetrainCruiseVelocity, 0);
    	mRightMaster.configMotionAcceleration(Constants.kDrivetrainAcceleration, 0);
    	
    	//Gyrolock gains
    	mRightMaster.config_kP(Constants.kGyroLockSlot, Constants.kGyroLock_kP, 0); //slot, value, timeout
    	mRightMaster.config_kI(Constants.kGyroLockSlot, Constants.kGyroLock_kI, 0);
    	mRightMaster.config_kD(Constants.kGyroLockSlot, Constants.kGyroLock_kD, 0);
    	mRightMaster.config_kF(Constants.kGyroLockSlot, Constants.kGyroLock_kF, 0);
    	
    	
    	mRightSlave1 = new WPI_TalonSRX(8);
    	mRightSlave1.follow(mRightMaster);
    	mRightSlave1.setInverted(true);
    	
    	mRightSlave2 = new WPI_TalonSRX(9);
    	mRightSlave2.follow(mRightMaster);
    	mRightSlave2.setInverted(true);
    	
    	rightHighGearSensor = new DigitalInput(3);
    	rightLowGearSensor = new DigitalInput(2);
    	
    	pcm1 = new Compressor();
    	pcm1.setClosedLoopControl(true);
    	
    	resetSensors();
    	LiveWindow.disableTelemetry(pdp);
    	setNeutralMode(NeutralMode.Brake);
    	
    	mIsHighGear = false;
    	mIsPTO = false;
    }
    
    public void setStickInputs(double leftInput, double rightInput) {
    	leftStickInput = leftInput;
    	rightStickInput = rightInput;
    }
    
    public void setDesiredAngle(double angle) {
    	desiredAngle = angle;
    }
    
    public double getAngle() {
    	return navx.getYaw();
    }
    
    public int getLeftPositionTicks() {
    	return leftDrivePositionTicks;
    }
    
    public int getRightPositionTicks() {
    	return rightDrivePositionTicks;
    }
    
    public double getLeftPosition() {
    	return leftDrivePositionInches;
    }
    
    public double getRightPosition() {
    	return rightDrivePositionInches;
    }
    
    public double getAveragePosition() {
    	return (leftDrivePositionInches+rightDrivePositionInches)/2;
    }
    
    public boolean getLeftHighGear() {
    	return !leftHighGearSensor.get();
    }
    public boolean getLeftLowGear() {
    	return !leftLowGearSensor.get();
    }
    public boolean getRightHighGear() {
    	return !rightHighGearSensor.get();
    }
    public boolean getRightLowGear() {
    	return !rightLowGearSensor.get();
    }
    
    public void setDesiredSpeed(double speed) {
    	desiredLeftSpeed = speed;
    	desiredRightSpeed = speed;
    }
    
    public void setDesiredSpeed(double leftSpeed, double rightSpeed) {
    	rawLeftSpeed = leftSpeed;
    	rawRightSpeed = rightSpeed;
    }
    
    public void setDriveMode(driveMode mode) {
    	if(mode == driveMode.GYROLOCK) {
    		gyroControl.enable();
			setDesiredAngle(getAngle());
    	}
    	else {
    		gyroControl.disable();
    	}
    	currentDriveMode = mode;
    	
    }
    
    public void setHighGear(boolean highGear) {
    	mIsHighGear = highGear;
    }
    
    public void setNeutralMode(NeutralMode mode) {
    	if(neutralMode != mode) {
    		neutralMode = mode;
    		mLeftMaster.setNeutralMode(mode);
    		mLeftSlave1.setNeutralMode(mode);
    		mLeftSlave2.setNeutralMode(mode);
    		mRightMaster.setNeutralMode(mode);
    		mRightSlave1.setNeutralMode(mode);
    		mRightSlave2.setNeutralMode(mode);
    	}
    }
    
    public void setPTO(boolean pto) {
    	mIsPTO = pto;
    }
    
    public boolean getPTO() {
    	return mIsPTO;
    }
    
    public void newFile(String name) {
    	logger.createNewFile(name);
    }
    
    public void logSpeed() {
    	String[] names = {"Left Encoder Speed", "Right Encoder Speed", "Left Encoder Position", "Right Encoder Position", "Left Encoder Position Ticks","Right Encoder Position Ticks",
    			"Left Encoder Position Setpoint",  "Right Encoder Velocity Setpoint","Left Encoder Velocity Setpoint", "Right Encoder Velocity Setpoint", "NavX Y Acceleration", "NavX X Acceleration",
    			"Left Master Current", "Left Slave 1 Current","Left Slave 2 Current", "Right Master Current", "Right Slave 1 Current", "Right Slave 2 Current",
    			"Left Master Voltage", "Left Slave 1 Voltage","Left Slave 2 Voltage", "Right Master Voltage", "Right Slave 1 Voltage", "Right Slave 2 Voltage",
    			"Battery Voltage", "High Gear"};
    	String[] values = {String.valueOf(mLeftMaster.getSelectedSensorVelocity(0)), String.valueOf(mRightMaster.getSelectedSensorVelocity(0)), 
    			String.valueOf(leftDrivePositionInches), String.valueOf(rightDrivePositionInches), String.valueOf(leftDrivePositionTicks), String.valueOf(rightDrivePositionTicks),
    			String.valueOf(mLeftMaster.getActiveTrajectoryPosition()), String.valueOf(mRightMaster.getActiveTrajectoryPosition()), String.valueOf(mLeftMaster.getActiveTrajectoryVelocity()),String.valueOf(mRightMaster.getActiveTrajectoryVelocity()),
    			String.valueOf(navx.getWorldLinearAccelY()), String.valueOf(navx.getWorldLinearAccelX()), String.valueOf(mLeftMaster.getOutputCurrent()),
    			String.valueOf(mLeftSlave1.getOutputCurrent()), String.valueOf(mLeftSlave2.getOutputCurrent()), String.valueOf(mRightMaster.getOutputCurrent()),
    			String.valueOf(mRightSlave1.getOutputCurrent()), String.valueOf(mRightSlave2.getOutputCurrent()), String.valueOf(mLeftMaster.getMotorOutputVoltage()),
    			String.valueOf(mLeftSlave1.getMotorOutputVoltage()),String.valueOf(mLeftSlave2.getMotorOutputVoltage()), String.valueOf(mRightMaster.getMotorOutputVoltage()),
    			String.valueOf(mRightSlave1.getMotorOutputVoltage()), String.valueOf(mRightSlave2.getMotorOutputVoltage()),String.valueOf(pdp.getVoltage()), String.valueOf(mIsHighGear)};
    	logger.logData(names, values);

    }
    
    public void stopLogger() {
    	logger.stop();
    }
    
    public void outputToSmartDashboard() {
    	SmartDashboard.putNumber("Left Encoder Ticks", leftDrivePositionTicks);
    	SmartDashboard.putNumber("Right Encoder Ticks", rightDrivePositionTicks);
    	SmartDashboard.putNumber("Left Encoder Position", leftDrivePositionInches);
    	SmartDashboard.putNumber("Right Encoder Position", rightDrivePositionInches);
    	SmartDashboard.putNumber("Left Encoder Speed", leftDriveSpeedRPM);
    	SmartDashboard.putNumber("Right Encoder Speed", rightDriveSpeedRPM);
    	SmartDashboard.putNumber("Left Master Current", mLeftMaster.getOutputCurrent());
    	SmartDashboard.putNumber("Left Slave 1 Current", mLeftSlave1.getOutputCurrent());
    	SmartDashboard.putNumber("Left Slave 2 Current", mLeftSlave2.getOutputCurrent());
    	SmartDashboard.putNumber("Right Master Current", mRightMaster.getOutputCurrent());
    	SmartDashboard.putNumber("Right Slave 1 Current", mRightSlave1.getOutputCurrent());
    	SmartDashboard.putNumber("Right Slave 2 Current", mRightSlave2.getOutputCurrent());
    	SmartDashboard.putString("Drive Mode", String.valueOf(currentDriveMode));
    	SmartDashboard.putString("Neutral Mode", String.valueOf(neutralMode));
    	SmartDashboard.putBoolean("High Gear Left", getLeftHighGear());
    	SmartDashboard.putBoolean("Low Gear Left", getLeftLowGear());
    	SmartDashboard.putBoolean("High Gear Right", getRightHighGear());
    	SmartDashboard.putBoolean("Low Gear Right", getRightLowGear());
    	SmartDashboard.putBoolean("High Gear", getHighGear());
    	SmartDashboard.putBoolean("Low Gear", getLowGear());
    	SmartDashboard.putBoolean("Neutral", getNeutral());
    	SmartDashboard.putNumber("Raw Left Speed", rawLeftSpeed);
    	SmartDashboard.putNumber("Raw Right Speed", rawRightSpeed);
    	SmartDashboard.putNumber("Desired Angle", desiredAngle);
    	SmartDashboard.putNumber("Current angle", navx.getAngle());
    	SmartDashboard.putNumber("Gyro adjustment", gyroPIDOutput.turnSpeed);
    	SmartDashboard.putNumber("Desired Left Speed", desiredLeftSpeed);
    	SmartDashboard.putNumber("Desired Right Speed", desiredRightSpeed);
    }
    
    public void log() {
		Log.add("Left Position Setpoint", (double)mLeftMaster.getActiveTrajectoryPosition());
		Log.add("Right Position Setpoint",(double) mRightMaster.getActiveTrajectoryPosition());
		Log.add("Left Velocity Setpoint",(double) mLeftMaster.getActiveTrajectoryVelocity());
		Log.add("Right Velocity Setpoint", (double)mRightMaster.getActiveTrajectoryVelocity());
		Log.add("Left Position", (double)mLeftMaster.getSelectedSensorPosition(0));
		Log.add("Right Position", (double)mRightMaster.getSelectedSensorPosition(0));
		Log.add("Left Velocity",(double) mLeftMaster.getSelectedSensorVelocity(0));
		Log.add("Right Velocity",(double) mRightMaster.getSelectedSensorVelocity(0));
    }
    
    private void updateSpeedAndPosition() {
    	leftDrivePositionTicks = mLeftMaster.getSelectedSensorPosition(0);
    	rightDrivePositionTicks =  mRightMaster.getSelectedSensorPosition(0);
    	leftDrivePositionInches = (double) leftDrivePositionTicks / Constants.kDriveEncoderCodesPerRev  * Constants.kRevToInConvFactor;
    	rightDrivePositionInches =  (double) rightDrivePositionTicks / Constants.kDriveEncoderCodesPerRev * Constants.kRevToInConvFactor;
    	leftDriveSpeedTicks = mLeftMaster.getSelectedSensorVelocity(0);
    	rightDriveSpeedTicks =  mRightMaster.getSelectedSensorVelocity(0);
    	leftDriveSpeedRPM = leftDriveSpeedTicks * (600.0/ Constants.kDriveEncoderCodesPerRev);
    	rightDriveSpeedRPM =  rightDriveSpeedTicks * (600.0/ Constants.kDriveEncoderCodesPerRev);
    }
  
    public void resetDriveEncoders() {
		mLeftMaster.set(ControlMode.Position, 0);
		mRightMaster.set(ControlMode.Position, 0);
		mLeftMaster.set(ControlMode.Velocity, 0);
		mRightMaster.set(ControlMode.Velocity, 0);
		mLeftMaster.set(ControlMode.MotionProfile,0);
		mRightMaster.set(ControlMode.MotionProfile, 0);
		mLeftMaster.setSelectedSensorPosition(0, 0, 0);
		mRightMaster.setSelectedSensorPosition(0, 0, 0);
	}
    
    public void resetSensors() {
    	navx.reset();
    	resetDriveEncoders();
    	mRightMaster.disable();
    	mLeftMaster.disable();
    }
    
    public boolean getHighGear() {
    	return leftHighGearSensor.get() && rightHighGearSensor.get();
    }
    public boolean getLowGear() {
    	return leftLowGearSensor.get() && rightLowGearSensor.get();
    }
    public boolean getNeutral() {
    	return !leftLowGearSensor.get() && !leftHighGearSensor.get() && !rightLowGearSensor.get() && !rightHighGearSensor.get();
    }
    
    public void pushPoints(TrajectoryPoint leftPoint, TrajectoryPoint rightPoint) {
    	mLeftMaster.pushMotionProfileTrajectory(leftPoint);
    	mRightMaster.pushMotionProfileTrajectory(rightPoint);
    }
    
    public void flushTalonBuffer() {
    	mLeftMaster.clearMotionProfileTrajectories();
    	mRightMaster.clearMotionProfileTrajectories();
    }
    
    public void processMotionProfilePoints() {
    	mLeftMaster.processMotionProfileBuffer();
    	mRightMaster.processMotionProfileBuffer();
    }
    
    public int checkLeftBuffer() {
    	return mLeftMaster.getMotionProfileTopLevelBufferCount();
    }
    
    public void getRightStatus(MotionProfileStatus status) {
   	 mRightMaster.getMotionProfileStatus(status);
   }
    
    public void getLeftStatus(MotionProfileStatus status) {
    	 mLeftMaster.getMotionProfileStatus(status);
    }
    
    public void setMotionProfileStatus(int status) { //0,1,2
		motionProfileMode = status;
	}
}
