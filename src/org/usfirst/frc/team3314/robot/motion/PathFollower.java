package org.usfirst.frc.team3314.robot.motion;

import java.util.List;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.paths.Path;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;

import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import com.cruzsbrian.robolog.Log;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Notifier;

public class PathFollower {
	
	private Drive drive = Drive.getInstance();
	private CSVParser parser = new CSVParser();
	private boolean pathFinished = false;
	private boolean running = false;
	private MotionProfileStatus leftStatus = new MotionProfileStatus(), rightStatus = new MotionProfileStatus();
	
	class PeriodicRunnable implements java.lang.Runnable {
		@Override
		public void run() {
			if(!running) {
				notifier.stop();
			}
			drive.processMotionProfilePoints();
		}
	}
	Notifier notifier = new Notifier(new PeriodicRunnable());
	
	public void start() {
		pathFinished = false;
		drive.setDriveMode(driveMode.MOTION_PROFILE);
		drive.setMotionProfileStatus(1);
		notifier.startPeriodic(0.005);
		running = true;
	}
	
	public void stop() {
		drive.setMotionProfileStatus(0);
		running = false;
	}
	
	public void loadPoints(Path path) {
		parser.start(path);
	}
	
	public boolean isDone() {
		drive.getLeftStatus(leftStatus);
		drive.getRightStatus(rightStatus);
		Log.add("Left Count", (double)leftStatus.btmBufferCnt);
		Log.add("Right Count",(double) rightStatus.btmBufferCnt);
		if(leftStatus.isLast == true && rightStatus.isLast == true) {
			stop();
			pathFinished = true;
		}
		return pathFinished;
	}

}
