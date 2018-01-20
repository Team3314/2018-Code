package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3314.robot.autos.AutoModeExecuter;
import org.usfirst.frc.team3314.robot.subsystems.*;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

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
	private Camera camera = Camera.getInstance();
	private Tracking tracking = Tracking.getInstance();
	private HumanInput hi = HumanInput.getInstance();
	
	private AutoModeExecuter autoExecuter = new AutoModeExecuter();
	private AutoModeSelector selector = new AutoModeSelector();

	Compressor pcm1 = new Compressor();
	
	private boolean lastGyrolock = false;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {	
		drive.resetSensors();
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
		autoExecuter.stop();
	}
	
	public void disabledPeriodic() {
		selector.pollFMS();
	}
	
	@Override
	public void autonomousInit() {
		autoExecuter.setAutoMode(selector.getSelectedAutoMode());
		autoExecuter.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		drive.update();
		intake.update();
		camera.update();
		tracking.update();
	}

	
	@Override
	public void teleopInit() {
		drive.logger.createNewFile();
		drive.resetSensors();
		autoExecuter.stop();
	}
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		drive.update();
		intake.update();
		camera.update();
		tracking.update();
		
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
			camera.trackingRequest = true;
		}
		
		if(hi.getHighGear()) {
			drive.setHighGear(true);
		}
		else if(hi.getLowGear()) {
			drive.setHighGear(false);
		}
		
		if(hi.getFullSpeedForward()) {
			drive.setDesiredSpeed(1);
		}
		
		if(hi.getIntake()) {
			intake.setDesiredSpeed(1);
		}
		
		if(hi.getOuttake()) {
			intake.setDesiredSpeed(-1);
		}
		
		drive.setStickInputs(hi.getLeftThrottle(), hi.getRightThrottle());
		lastGyrolock = hi.getGyrolock();
		
	}
}
