package org.usfirst.frc.team3314.robot.motion;

import org.usfirst.frc.team3314.robot.subsystems.Arm;

import com.ctre.phoenix.motion.TrajectoryPoint;

public class MotionProfileFollower {
	
	private Arm arm = Arm.getInstance();
	
	private double lastArmV= 0, lastTelescopeV = 0, lastArmP = 0, lastTelescopeP = 0;
	
	private double armVSetpoint = 0, telescopeVSetpoint = 0;
	
	private double armPSetpoint = 0, telescopePSetpoint = 0;
	
	private double targetArmAngle = 0, targetArmVelocity = 0;
	private double targetTelescopePosition = 0, targetTelescopeVelocity = 0;
	private TrajectoryPoint telescopePoint = new TrajectoryPoint(), armPoint = new TrajectoryPoint();
	
	private double armStopTime, telescopeStopTime;
	
	private double stopAngle = 0,  stopTelescopePosition = 0;
	
	public void updateData() {
		
		
	}
	
	public TrajectoryPoint calcNextArm() {
		
		//Calculations go here
		
		armPoint.position = armPSetpoint;
		armPoint.velocity = armVSetpoint;
		return armPoint;
	}
	public TrajectoryPoint calcNextTelescope() {
		
		
		//calculations go here
		telescopePoint.position = armPSetpoint;
		telescopePoint.velocity = armVSetpoint;
		return telescopePoint;
	}
	
	public void moveTo(double armP, double armV, double teleP, double teleV) {
		targetArmAngle = armP;
		targetArmVelocity = armV;
		targetTelescopePosition = teleP;
		targetTelescopeVelocity = teleV;
	}
	public void moveAt(double armV, double teleV) {
		targetArmVelocity = armV;
		targetTelescopeVelocity = teleV;
	}

}
