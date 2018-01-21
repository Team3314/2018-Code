package org.usfirst.frc.team3314.robot.motion;

import java.util.List;

import org.usfirst.frc.team3314.robot.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;

import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Notifier;

public class PathFollower {
	
	private Drive drive = Drive.getInstance();
	private boolean pathFinished = false;
	private double updateRate = 1/50;
	private long waitTime = (long) (updateRate * 1000.0);
	private Thread thread = null;
	private MotionProfileStatus leftStatus = new MotionProfileStatus(), rightStatus = new MotionProfileStatus();
	
	class PeriodicRunnable implements java.lang.Runnable {
		@Override
		public void run() {
			drive.processMotionProfilePoints();
		}
	}
	Notifier notifier = new Notifier(new PeriodicRunnable());
	
	public void start() {
		pathFinished = false;
		drive.currentDriveMode = driveMode.MOTION_PROFILE;
		drive.setMotionProfileStatus(1);
		notifier.startPeriodic(0.002);
	}
	
	public void stop() {
		drive.setMotionProfileStatus(0);
		notifier.stop();
	}
	
	public boolean isDone() {
		return pathFinished;
	}
	
	public void checkDone() { 
		drive.getLeftStatus(leftStatus);
		drive.getRightStatus(rightStatus);
		if(leftStatus.isLast == true && rightStatus.isLast == true) {
			pathFinished = true;
		}
	}
}
