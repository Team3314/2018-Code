package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.Tracking;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera {
	private static Camera mInstance = new Camera();
	
	public static Camera getInstance() {
		return mInstance;
	}
	
	private NetworkTableInstance networktables = NetworkTableInstance.getDefault();
	private NetworkTable limelight = networktables.getTable("limelight");
	
	private double targetsInView, targetHorizOffset, targetVertOffset, targetArea, targetSkew, targetLatency;
	private double ledMode, camMode;
	private String camString, ledString;
	
	private double rawDistance, adjustedDistance;
	private double linearHorizOffset, thetaCOR, arcLengthCOR;
	
	private double steeringAdjust;
	private boolean trackingRequest = false;
	
	public void update() {
		targetsInView = limelight.getEntry("tv").getDouble(0);
		//TODO Find way to get average tx and ty using an array of the latest 10 values
		targetHorizOffset = limelight.getEntry("tx").getDouble(-1337.254);
		targetVertOffset = limelight.getEntry("ty").getDouble(-1337.254);
		targetArea = limelight.getEntry("ta").getDouble(0);
		targetSkew = limelight.getEntry("ts").getDouble(0);
		targetLatency = 11 + limelight.getEntry("tl").getDouble(11);
		
		rawDistance = Constants.kTrackingHeight / Math.tan(Math.toRadians(targetVertOffset +
				Constants.kMountingAngle));
		//TODO Re-calibrate adjusted distance
		adjustedDistance = //1.24126 * rawDistance - 2.92415;
						   -1337.254;
		
		//TODO Make sure calculations work
		linearHorizOffset = adjustedDistance * Math.tan(Math.toRadians(targetHorizOffset));
		thetaCOR = Math.atan(linearHorizOffset / (rawDistance + Constants.kDistanceCOR));
		arcLengthCOR = thetaCOR * Constants.kRadiusCOR;
	}
		
	//getters
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
	
	public double getDistance() {
		return adjustedDistance;
	}
	
	public double getArcLength() {
		return arcLengthCOR;
	}
	
	public double getSteeringAdjust() {
		return steeringAdjust;
	}
	
	public boolean getTrackingRequest() {
		return trackingRequest;
	}
	
	public String getLEDMode() {
		if (ledMode == Constants.kLEDOff) {
			ledString = "OFF";
		} else if (ledMode == Constants.kLEDOn) {
			ledString = "ON";
		} else if (ledMode == Constants.kLEDBlink) {
			ledString = "BLINK";
		}
		return ledString;
	}
	
	public String getCamMode() {
		if (camMode == Constants.kVisionProcessorMode) {
			camString = "VISION PROCESSOR";
		} else if (camMode == Constants.kDriverCameraMode) {
			camString = "DRIVER CAMERA";
		}
		return camString;
	}
		
	//setters
	public void setSteeringAdjust(double adjust) {
		steeringAdjust = adjust;
	}
	
	public void setTrackingRequest(boolean request) {
		trackingRequest = request;
	}
	
	public void setLEDMode(int ledMode) {
		limelight.getEntry("ledMode").setDouble(ledMode);
	}
	
	public void setCamMode(int camMode) {
		limelight.getEntry("camMode").setDouble(camMode);
	}
	
	public void outputToSmartDashboard() {
		SmartDashboard.putBoolean("Any targets in view?", isTargetInView());
		SmartDashboard.putNumber("Target horiz offset", getError());
		SmartDashboard.putNumber("Target vert offset", targetVertOffset);
		SmartDashboard.putNumber("Target area", targetArea);
		SmartDashboard.putNumber("Target skew", targetSkew);
		SmartDashboard.putNumber("Target latency", targetLatency);
		SmartDashboard.putString("LED mode", getLEDMode());
		SmartDashboard.putString("Camera mode", getCamMode());

		SmartDashboard.putNumber("Raw distance", rawDistance);
		SmartDashboard.putNumber("Linear adjusted distance", getDistance());
		SmartDashboard.putNumber("Linear horiz offset", linearHorizOffset);
		SmartDashboard.putNumber("Theta from COR", thetaCOR);
		SmartDashboard.putNumber("Arc length from COR", getArcLength());
		
		SmartDashboard.putNumber("Steering adjust", getSteeringAdjust());
		SmartDashboard.putBoolean("Tracking request", getTrackingRequest());
	}	
}