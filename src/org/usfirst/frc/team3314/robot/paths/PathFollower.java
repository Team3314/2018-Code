package org.usfirst.frc.team3314.robot.paths;

import org.usfirst.frc.team3314.robot.Constants;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;
import org.usfirst.frc.team3314.robot.subsystems.Drive;

public class PathFollower {
	
	private Drive drive = Drive.getInstance();
	private EncoderFollower left, right;
	private boolean pathFinished = false;
	private boolean hasRun = false;
	
	public void followPath(Path path) {
		if(!hasRun) {
			left = path.getLeftEncoderFollower();
			right = path.getRightEncoderFollower();
	   		left.configureEncoder(drive.getLeftPositionTicks(), Constants.kDriveEncoderCodesPerRev , Constants.kPulleyDiameter / 12);
			right.configureEncoder(drive.getRightPositionTicks(), Constants.kDriveEncoderCodesPerRev , Constants.kPulleyDiameter / 12);
			left.configurePIDVA(1.0, 0.0, 0.0, 1 / Constants.kMaxVelocity, 0);
			hasRun = true;
		}
	    double leftSpeed = left.calculate(drive.getLeftPositionTicks());
		double rightSpeed = right.calculate(drive.getRightPositionTicks());
		double gyroHeading = drive.getAngle();
		double desiredHeading = Pathfinder.r2d(left.getHeading());
		double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
		double turn = Constants.kMotionProfileGyro_kP  * angleDifference;
		drive.setDesiredSpeed(leftSpeed - turn, rightSpeed + turn);
		pathFinished = left.isFinished() && right.isFinished();
		if(pathFinished) {
			reset();
		}
	}
	
	public boolean isDone() { 
		return pathFinished;
	}
	
	public void reset() {
		hasRun = false;
	}

}
