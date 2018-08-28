package org.usfirst.frc.team3314.robot.motion;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.paths.Path;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;
import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;

import com.cruzsbrian.robolog.Log;

import edu.wpi.first.wpilibj.Notifier;

public class PathFollower {
	
	private Drive drive = Drive.getInstance();
	private EncoderFollower left, right;
	private double leftVIntercept, rightVIntercept, lastHeading = 0, lastHeadingChange = 0, lastHeadingError = 0;
	private int direction;
	private boolean pathFinished = false;
	
	class PeriodicRunnable implements java.lang.Runnable {
		@Override
		public void run() {
		    double leftSpeed = left.calculate(drive.getLeftPositionTicks() * direction) * direction + leftVIntercept;
			double rightSpeed = right.calculate(drive.getRightPositionTicks() * direction) * direction + rightVIntercept;
			double gyroHeading = -drive.getAngle();
			double desiredHeading = Pathfinder.r2d(left.getHeading());
			double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
			double headingErrorChange = Pathfinder.boundHalfDegrees(angleDifference -  lastHeadingError);
			double angleSetpointChange = Pathfinder.boundHalfDegrees(desiredHeading - lastHeading);
			double angleAcceleration = angleSetpointChange - lastHeadingChange;
			double headingF = angleSetpointChange * Constants.kMotionProfileHeading_kF;
			double headingA = angleAcceleration * Constants.kMotionProfileHeading_kA;
			double turn = Constants.kMotionProfileHeading_kP  * angleDifference;
			double headingD = Constants.kMotionProfileHeading_kD * (headingErrorChange/.01) ;
			leftSpeed = leftSpeed - turn - headingF - headingA - headingD;
			rightSpeed = rightSpeed + turn + headingF + headingA + headingD;
			leftSpeed *= Constants.kMaxSpeed * Constants.kFPSToTicksPer100ms;
			rightSpeed *=Constants.kMaxSpeed * Constants.kFPSToTicksPer100ms;
			drive.setDesiredSpeed(leftSpeed, rightSpeed);
			lastHeading = desiredHeading;
			lastHeadingChange = angleSetpointChange;
			lastHeadingError = angleDifference;
			pathFinished = left.isFinished() && right.isFinished();
			if(pathFinished) {
				left.reset();
				right.reset();
				drive.setDriveMode(driveMode.IDLE);
				notifier.stop();
			}
		}
	}
	
	private Notifier notifier = new Notifier(new PeriodicRunnable());
	
