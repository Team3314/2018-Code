package org.usfirst.frc.team3314.robot.subsystems;

import java.util.List;
import java.util.ArrayList;
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
	
	private double targetsInView, targetHorizOffset, averageTargetHoriz, targetVertOffset, 
		targetArea, targetSkew, targetLatency;
	private List<Double> targetHorizList = new ArrayList<Double>(20);
	private double ledMode, camMode;
	private String camString, ledString;
	
	private double rawDistance, adjustedDistance;
	private double areaLin, areaExp, areaQuad, trigLin, trigExp, trigQuad;
	private double linearHorizOffset, thetaCOR, arcLengthCOR, arcTicks;
	
	private double steeringAdjust;
	private boolean trackingRequest = false;
	
	public void update() {
		setLEDMode(Constants.kLEDOff);
		
		targetsInView = limelight.getEntry("tv").getDouble(0);

		targetHorizOffset = limelight.getEntry("tx").getDouble(-1337.254);
		if (targetHorizList.size() >= 20) {
			targetHorizList.remove(0);
		}
		targetHorizList.add(targetHorizOffset);
		averageTargetHoriz = targetHorizList.stream().mapToDouble(val->val).average().getAsDouble();
		
		targetVertOffset = limelight.getEntry("ty").getDouble(-1337.254);
		targetArea = limelight.getEntry("ta").getDouble(0);
		targetSkew = limelight.getEntry("ts").getDouble(0);
		targetLatency = 11 + limelight.getEntry("tl").getDouble(0);
		
		rawDistance = Constants.kTrackingHeight / Math.tan(Math.toRadians(targetVertOffset +
				Constants.kMountingAngle));
		/*
		//FIXME RECALIBRATE SO ROBOT CAN WORK GOODLY
		areaLin = -1.39916 * targetArea + 57.8251;
		areaExp = 67.7383 * Math.pow(0.95467, targetArea);
		areaQuad = (0.0536115 * Math.pow(targetArea, 2)) + (-3.26041 * targetArea) + 69.2658;
		trigLin = 1.23867 * rawDistance - 7.91181;
		trigExp = 12.0369 * Math.pow(1.03033, rawDistance);
		trigQuad = (-0.0127109 * Math.pow(rawDistance, 2)) + (2.23641 * rawDistance) - 26.1831;
		*/
		
		/*
		//FIXME Check+implement once distance's fixed; Get cruise vel/accel for motion magic
		linearHorizOffset = adjustedDistance * Math.tan(Math.toRadians(targetHorizOffset));
		thetaCOR = Math.atan(linearHorizOffset / (adjustedDistance + Constants.kDistanceCOR));
		arcLengthCOR = thetaCOR * Constants.kRadiusCOR;
		arcTicks = arcLengthCOR / Constants.kRevToInConvFactor * Constants.kDriveEncoderCodesPerRev;
		*/
	}
		
	//Getter s
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
	
	public double getAvgError() {
		return averageTargetHoriz;
	}
	
	/*
	//FIXME Empirically determine? Reqs. tons of snapshots so flash limelight on robot
	public double getDistance() {
		if () 	   { adjustedDistance = 60; }
		else if () { adjustedDistance = 59; }
		else if () { adjustedDistance = 58; }
		else if () { adjustedDistance = 57; }
		else if () { adjustedDistance = 56; }
		else if () { adjustedDistance = 55; }
		else if () { adjustedDistance = 54; }
		else if () { adjustedDistance = 53; }
		else if () { adjustedDistance = 52; }
		else if () { adjustedDistance = 51; }
		else if () { adjustedDistance = 50; }
		else if () { adjustedDistance = 49; }
		else if () { adjustedDistance = 48; }
		else if () { adjustedDistance = 47; }
		else if () { adjustedDistance = 46; }
		else if () { adjustedDistance = 45; }
		else if () { adjustedDistance = 44; }
		else if () { adjustedDistance = 43; }
		else if () { adjustedDistance = 42; }
		else if () { adjustedDistance = 41; }
		else if () { adjustedDistance = 40; }
		else if () { adjustedDistance = 39; }
		else if () { adjustedDistance = 38; }
		else if () { adjustedDistance = 37; }
		else if () { adjustedDistance = 36; }
		else if () { adjustedDistance = 35; }
		else if () { adjustedDistance = 34; }
		else if () { adjustedDistance = 33; }
		else if () { adjustedDistance = 32; }
		else if () { adjustedDistance = 31; }
		else if () { adjustedDistance = 30; }
		else if () { adjustedDistance = 29; }
		else if () { adjustedDistance = 28; }
		else if () { adjustedDistance = 27; }
		else if () { adjustedDistance = 26; }
		else if () { adjustedDistance = 25; }
		else if () { adjustedDistance = 24; }
		else if () { adjustedDistance = 23; }
		else if () { adjustedDistance = 22; }
		else if () { adjustedDistance = 21; }
		else if () { adjustedDistance = 20; }
		else if () { adjustedDistance = 19; }
		else	   { adjustedDistance = 18; }

		return adjustedDistance;
		}
		*/
	
	public double getArcLength() {
		return arcLengthCOR;
	}
	
	public double getArcTicks() {
		return arcTicks;
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
			ledString = "B5LINK";
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
		
		SmartDashboard.putNumber("Avg target horiz", getAvgError());
		SmartDashboard.putNumber("1", targetHorizList.get(0));
		SmartDashboard.putNumber("2", targetHorizList.get(1));
		SmartDashboard.putNumber("3", targetHorizList.get(2));
		SmartDashboard.putNumber("4", targetHorizList.get(3));
		SmartDashboard.putNumber("5", targetHorizList.get(4));
		SmartDashboard.putNumber("6", targetHorizList.get(5));
		SmartDashboard.putNumber("7", targetHorizList.get(6));
		SmartDashboard.putNumber("8", targetHorizList.get(7));
		SmartDashboard.putNumber("9", targetHorizList.get(8));
		SmartDashboard.putNumber("10", targetHorizList.get(9));
		SmartDashboard.putNumber("11", targetHorizList.get(10));
		SmartDashboard.putNumber("12", targetHorizList.get(11));
		SmartDashboard.putNumber("13", targetHorizList.get(12));
		SmartDashboard.putNumber("14", targetHorizList.get(13));
		SmartDashboard.putNumber("15", targetHorizList.get(14));
		SmartDashboard.putNumber("16", targetHorizList.get(15));
		SmartDashboard.putNumber("17", targetHorizList.get(16));
		SmartDashboard.putNumber("18", targetHorizList.get(17));
		SmartDashboard.putNumber("19", targetHorizList.get(18));
		SmartDashboard.putNumber("20", targetHorizList.get(19));

		SmartDashboard.putNumber("Raw distance", rawDistance);
		//SmartDashboard.putNumber("Adjusted distance", getDistance());
		SmartDashboard.putNumber("Trig lin", trigLin);
		SmartDashboard.putNumber("Trig exp", trigExp);
		SmartDashboard.putNumber("Area lin", areaLin);
		SmartDashboard.putNumber("Area exp", areaExp);
		SmartDashboard.putNumber("Trig quad", trigQuad);
		SmartDashboard.putNumber("Area quad", areaQuad);
		
		SmartDashboard.putNumber("Linear horiz offset", linearHorizOffset);
		SmartDashboard.putNumber("Theta from COR", thetaCOR);
		SmartDashboard.putNumber("Arc length from COR", getArcLength());
		
		SmartDashboard.putNumber("Steering adjust", getSteeringAdjust());
		SmartDashboard.putBoolean("Tracking request", getTrackingRequest());
	}
}