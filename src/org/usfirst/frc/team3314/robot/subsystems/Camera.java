package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;
import edu.wpi.first.networktables.*;
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
	private double trigLin, trigExp, areaLin, areaExp;
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
		targetLatency = 11 + limelight.getEntry("tl").getDouble(0);
		
		rawDistance = Constants.kTrackingHeight / Math.tan(Math.toRadians(targetVertOffset +
				Constants.kMountingAngle));

		trigLin = -1.5905 * targetArea + 58.7728;
		trigExp = 69.5539 * Math.pow(0.949044, targetArea);
		areaLin = 1.239 * rawDistance - 8.09891;
		areaExp = 12.1321 * Math.pow(1.03003, rawDistance);
		
		if (targetArea > 15.5 || targetArea < 6.5) {
			adjustedDistance = areaLin;
		} else {
			adjustedDistance = trigExp;
		}
		
		//TODO Make sure calculations work
		linearHorizOffset = /*adjustedDistance * */ Math.tan(Math.toRadians(targetHorizOffset));
		thetaCOR = Math.atan(linearHorizOffset / (rawDistance + Constants.kDistanceCOR));
		arcLengthCOR = thetaCOR * Constants.kRadiusCOR;
	}
		
	//Getters
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
		
	//Setters
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
		SmartDashboard.putNumber("Adjusted distance", getDistance());
		SmartDashboard.putNumber("Trig lin", trigLin);
		SmartDashboard.putNumber("Trig exp", trigExp);
		SmartDashboard.putNumber("Area lin", areaLin);
		SmartDashboard.putNumber("Area exp", areaExp);
		SmartDashboard.putNumber("Linear horiz offset", linearHorizOffset);
		SmartDashboard.putNumber("Theta from COR", thetaCOR);
		SmartDashboard.putNumber("Arc length from COR", getArcLength());
		
		SmartDashboard.putNumber("Steering adjust", getSteeringAdjust());
		SmartDashboard.putBoolean("Tracking request", getTrackingRequest());
	}	
}