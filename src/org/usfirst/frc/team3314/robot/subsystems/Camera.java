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
	private Notifier notifier = new Notifier(new PeriodicRunnable());
	
	private double targetsInView, targetHorizOffset, targetVertOffset, targetArea, targetSkew, targetLatency;
	private double ledMode, camMode;
	
	//TODO find way to differentiate from height of 11 and 13
	private double cubeHeight = 11, cameraHeight = 4;
	private double rawDistance, adjustedDistance;
	private double linearHorizOffset, thetaCOR, arcLengthCOR;
	private double steeringAdjust;
	private boolean trackingRequest = false;
	
	public class PeriodicRunnable implements java.lang.Runnable {
		public void run() {
			targetsInView = limelight.getEntry("tv").getDouble(0);
			targetHorizOffset = limelight.getEntry("tx").getDouble(0);
			targetVertOffset = limelight.getEntry("ty").getDouble(0);
			targetArea = limelight.getEntry("ta").getDouble(0);
			targetSkew = limelight.getEntry("ts").getDouble(0);
			targetLatency = limelight.getEntry("tl").getDouble(0);
			
			rawDistance = ((cubeHeight - cameraHeight) / Math.tan(Math.toRadians(targetVertOffset)));
			//TODO recalibrate adjusted distance
			adjustedDistance = //1.24126 * rawDistance - 2.92415;
							   -1337.254;
			//TODO make sure calcs work
			linearHorizOffset = adjustedDistance * Math.tan(Math.toRadians(targetHorizOffset));
			thetaCOR = Math.atan(linearHorizOffset / (rawDistance + Constants.kDistanceCOR));
			arcLengthCOR = thetaCOR * Constants.kRadiusCOR;
			
			Tracking.getInstance().update();
			outputToSmartDashboard();
		}
	}
	
	public void start() {
		// camera = 90 fps/hz = 1 frame per 11.1 ms = 0.0111 sec
		notifier.startPeriodic(0.0111);
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
			return "OFF";
		} else if (ledMode == Constants.kLEDOn) {
			return "ON";
		} else if (ledMode == Constants.kLEDBlink) {
			return "BLINK";
		} else return null;
	}
	
	public String getCamMode() {
		if (camMode == Constants.kVisionProcessorMode) {
			return "VISION PROCESSOR";
		} else if (camMode == Constants.kDriverCameraMode) {
			return "DRIVER CAMERA";
		} else return null;
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
	
	public void setCamMode(double camMode) {
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
		
		SmartDashboard.putNumber("Cube height", cubeHeight);
		SmartDashboard.putNumber("Raw distance", rawDistance);
		SmartDashboard.putNumber("Linear adjusted distance", getDistance());
		SmartDashboard.putNumber("Linear horiz offset", linearHorizOffset);
		SmartDashboard.putNumber("Theta from COR", thetaCOR);
		SmartDashboard.putNumber("Arc length from COR", getArcLength());
		}	
	}