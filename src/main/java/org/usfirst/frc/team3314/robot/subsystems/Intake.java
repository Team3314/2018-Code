package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
	
	public enum IntakeState {
		HOLDING,
		INTAKING,
		INTAKE_SLOW,
		UNJAMMING,
		RELEASING,
		RELEASE_SLOW,	
		RELEASE_REALLY_SLOW,
		OVERRIDE
	}

	private static Intake mInstance = new Intake();
	
	private WPI_TalonSRX mRollerLeft, mRollerRight;
	private DigitalInput cubeSensor;
	private DoubleSolenoid intakeRotator;
	private double leftSpeed, rightSpeed;
	private int stallTimer = 0;
	private Timer timer = new Timer();
	
	private boolean override = false, intakeExtended = false;
	
	private IntakeState currentState = IntakeState.HOLDING;
	
	public static Intake getInstance() {
		return mInstance;
	}
	
	public void update() {
		if(intakeExtended)
			intakeRotator.set(Constants.kIntakePistonOut);
		else 
			intakeRotator.set(Constants.kIntakePistonIn);
 		checkStallCurrent();
		switch(currentState) {
			case HOLDING:
				setDesiredSpeed(0);
				timer.stop();
				timer.reset();
				break;
			case INTAKING:
				setDesiredSpeed(1);
				if(!override) {
					if(senseCube() && !motorsStalled()) {
						setDesiredSpeed(0);
						currentState = IntakeState.HOLDING;
					}
					if(senseCube() && motorsStalled()) {
						currentState = IntakeState.HOLDING;
					}
					else if(!senseCube() && motorsStalled()) {
						startTimer();
						currentState = IntakeState.UNJAMMING;
					}
				}
				break;
			case INTAKE_SLOW:
				setDesiredSpeed(.5);
				if(!override) {
					if(senseCube() && !motorsStalled()) {
						setDesiredSpeed(0);
						currentState = IntakeState.HOLDING;
					}
					if(senseCube() && motorsStalled()) {
						currentState = IntakeState.HOLDING;
					}
					else if(!senseCube() && motorsStalled()) {
						startTimer();
						currentState = IntakeState.UNJAMMING;
					}
				}
				break;
			case UNJAMMING:
				leftSpeed = 0;//-.25;
				rightSpeed = 1;
				if(!override) {
					if(getTime() >= .3) {
						currentState = IntakeState.INTAKING;
						timer.stop();
						timer.reset();
					}
				}
				break;
			case RELEASING:
				setDesiredSpeed(-1);
				break;
			case RELEASE_SLOW:
				setDesiredSpeed(-.5);
				break;
			case RELEASE_REALLY_SLOW:
				setDesiredSpeed(-.2);
				break;
			case OVERRIDE:
				break;
		}
		mRollerLeft.set(ControlMode.PercentOutput, leftSpeed);
		mRollerRight.set(ControlMode.PercentOutput, rightSpeed);
	}
	
	private Intake() {
		mRollerLeft = new WPI_TalonSRX(4); 
		mRollerLeft.configContinuousCurrentLimit(Constants.kIntakeCurrentLimit, 0);
		mRollerLeft.configPeakCurrentLimit(Constants.kIntakePeakCurrentLimit, 0);
		mRollerLeft.configPeakCurrentDuration(Constants.kIntakePeakCurrentDuration, 0);
		mRollerLeft.enableCurrentLimit(true);
		mRollerLeft.setInverted(false);
		mRollerLeft.configPeakOutputForward(1, 0);
		mRollerLeft.configPeakOutputReverse(-1, 0);
		mRollerLeft.configForwardSoftLimitEnable(false, 0);
		mRollerLeft.configReverseSoftLimitEnable(false, 0);
		mRollerLeft.configVoltageCompSaturation(12.0, 0);
		mRollerLeft.enableVoltageCompensation(true);
		
		mRollerRight = new WPI_TalonSRX(5);
		mRollerRight.configContinuousCurrentLimit(Constants.kIntakeCurrentLimit, 0);
		mRollerRight.configPeakCurrentLimit(Constants.kIntakePeakCurrentLimit, 0);
		mRollerRight.configPeakCurrentDuration(Constants.kIntakePeakCurrentDuration, 0);
		mRollerRight.enableCurrentLimit(true);
		mRollerRight.setInverted(true);
		mRollerRight.configPeakOutputForward(1, 0);
		mRollerRight.configPeakOutputReverse(-1, 0);
		mRollerRight.configForwardSoftLimitEnable(false, 0);
		mRollerRight.configReverseSoftLimitEnable(false, 0);
		mRollerRight.configVoltageCompSaturation(12.0, 0);
		mRollerRight.enableVoltageCompensation(true);
		
		cubeSensor = new DigitalInput(4);
		
		intakeRotator = new DoubleSolenoid(4, 5);
	}
	
	public void setDesiredSpeed(double speed) {
		leftSpeed = speed;
		rightSpeed = speed;
	}
	
	public void setOverride(boolean override) {
		this.override = override;
	}
	
	public boolean senseCube() {
		return !cubeSensor.get();
	}
	
	public void setDesiredState(IntakeState desiredState) {
		currentState = desiredState;
	}
	
	public void checkStallCurrent() {
		if( (mRollerRight.getOutputCurrent() > Constants.kIntakeStallThreshold && mRollerLeft.getOutputCurrent() > Constants.kIntakeStallThreshold)) {
			stallTimer++;
		}
		else {
			stallTimer = 0;
		}
	}
	
	public boolean motorsStalled() {
		if(stallTimer >= 5) {
			stallTimer = 0;
			return true;
		}
		return false;
	}
	
	public IntakeState getState() {
		return currentState;
	}
	
	public void setExtendIntake(boolean extend) {
		intakeExtended = extend;
	}
	
	public void stopTimer() {
		timer.reset();
	}
	public void startTimer() {
		timer.start();
	}
	public double getTime() {
		return timer.get();
	}
	
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Intake Roller Left Current", mRollerLeft.getOutputCurrent());
		SmartDashboard.putNumber("Intake Roller Right Current", mRollerRight.getOutputCurrent());
		SmartDashboard.putString("Intake state", currentState.toString());
		SmartDashboard.putNumber("Intake Left Speed", leftSpeed);
		SmartDashboard.putNumber("Intake Right Speed", rightSpeed);
		SmartDashboard.putNumber("Intake Timer", timer.get());
		SmartDashboard.putBoolean("Cube sensor", senseCube());
	}
}
