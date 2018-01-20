package org.usfirst.frc.team3314.robot.subsystems;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.DataLogger;
import org.usfirst.frc.team3314.robot.GyroPIDOutput;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Drive implements Subsystem {
	
	public enum driveMode {
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
	public driveMode currentDriveMode = driveMode.OPEN_LOOP;
	ControlMode controlMode = ControlMode.PercentOutput;
	
	//Hardware
	private WPI_TalonSRX mLeftMaster, mLeftSlave1, mLeftSlave2, mRightMaster, mRightSlave1, mRightSlave2;
	private DoubleSolenoid shifter;
	private PowerDistributionPanel pdp;
	private AHRS navx;
	
	//Hardware states
	private boolean mIsHighGear;
    private boolean mIsBrakeMode;
    
    //Data Logging
    public DataLogger logger;
    
    public Camera camera;
    
    //PID
	private GyroPIDOutput gyroPIDOutput;
	private PIDController gyroControl;

    private double rawLeftSpeed, rawRightSpeed, leftStickInput, rightStickInput, desiredLeftSpeed, desiredRightSpeed, desiredAngle;
    
    private int leftDrivePositionTicks, rightDrivePositionTicks, leftDriveSpeedTicks, rightDriveSpeedTicks;
    
    private double leftDrivePositionInches, rightDrivePositionInches, leftDriveSpeedRPM, rightDriveSpeedRPM;
    
    public void update() {
    	if(mIsHighGear) {
    		shifter.set(Constants.kHighGear);
    	}
    	else {
    		shifter.set(Constants.kLowGear);
    	}
    	outputToSmartDashboard();
    	updateSpeedAndPosition();
    	logSpeed();
    	switch(currentDriveMode) {
    		case OPEN_LOOP:
    			rawLeftSpeed = leftStickInput;
    			rawRightSpeed = rightStickInput;
    			controlMode = ControlMode.PercentOutput;
    			break;
    		case GYROLOCK:
    			if (!gyroControl.isEnabled()){
    				gyroControl.enable();
    			}
    			
    			rawLeftSpeed = desiredLeftSpeed + gyroPIDOutput.turnSpeed;
    			rawRightSpeed = desiredRightSpeed - gyroPIDOutput.turnSpeed;
    			gyroControl.setSetpoint(desiredAngle);
    			controlMode = ControlMode.PercentOutput;
    			break;
    		case VISION_CONTROL:
    			rawLeftSpeed = leftStickInput + camera.steeringAdjust;
    			rawRightSpeed = leftStickInput - camera.steeringAdjust;
    			controlMode = ControlMode.PercentOutput;
    			break;
    		case MOTION_PROFILE:
    			break;
    	}
    	mLeftMaster.set(controlMode, rawLeftSpeed);
    	mRightMaster.set(controlMode, rawRightSpeed);
    
    }

	

    private Drive() {
    	// Logger
    	 logger = DataLogger.getInstance();
    	 
    	 camera = Camera.getInstance();
    	
		//Hardware
    	pdp  = new PowerDistributionPanel();
    	shifter = new DoubleSolenoid(2, 3);
    	navx = new AHRS(SPI.Port.kMXP);
    	
    	//Gyro PID
    	gyroPIDOutput = new GyroPIDOutput();
    	gyroControl = new PIDController(Constants.kGyroLock_kP, Constants.kGyroLock_kI, Constants.kGyroLock_kD,
    		Constants.kGyroLock_kF, navx, gyroPIDOutput);
		//Sets the PID controller to treat 180 and -180 to be the same point, 
		//so that when turning the robot takes the shortest path instead of going the long way around
		//Effectively changes PID input from a line to a circle
	    gyroControl.setContinuous(); 
		gyroControl.setInputRange(-180, 180);
		gyroControl.setOutputRange(-.7, .7);		// Limits speed of turn to prevent overshoot
		gyroControl.setAbsoluteTolerance(1);
    	
		
		//Talons
    	mLeftMaster = new WPI_TalonSRX(0);
    	//mLeftMaster.enable
    	mLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    	mLeftMaster.setInverted(true);
    	mLeftMaster.setSensorPhase(true);
    	
    	mLeftSlave1 = new WPI_TalonSRX(1);
    	mLeftSlave1.follow(mLeftMaster);
    	mLeftSlave1.setInverted(true);
    	
    	mLeftSlave2 = new WPI_TalonSRX(2);
    	mLeftSlave2.follow(mLeftMaster);
    	mLeftSlave2.setInverted(true);
    	
    	mRightMaster = new WPI_TalonSRX(3);
    	mRightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    	mRightMaster.setSensorPhase(true);

    	
    	mRightSlave1 = new WPI_TalonSRX(4);
    	mRightSlave1.follow(mRightMaster);
    	
    	mRightSlave2 = new WPI_TalonSRX(5);
    	mRightSlave2.follow(mRightMaster);
    	
    	resetSensors();
    	
    	mIsHighGear = false;
    }
    
    public void setStickInputs(double leftInput, double rightInput) {
    	leftStickInput = -leftInput;
    	rightStickInput = -rightInput;
    }
    
    public void setDesiredAngle(double angle) {
    	desiredAngle = angle;
    }
    
    public double getAngle() {
    	return navx.getYaw();
    }
    
    public boolean checkTolerance() {
    	return gyroControl.onTarget();
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
    
    public void setDesiredSpeed(double speed) {
    	desiredLeftSpeed = speed;
    	desiredRightSpeed = speed;
    }
    
    public void setDesiredSpeed(double leftSpeed, double rightSpeed) {
    	rawLeftSpeed = leftSpeed; //makes sure 
    	rawRightSpeed = rightSpeed;
    }
    
    public void setDriveMode(driveMode mode) {
    	currentDriveMode = mode;
    }
    
    public void setHighGear(boolean highGear) {
    	mIsHighGear = highGear;
    }
    
    private void logSpeed() {
    	String[] names = {"Left Encoder Speed", "Right Encoder Speed", "Left Encoder Position", "Right Encoder Position", "NavX Y Acceleration", "NavX X Acceleration",
    			"Left Master Current", "Left Slave 1 Current","Left Slave 2 Current", "Right Master Current", "Right Slave 1 Current", "Right Slave 2 Current",
    			"Left Master Voltage", "Left Slave 1 Voltage","Left Slave 2 Voltage", "Right Master Voltage", "Right Slave 1 Voltage", "Right Slave 2 Voltage",
    			"Battery Voltage", "High Gear"};
    	String[] values = {String.valueOf(mLeftMaster.getSelectedSensorVelocity(0)), String.valueOf(mRightMaster.getSelectedSensorVelocity(0)), String.valueOf(leftDrivePositionInches), String.valueOf(rightDrivePositionInches),
    			String.valueOf(navx.getWorldLinearAccelY()), String.valueOf(navx.getWorldLinearAccelX()), String.valueOf(mLeftMaster.getOutputCurrent()),
    			String.valueOf(mLeftSlave1.getOutputCurrent()), String.valueOf(mLeftSlave2.getOutputCurrent()), String.valueOf(mRightMaster.getOutputCurrent()),
    			String.valueOf(mRightSlave1.getOutputCurrent()), String.valueOf(mRightSlave2.getOutputCurrent()), String.valueOf(mLeftMaster.getMotorOutputVoltage()),
    			String.valueOf(mLeftSlave1.getMotorOutputVoltage()),String.valueOf(mLeftSlave2.getMotorOutputVoltage()), String.valueOf(mRightMaster.getMotorOutputVoltage()),
    			String.valueOf(mRightSlave1.getMotorOutputVoltage()), String.valueOf(mRightSlave2.getMotorOutputVoltage()),String.valueOf(pdp.getVoltage()), String.valueOf(mIsHighGear)};
    	logger.logData(names, values);

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
		mLeftMaster.setSelectedSensorPosition(0, 0, 0);
		mRightMaster.setSelectedSensorPosition(0, 0, 0);
	}
    
    public void resetSensors() {
    	navx.reset();
    	resetDriveEncoders();
    }
	
}
