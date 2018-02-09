package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import jaci.pathfinder.Trajectory;

public class Constants {
	
	public static int kDriveEncoderCodesPerRev = 8192;
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
		
		//Arm PIDF
		public static double kArm_kP = 0;
		public static double kArm_kI = 0;
		public static double kArm_kD = 0;
		public static double kArm_kF = 0;
		
		//Arm Values
		public static double kMaxArmAngularVelocity = Math.toRadians(60); //radians/sec
		public static double kMaxArmAcceleceration = 0; // radians/sec/sec
		public static double kMaxArmJerk = 0; // radians/sec/sec/sec
		public static double kArmPositionOffset = 0;
		public static double kMaxArmEncoderOffset = 1024;
		
		
		//Telescope PIDF
		public static double kTelescope_kP = 0;
		public static double kTelescope_kI = 0;
		public static double kTelescope_kD = 0;
		public static double kTelescope_kF = 0;
		
		//Telescope Characterisitcs
		public static double kMaxTelescopeVelocity = 18; // inches/sec
		public static double kMaxTelescopeAcceleration = 0; // inches/sec/sec
		public static double kMaxTelescopeJerk = 0; // inches/sec/sec/sec
		public static double kTelescopeEncoderOffset = 0; //inches
		public static double kTelescopeLimitInsideRobot = 40; // inches
		public static double kTelescopeTicksToInches = 0; //XXX PLACEHOLDER
		public static double kMaxTelescopePosition = 1024;
		
		//Gears
		public static Value kHighGear = Value.kReverse;
		public static Value kLowGear = Value.kForward;
		
		//PTO
		public static Value kPTOIn = Value.kForward;
		public static Value kPTOOut = Value.kReverse;
		
		//Robot characteristics
		public static double kPulleyDiameter = 3.75; //inches
		public static double kMaxVelocity = 15.0; //fps
		public static double kWheelbaseWidth = 27.5 / 12; // inches to feet
		
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
		public static double kIntakeLength = 9.0; //inches
		public static double kArmToIntakeAngle = Math.toRadians(51.132); // degrees to radians
		public static double kIntakeAdditionToLength = kIntakeLength * Math.cos(kArmToIntakeAngle);
		public static double kShortSideOfArm = kIntakeLength * Math.cos(kArmToIntakeAngle);
		public static double kMinArmLength = 30; // inches
		public static double kMaxArmXPosition = 42.875; // inches
		public static double kArmToRadiusAngle = Math.toRadians(0); // radians
		public static double kPivotPointHeight = 42.557; //inches
		public static double kArmEncoderOffset = 0; // radians
		
		/* Not needed?
		public static double kDrivePlateHeight = 8 - kPivotPointHeight; // inches
		public static double kDrivePlateX = 29; // inches
		
		public static double kAngleToCornerOfFrame = Math.atan(kDrivePlateHeight/kDrivePlateX);
		public static double kLengthToCornerOfFrame = Math.sqrt(Math.pow(kDrivePlateX, 2) + Math.pow(Constants.kDrivePlateHeight, 2));
		
		public static double kNonAllowableRegionCornerX = (kLengthToCornerOfFrame + kIntakeAdditionToLength) * Math.cos(kAngleToCornerOfFrame + kArmToRadiusAngle);
		public static double kNonAllowableRegionCornerY = (kLengthToCornerOfFrame + kIntakeAdditionToLength) * Math.sin(kAngleToCornerOfFrame + kArmToRadiusAngle);
		*/
		
		public static double kArmInsideRobotAngle = Math.toRadians(-60);
		
		public static double kArmTicksToAngle = 0; //TODO radians
		
		//Arm positions (inches)
		public static double kArmPickUpAngle = 0;
		public static double kArmPickUpTelescopePosition  = 0;
		public static double kArmScaleAngle = 0;
		public static double kArmScaleTelescopePosition = 0;
		public static double kArmSwitchAngle = 0;
		public static double kArmSwitchTelescopePosition = 0;
		public static double kArmHoldAngle = 0;
		public static double kArmHoldTelescopePosition = 0;
		
}
