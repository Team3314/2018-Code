package org.usfirst.frc.team3314.robot.subsystems;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera {

	private static Camera mInstance = new Camera();
	
	public static Camera getInstance() {
		return mInstance;
	}
	
	private NetworkTableInstance networktables = NetworkTableInstance.getDefault();
	private NetworkTable limelight = networktables.getTable("limelight");
	
	private double targetsInView, targetHorizOffset, targetVertOffset, targetArea, targetSkew;
	
	public double steeringAdjust;
	public boolean trackingRequest = false;
	
	public void update() {
		// TODO Auto-generated method stub
		targetsInView = limelight.getEntry("tv").getDouble(0);
		targetHorizOffset = limelight.getEntry("tx").getDouble(0);
		targetVertOffset = limelight.getEntry("ty").getDouble(0);
		targetArea = limelight.getEntry("ta").getDouble(0);
		targetSkew = limelight.getEntry("ts").getDouble(0);
		
		outputToSmartDashboard();
	}
		
	public boolean isTargetInView() {
		if (targetsInView == 1.0) {
			return true;
		} else {
			return false;
		}
	}
	
	public double getError() {
		return targetHorizOffset;
	}
		
	public void setSteeringAdjust(double adjust) {
		steeringAdjust = adjust;
	}
	
	/*
	public void setLEDMode(int ledMode) {
		limelight.getEntry("ledMode").setDouble(ledMode);
	}
	
	public void setCamMode(double camMode) {
		limelight.getEntry("camMode").setDouble(camMode);
	}
	*/
	
	public void outputToSmartDashboard() {
		// TODO Auto-generated method stub
		SmartDashboard.putBoolean("Any targets in view?", isTargetInView());
		SmartDashboard.putNumber("Target horiz offset", targetHorizOffset);
		SmartDashboard.putNumber("Target vert offset", targetVertOffset);
		SmartDashboard.putNumber("Target area", targetArea);
		SmartDashboard.putNumber("Target skew", targetSkew);
		}	
	}