package org.usfirst.frc.team3314.robot;

import org.usfirst.frc.team3314.robot.autos.*;

public class AutoModeSelector {

	private static HumanInput hi = HumanInput.getInstance();
	private static String autoModeBinary;
	private static int autoModeDecimal;
	private static Autonomous autoMode;
	private static Autonomous auto0 = new AutoNothing(), 
			auto1 = new AutoCrossBaseline(), 
			auto2 = new MotionProfile();
	private static Autonomous[] autos = {auto0, auto1, auto2};
	
	public static Autonomous getSelectedAutoMode() {
		autoModeBinary = "" + hi.getBinaryEight() + hi.getBinaryFour() + hi.getBinaryTwo() + hi.getBinaryOne();
		autoModeDecimal = Integer.parseInt(autoModeBinary, 2);
		autoMode = autos[autoModeDecimal];
 		return autoMode;
	}
}
