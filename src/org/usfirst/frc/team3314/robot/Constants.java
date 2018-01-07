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

}
