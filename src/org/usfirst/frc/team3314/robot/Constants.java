package org.usfirst.frc.team3314.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Constants {
	
	//PID values for gyro
		public static double kGyroLock_kP = .04;
		public static double kGyroLock_kI = .002;
		public static double kGyroLock_kD = .1;
		public static double kGyroLock_kF = 0;
		
		//Gears
		public static Value kHighGear = Value.kReverse;
		public static Value kLowGear = Value.kForward;
		
		public static double kPulleyDiameter = 3.75; //inches
		
		public static double kMaxVelocity = 15.0; //fps
		
		public static double kWheelbaseWidth = 27.5; // inches
		
		public static double kMotionProfileGyro_kP = .025;
		
		public static double kRevToInConvFactor = 7.90274223082;
		public static int kDriveEncoderCodesPerRev = 8192;

}
