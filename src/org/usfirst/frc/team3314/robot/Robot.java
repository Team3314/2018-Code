package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
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

	Compressor pcm1 = new Compressor();
	
	Autonomous selectedAutoMode = null;
	
	private boolean lastGyrolock = false, lastScaleHigh, lastScaleLow, lastPickup, lastHold, lastStop, lastClimb;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {	
		Log.startServer(1099);
		camera.start();
	}
	
	@Override
	public void robotPeriodic() {
		outputToSmartDashboard();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void disabledInit() {
		pathFollower.stop();
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
		drive.logger.createNewFile("Auto");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		allPeriodic();
		selectedAutoMode.update();
	}


	@Override
	public void teleopInit() {
		drive.logger.createNewFile("Teleop");
		pathFollower.stop();
		drive.resetSensors();
		arm.startUp();
		drive.flushTalonBuffer();
		camera.setLEDMode(1);
	}
	
	/**
	 * This function is called periodically during operator control
	 */
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
				drive.setDesiredAngle(drive.getAngle());
			}
			drive.setDesiredSpeed(hi.getLeftThrottle());
		}
		else {
			drive.setDriveMode(driveMode.OPEN_LOOP);
		}
		
		if (hi.getVisionCtrl()) {
			camera.setTrackingRequest(true);
		} else {
			camera.setTrackingRequest(false);
			drive.setDriveMode(driveMode.OPEN_LOOP);
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
		if(hi.getScaleHigh() && !lastScaleHigh) {
			arm.setDesiredState(ArmState.TO_SCALE_HIGH);
		}
		else if(hi.getScaleLow() && !lastScaleLow) {
			arm.setDesiredState(ArmState.TO_SCALE_LOW);
		}
		else if(hi.getPickup() && !lastPickup) {
			arm.setDesiredState(ArmState.TO_PICKUP);
		}
		else if(hi.getHold() && !lastHold) {
			arm.setDesiredState(ArmState.TO_HOLDING);
		}
		else if(hi.getClimb() && !lastClimb) {
			arm.setDesiredState(ArmState.TO_CLIMB);
		}
		else if(hi.getStop() && !lastStop) {
			arm.setDesiredState(ArmState.STOP);
		}
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
	}

	public void allPeriodic() {
		drive.update();
		arm.update();
		intake.update();
		//camera.update();
		//tracking.update();
	}
	
	public void outputToSmartDashboard() {
		arm.outputToSmartDashboard();
		drive.outputToSmartDashboard();
		intake.outputToSmartDashboard();
		camera.outputToSmartDashboard();
	}
	
	public void testInit() {
		camera.setLEDMode(1);
	}
}
