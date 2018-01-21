package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3314.robot.autos.AutoModeExecuter;
import org.usfirst.frc.team3314.robot.autos.Autonomous;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

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
	private HumanInput hi = HumanInput.getInstance();
	//private AutoModeExecuter autoExecuter = new AutoModeExecuter();
	private AutoModeSelector selector = new AutoModeSelector();
	private PathFollower pathFollower = new PathFollower();

	Compressor pcm1 = new Compressor();
	
	Autonomous selectedAutoMode = null;
	
	private boolean lastGyrolock = false;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {	
		Log.startServer(1099);
		drive.resetSensors();
	}
	
	@Override
	public void robotPeriodic() {
		//drive.update();
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
		drive.setDriveMode(driveMode.IDLE);
		//autoExecuter.stop();
	}
	
	@Override
	public void disabledPeriodic() {
		//drive.flushTalonBuffer();
	}
	
	@Override
	public void autonomousInit() {
		drive.flushTalonBuffer();
		drive.setDriveMode(driveMode.IDLE);
		selectedAutoMode = selector.getSelectedAutoMode();
		drive.logger.createNewFile("Auto");
		//autoExecuter.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		drive.update();
		selectedAutoMode.update();
	}

	
	@Override
	public void teleopInit() {
		drive.logger.createNewFile("Teleop");
		pathFollower.stop();
		drive.resetSensors();
		drive.flushTalonBuffer();
	}
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		drive.update();
		if(hi.getGyrolock()) {
			if(!lastGyrolock) {
				drive.setDriveMode(driveMode.GYROLOCK);
			}
			drive.setDesiredSpeed(hi.getLeftThrottle());
		}
		else {
			drive.setDriveMode(driveMode.OPEN_LOOP);
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
		SmartDashboard.putBoolean("Gyrolock", hi.getGyrolock());
		lastGyrolock = hi.getGyrolock();
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}
