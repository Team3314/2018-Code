package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Constants {
	
	public static int kDriveEncoderCodesPerRev = 8192;
	public static double kDegToTicksConvFactor = .038888888888888;
	public static double kRevToInConvFactor = 7.90274223082;
	
	//PIDF Values for Motion Profile 
		public static double kMotionProfile_kP = .05;
		public static double kMotionProfile_kI = 0;
		public static double kMotionProfile_kD = 0;
		public static double kMotionProfile_kF = .1204;
		public static int kMotionProfileSlot = 0;
	
	//PIDF values for gyro
		public static double kGyroLock_kP = .04;
		public static double kGyroLock_kI = 0;//.002;
		public static double kGyroLock_kD = 0;//0.1;
		public static double kGyroLock_kF = 0;
		public static int kGyroLockSlot = 1;
		
		public static double kMotionProfileGyro_kP = .025;
		
		//pidf for vision control
		public static double kVisionCtrl_kP = .04;
		public static double kVisionCtrl_kI = 0;
		public static double kVisionCtrl_kD = 0;
		public static double kVisionCtrl_kF = 0;
		public static int kVisionCtrlSlot = 2;
		
		//Arm PIDF
		public static double kArm_kP = 5;
		public static double kArm_kI = 0;
		public static double kArm_kD = 400;
		public static double kArm_kF = 6.42;
		
		//Arm Values
		public static double kArmTicksToAngle = 0.087890625; 
		public static int kMaxArmAngularVelocity = (int) (140 / kArmTicksToAngle / 10); //radians/sec
		public static int kMaxArmAcceleceration = (int)(70 / kArmTicksToAngle / 10); // radians/sec/sec
		public static int kArmEncoderOffset = -3134; //Comp robot : -3134 Practice : -1411
		public static int kArmMinPosition = -765;
		public static int kMaxArmPosition = 1162;
		public static double kArmTargetThreshold = 2 / kArmTicksToAngle;
		
		//Telescope PIDF
		public static double kTelescope_kP = 2;
		public static double kTelescope_kI = 0;
		public static double kTelescope_kD = 40;
		public static double kTelescope_kF = 3.41;
		
		//Telescope Characterisitcs
		public static double kTelescopeTicksToInches =0.005126953125; 
		public static int kMaxTelescopeVelocity = (int)(18 / kTelescopeTicksToInches / 10); // inches/sec
		public static int kMaxTelescopeAcceleration = (int)(30 / kTelescopeTicksToInches / 10); // inches/sec/sec
		public static int kTelescopeMinPosition = 0;
		public static int kTelescopeEncoderOffset = -2801; //Comp robot : -2058 Practice : TODO this shit fucked
		public static int kMaxTelescopePosition = (int)(19 / kTelescopeTicksToInches); 
		public static double kTelescopeTargetThreshold = 3 / kTelescopeTicksToInches;
		
		//Gears
		public static Value kHighGear = Value.kForward ;
		public static Value kLowGear = Value.kReverse;
		
		//PTO
		public static Value kPTOIn = Value.kForward;
		public static Value kPTOOut = Value.kReverse;
		
		//camera
		public static double kLEDOn = 0.0;
		public static double kLEDOff = 1.0;
		public static double kLEDBlink = 2.0;
		public static double kVisionProcessorMode = 0.0;
		public static double kDriverCameraMode = 1.0;
		
		public static double kTrackingHeight = 11-4; //cube - camera
		
		//Robot characteristics
		public static double kPulleyDiameter = 3.75; //inches
		public static double kMaxVelocity = 15.0; //fps
		public static double kWheelbaseWidth = 27.5 / 12; // inches to feet
		
		public static int kDrivetrainAcceleration = 0;
		public static int kDrivetrainCruiseVelocity = 0;
		public static double kDistanceCOR = 7.1; //inches; from front of camera
		public static double kRadiusCOR = 12.5; //inches
		
		//Arm Motion Profile
		public static int kArmMotionControlFramePeriod = 5; //5 ms
		public static int kArmMotionControlTrajectroyPeriod = 10; //10 ms
		
		//Drive Motion Profile
		public static int kDriveMotionControlFramePeriod = 2;// 2ms
		public static int kDriveMotionControlTrajectoryPeriod = 5; //5ms;
		public static double kFeetToEncoderCodes = (12 *kDriveEncoderCodesPerRev) / kRevToInConvFactor;
		
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
		public static double kScaleLowAngle = 100 / kArmTicksToAngle;; //100
		public static double kScaleLowTelescopePosition = 0;
		public static int kScaleHighAngle =  (int)(100 / kArmTicksToAngle);
		public static double kScaleHighTelescopePosition = 12 / kTelescopeTicksToInches;
		public static double kHoldAngle = -65 / kArmTicksToAngle;
		public static double kHoldTelescopePosition = 0;
		public static double kSwitchAngle = 0;
		public static double kSwitchTelescopePosition = 0;
		public static double kClimbRaisedAngle = 90 / kArmTicksToAngle; // 100
		public static double kClimbRaisedTelescopePosition = kMaxTelescopePosition;
		public static double kBarAngle = 60 / kArmTicksToAngle;
		public static double kBarTelescopePosition = kMaxTelescopePosition;
		
}
