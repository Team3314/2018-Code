package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3314.robot.autos.Autonomous;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
import org.usfirst.frc.team3314.robot.subsystems.*;
import org.usfirst.frc.team3314.robot.subsystems.Arm.ArmState;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;
import org.usfirst.frc.team3314.robot.subsystems.Intake.IntakeState;
import com.cruzsbrian.robolog.Log;

//import com.ctre.*;
//import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */


public class Robot extends IterativeRobot {
	
	private Drive drive = Drive.getInstance();
	private Intake intake = Intake.getInstance();
	private Arm arm = Arm.getInstance();
	
	private Camera camera = Camera.getInstance();
	private HumanInput hi = HumanInput.getInstance();
	
	private AutoModeSelector selector = new AutoModeSelector();
	private PathFollower pathFollower = new PathFollower();
	private Timer timer = new Timer();
	
	Autonomous selectedAutoMode = null;
	
	private boolean lastGyrolock = false, lastScaleHigh, lastScaleLow, lastPickup, lastHold, lastStop, lastClimb, lastBar;
	
	@Override
	public void robotInit() {	
		Log.startServer(1099);
		camera.start();
	}
	
	@Override
	public void robotPeriodic() {
		outputToSmartDashboard();
	}
	@Override
	public void disabledInit() {
		pathFollower.stop();
		
		camera.setLEDMode(Constants.kLEDOff);
		camera.setCamMode(Constants.kVisionProcessorMode);
	}
	
	@Override
	public void disabledPeriodic() {
	}
	
	@Override
	public void autonomousInit() {
		drive.flushTalonBuffer();
		drive.setDriveMode(driveMode.IDLE);
		drive.resetSensors();
		arm.startUp();
		selectedAutoMode = selector.getSelectedAutoMode();
		drive.newFile("DriveAuto");
		arm.newFile("ArmAuto");
		timer.start();
		
		camera.setLEDMode(Constants.kLEDOff);
		camera.setCamMode(Constants.kVisionProcessorMode);
	}

	@Override
	public void autonomousPeriodic() {
		if(selector.getGameData().length() == 0 && timer.get() < 5) {
			selectedAutoMode = selector.getSelectedAutoMode();
		}
		else {
			allPeriodic();
			selectedAutoMode.update();
			timer.stop();
		}
	}


	@Override
	public void teleopInit() {
		pathFollower.stop();
		drive.resetSensors();
		arm.startUp();
		drive.flushTalonBuffer();
		
		camera.setTrackingRequest(false);
		camera.setLEDMode(Constants.kLEDOff);
		camera.setCamMode(Constants.kVisionProcessorMode);
		drive.newFile("DriveTele");
		arm.newFile("ArmTele");
	}
	
	@Override
	public void teleopPeriodic() {
		allPeriodic();
		
		// Intake Controls
		intake.setOverride(hi.getIntakeOverride());
		if(hi.getIntakePressed()) {
			intake.setDesiredState(IntakeState.INTAKING);
		}
		else if(hi.getOuttake()) {
			intake.setDesiredState(IntakeState.RELEASING);
		}
		else if(hi.getUnjamPressed()) {
			intake.setDesiredState(IntakeState.UNJAMMING);
		}
		else if(!hi.getIntake() && !hi.getUnjam() && !hi.getOuttake()) {
			intake.setDesiredState(IntakeState.HOLDING);
		}

		
		// Drive Controls
		if(hi.getGyrolock()) {
			if(!lastGyrolock) {
				drive.setDriveMode(driveMode.GYROLOCK);
			}
			drive.setDesiredSpeed(hi.getLeftThrottle());
		}
		else if(!hi.getGyrolock()) {
			drive.setDriveMode(driveMode.OPEN_LOOP);
		}
		
		if (hi.getVisionCtrl()) {
			camera.setTrackingRequest(true);
		} else {
			camera.setTrackingRequest(false);
			//drive.setDriveMode(driveMode.OPEN_LOOP);
		}
		
		if(hi.getHighGear()) {
			drive.setHighGear(true);
		}
		else if(hi.getLowGear()) {
			drive.setHighGear(false);
		}	
		if(hi.getPTO()) {
			drive.setPTO(!drive.getPTO());
		}
		
		//Arm Controls
		if(hi.getClimb() && !lastClimb) {
				arm.setDesiredState(ArmState.TO_CLIMB);
		}
		else if(hi.getBar() && !lastBar) {
			arm.setDesiredState(ArmState.LOWER_TO_BAR);
		}
		else if(hi.getScaleHigh() && !lastScaleHigh && !hi.getClimb()) {
			arm.setDesiredState(ArmState.TO_SCALE_HIGH);
		}
		else if(hi.getScaleLow() && !lastScaleLow && !hi.getClimb()) {
			arm.setDesiredState(ArmState.TO_SCALE_LOW);
		}
		else if(hi.getPickup() && !lastPickup && !hi.getClimb()) {
			arm.setDesiredState(ArmState.TO_PICKUP);
		}
		else if(hi.getHold() && !lastHold && !hi.getClimb()) {
			arm.setDesiredState(ArmState.TO_HOLDING);
		}
		else if(hi.getStop() && !lastStop && !hi.getClimb()) {
			arm.setDesiredState(ArmState.STOP);
		}
		
		arm.setArmOverride(hi.armPowerOverride());
		arm.setArmOverrideSpeed(hi.getArmOverrideSpeed());
		
		arm.setTelescopeOverride(hi.telescopePowerOverride());
		arm.setTelescopeOverrideSpeed(hi.getTelescopeOverrideSpeed());
		
		arm.setTargetSpeed(hi.getArmSpeed());
		drive.setStickInputs(hi.getLeftThrottle(), hi.getRightThrottle());
		SmartDashboard.putBoolean("Gyrolock", hi.getGyrolock());
		lastGyrolock = hi.getGyrolock();
		lastScaleHigh = hi.getScaleHigh();
		lastScaleLow = hi.getScaleLow();
		lastPickup = hi.getPickup();
		lastHold = hi.getHold();
		lastStop = hi.getStop();
		lastClimb = hi.getClimb();
		lastBar = hi.getBar();
	}

	public void allPeriodic() {
		drive.update();
		arm.update();
		intake.update();
	}
	
	public void outputToSmartDashboard() {
		arm.outputToSmartDashboard();
		drive.outputToSmartDashboard();
		intake.outputToSmartDashboard();
		camera.outputToSmartDashboard();
	}
	
	public void testInit() {
		camera.setLEDMode(Constants.kLEDOff);
		camera.setCamMode(Constants.kVisionProcessorMode);
	}
}
