package org.usfirst.frc.team3314.robot;

import org.usfirst.frc.team3314.robot.autos.*;
import edu.wpi.first.wpilibj.DriverStation;

public class AutoModeSelector {

	private HumanInput hi = HumanInput.getInstance();
	private String autoModeBinary;
	private int autoModeDecimal;
	private DriverStation fms = DriverStation.getInstance();
	private Autonomous autoMode;
	private String gameData;
	private Autonomous auto0 = new AutoNothing(), 
			auto1 = new AutoCrossBaseline(),
			auto2 = new AutoCubeToScale(),
			auto3 = new AutoCubeToSwitch(),
			auto4 = new AutoCubeToSwitchFromCenter(),
			auto5 = new AutoScaleThenSwitch(),
			auto6 = new AutoSwitchThenScale(),
			auto7 = new AutoTwoCubeScale(),
			auto8 = new MotionProfile(),
			auto9 = new AutoVisionCubeFuckery();
	private  Autonomous[] autos = {auto0, auto1, auto2, auto3, auto4, auto5, auto6, auto7, auto8, auto9};
	
	public Autonomous getSelectedAutoMode() {
		pollFMS();
		if(gameData.length() < 2) {
			return auto1;
		}
		if((gameData.charAt(0) == 'L') && gameData.charAt(1) == 'L') {
			autoModeBinary = "" + hi.getLLBinaryEight() + hi.getLLBinaryFour() + hi.getLLBinaryTwo() + hi.getLLBinaryOne();
		}
		else if (gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R') {
			autoModeBinary = "" + hi.getLRBinaryEight() + hi.getLRBinaryFour() + hi.getLRBinaryTwo() + hi.getLRBinaryOne();
		}
		else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L' ) {
			autoModeBinary = "" + hi.getRLBinaryEight() + hi.getRLBinaryFour() + hi.getRLBinaryTwo() + hi.getRLBinaryOne();
		}
		else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
			autoModeBinary = "" + hi.getRRBinaryEight() + hi.getRRBinaryFour() + hi.getRRBinaryTwo() + hi.getRRBinaryOne();
		}
		autoModeDecimal = Integer.parseInt(autoModeBinary, 2);
		autoMode = autos[8];//autoModeDecimal];
		autoMode.setGameData(gameData);
		autoMode.reset();
 		return autoMode;
	}
	public void pollFMS() {
			gameData = fms.getGameSpecificMessage();
	}
	public String getGameData() {
		return gameData;
	}
}