	public void followPath(Path path) {
		drive.resetSensors();
		switch(path.getMode()) {
		case BACKWARD_HIGH:
			left = new EncoderFollower(Pathfinder.readFromCSV(path.getRightPath()));
			right = new EncoderFollower(Pathfinder.readFromCSV(path.getLeftPath()));
			left.configurePIDVA(Constants.kMotionProfile_kP, Constants.kMotionProfile_kI, Constants.kMotionProfile_kD, Constants.kMotionProfileLeftBackHigh_kV / 12, Constants.kMotionProfileLeftBackHigh_kA / 12);
			right.configurePIDVA(Constants.kMotionProfile_kP, Constants.kMotionProfile_kI, Constants.kMotionProfile_kD, Constants.kMotionProfileRightBackHigh_kV / 12, Constants.kMotionProfileRightBackHigh_kA / 12);
			leftVIntercept = Constants.kMotionProfileLeftBackHigh_Intercept / 12;
			rightVIntercept = Constants.kMotionProfileRightBackHigh_Intercept / 12;
			direction = -1;
			break;
		case BACKWARD_LOW:
			left = new EncoderFollower(Pathfinder.readFromCSV(path.getRightPath()));
			right = new EncoderFollower(Pathfinder.readFromCSV(path.getLeftPath()));
			left.configurePIDVA(Constants.kMotionProfile_kP, Constants.kMotionProfile_kI, Constants.kMotionProfile_kD, Constants.kMotionProfileLeftBackLow_kV / 12, Constants.kMotionProfileLeftBackLow_kA / 12);
			right.configurePIDVA(Constants.kMotionProfile_kP, Constants.kMotionProfile_kI, Constants.kMotionProfile_kD, Constants.kMotionProfileRightBackLow_kV / 12, Constants.kMotionProfileRightBackLow_kA / 12);
			leftVIntercept = Constants.kMotionProfileLeftBackLow_Intercept / 12;
			rightVIntercept = Constants.kMotionProfileRightBackLow_Intercept / 12;
			direction = -1;
			break;
		case FORWARD_HIGH:
			left = new EncoderFollower(Pathfinder.readFromCSV(path.getLeftPath()));
			right = new EncoderFollower(Pathfinder.readFromCSV(path.getRightPath()));
			left.configurePIDVA(Constants.kMotionProfile_kP, Constants.kMotionProfile_kI, Constants.kMotionProfile_kD, Constants.kMotionProfileLeftForeHigh_kV / 12, Constants.kMotionProfileLeftForeHigh_kA / 12);
			right.configurePIDVA(Constants.kMotionProfile_kP, Constants.kMotionProfile_kI, Constants.kMotionProfile_kD, Constants.kMotionProfileRightForeHigh_kV / 12, Constants.kMotionProfileRightForeHigh_kA / 12);
			leftVIntercept = Constants.kMotionProfileLeftForeHigh_Intercept / 12;
			rightVIntercept = Constants.kMotionProfileRightForeHigh_Intercept / 12;
			direction = 1;
			break;
		case FORWARD_LOW:
			left = new EncoderFollower(Pathfinder.readFromCSV(path.getLeftPath()));
			right = new EncoderFollower(Pathfinder.readFromCSV(path.getRightPath()));
			left.configurePIDVA(Constants.kMotionProfile_kP, Constants.kMotionProfile_kI, Constants.kMotionProfile_kD, Constants.kMotionProfileLeftForeLow_kV / 12, Constants.kMotionProfileLeftForeLow_kA / 12);
			right.configurePIDVA(Constants.kMotionProfile_kP, Constants.kMotionProfile_kI, Constants.kMotionProfile_kD, Constants.kMotionProfileRightForeLow_kV / 12, Constants.kMotionProfileRightForeLow_kA / 12);
			leftVIntercept = Constants.kMotionProfileLeftForeLow_Intercept / 12;
			rightVIntercept = Constants.kMotionProfileRightForeLow_Intercept / 12;
			direction = 1;
			break;
		}
   		left.configureEncoder(drive.getLeftPositionTicks(), Constants.kDriveEncoderCodesPerRev , Constants.kPulleyDiameter / 12);
		right.configureEncoder(drive.getRightPositionTicks(), Constants.kDriveEncoderCodesPerRev , Constants.kPulleyDiameter / 12);
		pathFinished = false;
		
		drive.setDesiredSpeed(0);
		drive.setDriveMode(driveMode.MOTION_PROFILE);
		notifier.startPeriodic(0.01);
	}
	
	public boolean isDone() { 
		return pathFinished;
	}
	
	public void stop() {
		left = null;
		right = null;
		pathFinished = true;
		drive.setDriveMode(driveMode.IDLE);
	}
	
	public void log() {
		if(left != null && right != null && !left.isFinished() && !right.isFinished()) {
			Log.add("Left Position Setpoint", left.getSegment().position * direction);
			Log.add("Right Position Setpoint", right.getSegment().position * direction);
			Log.add("Left Heading Setpoint ", Pathfinder.boundHalfDegrees(Pathfinder.r2d(-left.getHeading())));
			Log.add("Left Velocity Setpoint", left.getSegment().velocity * direction);
			Log.add("Right Velocity Setpoint", right.getSegment().velocity * direction);
		}
	}

}