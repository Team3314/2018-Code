package org.usfirst.frc.team3314.robot;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.*;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Drive {
	
	public enum driveMode {
		OPEN_LOOP,
		GYROLOCK
	}
	
	private static Drive mInstance = new Drive();
	
	public static Drive getInstance() {
		return mInstance;
	}

	
	//Control mode
	public driveMode currentDriveMode = driveMode.OPEN_LOOP;
	
	//Hardware
	private WPI_TalonSRX mLeftMaster, mLeftSlave1, mLeftSlave2, mRightMaster, mRightSlave1, mRightSlave2;
	private DoubleSolenoid shifter;
	private PowerDistributionPanel pdp;
	private AHRS navx;
	
	
	//Hardware states
	private boolean mIsHighGear;
    private boolean mIsBrakeMode;
    
    //Data Logging
    DataLogger logger;

    //PID
	private GyroPIDOutput gyroPIDOutput;
	private PIDController gyroControl;

    private double rawLeftSpeed, rawRightSpeed, leftStickInput, rightStickInput, desiredSpeed, desiredAngle;
    
    public void update() {
    	if(mIsHighGear) {
    		shifter.set(Constants.kHighGear);
    	}
    	else {
    		shifter.set(Constants.kLowGear);
    	}
    	outputToSmartDashboard();
    	logSpeed();
    	mLeftMaster.set(rawLeftSpeed);
    	mRightMaster.set(rawRightSpeed);
    	switch(currentDriveMode) {
    		case OPEN_LOOP:
    			rawLeftSpeed = leftStickInput;
    			rawRightSpeed = rightStickInput;
    			return;
    		case GYROLOCK:
    			if (!gyroControl.isEnabled()){
    				gyroControl.enable();
    			}
    			
    			rawLeftSpeed = desiredSpeed + gyroPIDOutput.turnSpeed;
    			rawRightSpeed = desiredSpeed - gyroPIDOutput.turnSpeed;
    			gyroControl.setSetpoint(desiredAngle);	
    			return;
    	}
    
    }

	

    private Drive() {
    	// Logger
    	 logger = DataLogger.getInstance();
    	
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
    	
    	mLeftSlave2 = new WPI_TalonSRX(2);
    	mLeftSlave2.follow(mLeftMaster);
    	
    	mRightMaster = new WPI_TalonSRX(3);
    	mRightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

    	
    	mRightSlave1 = new WPI_TalonSRX(4);
    	mRightSlave1.follow(mLeftMaster);
    	
    	mRightSlave2 = new WPI_TalonSRX(5);
    	mRightSlave2.follow(mLeftMaster);
    	
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
    
    public void setDesiredSpeed(double speed) {
    	desiredSpeed = speed;
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
    	String[] values = {String.valueOf(mLeftMaster.getSelectedSensorVelocity(0)), String.valueOf(mRightMaster.getSelectedSensorVelocity(0)), String.valueOf(mLeftMaster.getSelectedSensorPosition(0)), String.valueOf(mRightMaster.getSelectedSensorPosition(0)),
    			String.valueOf(navx.getWorldLinearAccelY()), String.valueOf(navx.getWorldLinearAccelX()), String.valueOf(mLeftMaster.getOutputCurrent()),
    			String.valueOf(mLeftSlave1.getOutputCurrent()), String.valueOf(mLeftSlave2.getOutputCurrent()), String.valueOf(mRightMaster.getOutputCurrent()),
    			String.valueOf(mRightSlave1.getOutputCurrent()), String.valueOf(mRightSlave2.getOutputCurrent()), String.valueOf(mLeftMaster.getMotorOutputVoltage()),
    			String.valueOf(mLeftSlave1.getMotorOutputVoltage()),String.valueOf(mLeftSlave2.getMotorOutputVoltage()), String.valueOf(mRightMaster.getMotorOutputVoltage()),
    			String.valueOf(mRightSlave1.getMotorOutputVoltage()), String.valueOf(mRightSlave2.getMotorOutputVoltage()),String.valueOf(pdp.getVoltage()), String.valueOf(mIsHighGear)};
    	logger.logData(names, values);

    }
    
    private void outputToSmartDashboard() {
    	SmartDashboard.putNumber("Left Encoder Position", mLeftMaster.getSelectedSensorPosition(0));
    	SmartDashboard.putNumber("Right Encoder Position", mRightMaster.getSelectedSensorPosition(0));
    	SmartDashboard.putNumber("Left Encoder Speed", mLeftMaster.getSelectedSensorVelocity(0));
    	SmartDashboard.putNumber("Right Encoder Speed", mRightMaster.getSelectedSensorVelocity(0));
    	SmartDashboard.putNumber("Left Master Current", mLeftMaster.getOutputCurrent());
    	SmartDashboard.putNumber("Left Slave 1 Current", mLeftSlave1.getOutputCurrent());
    	SmartDashboard.putNumber("Left Slave 2 Current", mLeftSlave2.getOutputCurrent());
    	SmartDashboard.putNumber("Right Master Current", mRightMaster.getOutputCurrent());
    	SmartDashboard.putNumber("Right Slave 1 Current", mRightSlave1.getOutputCurrent());
    	SmartDashboard.putNumber("Right Slave 2 Current", mRightSlave2.getOutputCurrent());
    	SmartDashboard.putString("Drive Mode", String.valueOf(currentDriveMode));
    	
    }
    
    private void resetDriveEncoders() {
		mLeftMaster.setSelectedSensorPosition(0, 0, 0);
		mRightMaster.setSelectedSensorPosition(0, 0, 0);
	}
    
    public void resetSensors() {
    	navx.reset();
    	resetDriveEncoders();
    }
	
}
