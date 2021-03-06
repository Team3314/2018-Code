package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Constants {
	
	public static int kDriveEncoderCodesPerRev = 8192;
	public static double kDegToTicksConvFactor = .038888888888888;
	public static double kRevToInConvFactor = 7.90274223082;
	public static double kFeetToEncoderCodes = (12.0 *kDriveEncoderCodesPerRev) / kRevToInConvFactor;
	public static double kFPSToTicksPer100ms = (kFeetToEncoderCodes / 10);
	public static double kVoltageToNativeTalonUnits = 1023.0/12.0;
	
	public static boolean practiceBot = false;
	
	public static int kCANTimeout = 0;
	
	public static int kDriveContinuousCurrentLimit = 40;
	public static int kDrivePeakCurrentLimit = 40;
	public static int kDrivePeakCurrentDuration = 0;
	public static double kDriveDeadband = .1;
	public static double kDriveOpenLoopRampRate = .2;
	public static double kDriveVoltageScale = 12.0;
	public static double kDriveClosedLoopRampTime = 0;
	public static double kMaxSpeed = 14; //fps
	
	
	//PIDF Values for Motion Profile 
		public static double kMotionProfile_kP = .11;// max motor output when error is 8 inches
		public static double kMotionProfile_kI = 0;
		public static double kMotionProfile_kD = 0;
		public static double kMotionProfile_kF = .042; 
		public static int kMotionProfileSlot = 0;
		
		//Left Fore High
				public static double kMotionProfileLeftForeHigh_kV = 0.647188; 
				public static double kMotionProfileLeftForeHigh_kA = 0.107643;  
				public static double kMotionProfileLeftForeHigh_Intercept = 1.202158 ;
				public static double kMotionProfileLeftForeHigh_kF = kMotionProfileLeftForeHigh_kV  / kFPSToTicksPer100ms * kVoltageToNativeTalonUnits;
				//Right Fore High
				public static double kMotionProfileRightForeHigh_kV = 0.636061;
				public static double kMotionProfileRightForeHigh_kA = 0.109528;
				public static double kMotionProfileRightForeHigh_Intercept = 1.222074;
				public static double kMotionProfileRightForeHigh_kF = kMotionProfileRightForeHigh_kV   / kFPSToTicksPer100ms * kVoltageToNativeTalonUnits;
				//Left Back High
				public static double kMotionProfileLeftBackHigh_kV= 0.579970;
				public static double kMotionProfileLeftBackHigh_kA = 0;//0.103432;
				public static double kMotionProfileLeftBackHigh_Intercept =  -1.480870;
				public static double kMotionProfileLeftBackHigh_kF = kMotionProfileLeftBackHigh_kV  / kFPSToTicksPer100ms * kVoltageToNativeTalonUnits;;
				//Right Back High
				public static double kMotionProfileRightBackHigh_kV  = 0.609598;
				public static double kMotionProfileRightBackHigh_kA =  0;//0.132630;
				public static double kMotionProfileRightBackHigh_Intercept = -1.418709;
				public static double kMotionProfileRightBackHigh_kF = kMotionProfileRightBackHigh_kV  / kFPSToTicksPer100ms * kVoltageToNativeTalonUnits;
				//Left Fore Low
				public static double kMotionProfileLeftForeLow_kV =1.866947;
				public static double kMotionProfileLeftForeLow_kA = 0.093998;
				public static double kMotionProfileLeftForeLow_Intercept = 0.761862;
				public static double kMotionProfileLeftForeLow_kF = kMotionProfileLeftForeHigh_kV   / kFPSToTicksPer100ms  * kVoltageToNativeTalonUnits;;
				//Right Fore Low
				public static double kMotionProfileRightForeLow_kV =  1.881882;
				public static double kMotionProfileRightForeLow_kA = 0.145920;
				public static double kMotionProfileRightForeLow_Intercept =0.709919;
				public static double kMotionProfileRightForeLow_kF = kMotionProfileRightForeLow_kV   / kFPSToTicksPer100ms  * kVoltageToNativeTalonUnits;;
				//Left Back Low
				public static double kMotionProfileLeftBackLow_kV =1.851484;
				public static double kMotionProfileLeftBackLow_kA = 0.145081;
				public static double kMotionProfileLeftBackLow_Intercept =-0.727548;
				public static double kMotionProfileLeftBackLow_kF = kMotionProfileLeftBackLow_kV  / kFPSToTicksPer100ms * kVoltageToNativeTalonUnits;
				//Right Back Low
				public static double kMotionProfileRightBackLow_kV = 1.861267;
				public static double kMotionProfileRightBackLow_kA =0.123069;
				public static double kMotionProfileRightBackLow_Intercept = -0.725794;
				public static double kMotionProfileRightBackLow_kF = kMotionProfileRightBackLow_kV  / kFPSToTicksPer100ms * kVoltageToNativeTalonUnits;
		
		//PIDF values for motion profile heading following
		public static double kMotionProfileHeading_kP =  .025;
		public static double kMotionProfileHeading_kI = 0;
		public static double kMotionProfileHeading_kD =0;
		public static double kMotionProfileHeading_kF = 0;
		public static double kMotionProfileHeading_kA = 0;
		
		public static double kVelocity_kP = .1;
		public static double kVelocity_kI = 0;
		public static double kVelocity_kD = 0;
		public static double kVelocity_kF = .043;
		public static int kVelocitySlot = 1;
		
	
	//PIDF values for gyro
		public static double kGyroLock_kP = .025;
		public static double kGyroLock_kI = 0;
		public static double kGyroLock_kD = 0;
		public static double kGyroLock_kF = 0;
		public static int kGyroLockSlot = 2;
		
		//pidf for vision control
		public static double kVisionCtrl_kP = .01;
		public static double kVisionCtrl_kI = 0;
		public static double kVisionCtrl_kD = 0;
		public static double kVisionCtrl_kF = 0;
		public static int kVisionCtrlSlot = 3;
		
		//Arm PIDFf
		public static double kArm_kP = 6;
		public static double kArm_kI = 0;
		public static double kArm_kD = 400;
		public static double kArm_kF = 6.42;
		
		//Arm Values
		public static double kArmTicksToAngle = 0.087890625; 
		public static int kMaxArmAngularVelocity = (int) (140 / kArmTicksToAngle / 10); //radians/sec
		public static int kMaxArmAcceleceration = (int)(200 / kArmTicksToAngle / 10); // radians/sec/sec
		public static int kArmEncoderOffset =  -1411; //Comp robot : -778 Practice : -1411
		public static int kArmMinPosition = -765;
		public static int kMaxArmPosition = 1162;
		public static double kArmTargetThreshold = 3/ kArmTicksToAngle;
		public static int kArmContinuousCurrentLimit = 20;
		public static int kArmPeakCurrentLimit = 30;
		public static int kArmPeakCurrentDuration = 500;
		
		//Telescope PIDF
		public static double kTelescope_kP = 2;
		public static double kTelescope_kI = 0;
		public static double kTelescope_kD = 200;
		public static double kTelescope_kF = 3.41;
		
		//Telescope Characterisitcs
		public static double kTelescopeTicksToInches =0.005126953125; 
		public static int kMaxTelescopeVelocity = (int)(20 / kTelescopeTicksToInches / 10); // inches/sec
		public static int kMaxTelescopeAcceleration = (int)(80 / kTelescopeTicksToInches / 10); // inches/sec/sec
		public static int kTelescopeMinPosition = 0;
		public static int kTelescopeEncoderOffset = 980; //Comp robot : 4030 Practice : 980
		public static int kMaxTelescopePosition = (int)(19 / kTelescopeTicksToInches); 
		public static double kTelescopeTargetThreshold = 3 / kTelescopeTicksToInches;
		public static int kTelescopeContinuousCurrentLimit = 20;
		public static int kTelescopePeakCurrentLimit = 30;
		public static int kTelescopePeakCurrentDuration = 500;
		
		//Gears
		public static Value kHighGear = Value.kForward ;
		public static Value kLowGear = Value.kReverse;
		
		//PTO
		public static Value kPTOIn = Value.kForward;
		public static Value kPTOOut = Value.kReverse;
		
		//Ramp Piston
		public static boolean kRampPistonIn = true;
		public static boolean kRampPistonOut =  false;
		
		//Intake Piston
		public static Value kIntakePistonIn = Value.kForward;
		public static Value kIntakePistonOut = Value.kReverse;
		
		//camera
		public static int kLEDOn = 0;
		public static int kLEDOff = 1;
		public static int kLEDBlink = 2;
		public static int kVisionProcessorMode = 0;
		public static int kDriverCameraMode = 1;
		public static int kSnapshotOff = 0;
		public static int kSnapshotOn = 1;
		
		public static double kTrackingHeight = 11-4; //cube - camera
		public static double kMountingAngle = 10; //degrees
		
		public static double kMinMotorCmd = 0.175;//0-1
		public static double kMaxTrackingRPM = 0;
		
		//Robot characteristics
		public static double kPulleyDiameter = 2.51552; //inches
		public static double kMaxVelocity = 15.0; //fps
		public static double kWheelbaseWidth = 27.5 / 12; // inches to feet
		
		public static int kDrivetrainAcceleration = (int)(12 * Constants.kFPSToTicksPer100ms);
		public static int kDrivetrainCruiseVelocity = 0;
		public static double kDistanceCOR = 7.1; //inches; from front of camera
		public static double kRadiusCOR = 12.5; //inches
		
		//Arm Motion Profile
		public static int kArmMotionControlFramePeriod = 5; //5 ms
		public static int kArmMotionControlTrajectroyPeriod = 10; //10 ms
		
		//Drive Motion Profile
		public static int kDriveMotionControlFramePeriod = 5;// 5ms
		public static int kDriveMotionControlTrajectoryPeriod = 10; //10ms;
		
		
		//Intake
		public static int kIntakeCurrentLimit = 15;
		public static int kIntakeStallThreshold = 9;
		public static int kIntakePeakCurrentLimit = 15;
		public static int kIntakePeakCurrentDuration = 100;
		
		//Arm Characteristics
		public static double kIntakeLength = 11.75; //inches
		public static double kArmToIntakeAngle = Math.toRadians(51.132); // degrees to radians
		public static double kIntakeAdditionToLength = kIntakeLength * Math.cos(kArmToIntakeAngle);
		public static double kShortSideOfArm = 9.25;
		public static double kMinArmLength = 30.125; // inches
		public static double kMaxArmXPosition = 42.875; // inches
		public static double kPivotPointHeight = 42.557; //inches
		
		//Arm positions (inches)
		public static double kPickUpAngle = -50 / kArmTicksToAngle;
		public static double kPickUpTelescopePosition  = 14.5 / kTelescopeTicksToInches;
		public static double kScaleLowAngle = 92 / kArmTicksToAngle;; //100
		public static double kScaleLowTelescopePosition = 0;
		public static int kScaleHighAngle =  (int)(92 / kArmTicksToAngle);
		public static double kScaleHighTelescopePosition = 12 / kTelescopeTicksToInches;
		public static double kHoldAngle = -63 / kArmTicksToAngle;
		public static double kHoldTelescopePosition = 0;
		public static double kSwitchAngle = -20 / Constants.kArmTicksToAngle;
		public static double kSwitchTelescopePosition = 0;
		public static double kClimbRaisedAngle = 90 / kArmTicksToAngle; // 100
		public static double kClimbRaisedTelescopePosition = kMaxTelescopePosition;
		public static double kBarAngle = 60 / kArmTicksToAngle;
		public static double kBarTelescopePosition = kMaxTelescopePosition;
		public static double raiseCubeAngle = -40 / kArmTicksToAngle;
		public static double kScaleAutoTelescopePosition = 6 / kTelescopeTicksToInches;
		public static double kScaleAutoArmAngle = 90 / kArmTicksToAngle;
		public static double kFlipCubeAngle = -50 / kArmTicksToAngle;
		public static double kFlipCubeTelescopePosition =8 / kTelescopeTicksToInches;
		
}
