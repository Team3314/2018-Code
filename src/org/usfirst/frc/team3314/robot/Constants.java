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
		public static double kVisionCtrl_kP = 0;
		public static double kVisionCtrl_kI = 0;
		public static double kVisionCtrl_kD = 0;
		public static double kVisionCtrl_kF = 0;
		public static int kVisionCtrlSlot = 2;
		
		//Arm PIDF
		public static double kArm_kP = 0;
		public static double kArm_kI = 0;
		public static double kArm_kD = 0;
		public static double kArm_kF = 0;
		
		//Telescope PIDF
		public static double kTelescope_kP = 0;
		public static double kTelescope_kI = 0;
		public static double kTelescope_kD = 0;
		public static double kTelescope_kF = 0;
		
		//Gears
		public static Value kHighGear = Value.kReverse;
		public static Value kLowGear = Value.kForward;
		
		//Robot characteristics
		public static double kPulleyDiameter = 3.75; //inches
		public static double kMaxVelocity = 15.0; //fps
		public static double kWheelbaseWidth = 27.5 / 12; // inches to feet
		
		public static int kDrivetrainAcceleration = 0;
		public static int kDrivetrainCruiseVelocity = 0;
		public static double kDistanceCOR = 0; //inches; from front of camera
		public static double kRadiusCOR = 0;
		
		//Arm Motion Profile
		public static int kArmMotionControlFramePeriod = 5; //5 ms
		public static int kArmMotionControlTrajectroyPeriod = 10; //10 ms
		
		//Drive Motion Profile
		public static int kDriveMotionControlFramePeriod = 2;// 2ms
		public static int kDriveMotionControlTrajectoryPeriod = 5; //5ms;
		public static double kFeetToEncoderCodes = (12 *kDriveEncoderCodesPerRev) / kRevToInConvFactor;
		
		//Intake
		public static int kIntakeCurrentLimit = 15;//20;
		public static int kIntakeStallThreshold = 9;
		public static int kIntakePeakCurrentLimit = 15;//15;
		public static int kIntakePeakCurrentDuration = 100;
		
}
