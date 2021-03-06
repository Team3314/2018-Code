package org.usfirst.frc.team3314.robot;

import org.usfirst.frc.team3314.robot.autos.*;
import edu.wpi.first.wpilibj.DriverStation;

public class AutoModeSelector {
	private  HumanInput hi = HumanInput.getInstance();
	private  String autoModeBinary, delayBinary;
	private  int autoModeDecimal, delayDecimal;
	private DriverStation fms = DriverStation.getInstance();
	private  Autonomous autoMode; 
	private String gameData;
	private Autonomous auto0 = new AutoNothing(), 
			auto1 = new AutoCrossBaseline(),
			auto2 = new AutoCubeToScale(),
			auto3 = new AutoCubeToSwitch(),
			auto4 = new AutoScaleThenSwitch(),
			auto5 = new AutoCubeToScaleNullZone(),
			auto6 = new AutoTwoCubeScale(),
			auto7 = new AutoDriveToScale();
	private  Autonomous[] autos = {auto0, auto1, auto2, auto3, auto4, auto5, auto6, auto7};
	
	public Autonomous getSelectedAutoMode() {
		pollFMS();
		if(gameData.length() < 2) {
			return auto1;
		}
		if((gameData.charAt(0) == 'L') && gameData.charAt(1) == 'L') {
			autoModeBinary = ""  + hi.getLLBinaryFour() + hi.getLLBinaryTwo() + hi.getLLBinaryOne();
		}
		else if (gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R') {
			autoModeBinary = ""  + hi.getLRBinaryFour() + hi.getLRBinaryTwo() + hi.getLRBinaryOne();
		}
		else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L' ) {
			autoModeBinary = ""  + hi.getRLBinaryFour() + hi.getRLBinaryTwo() + hi.getRLBinaryOne();
		}
		else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
			autoModeBinary = "" + hi.getRRBinaryFour() + hi.getRRBinaryTwo() + hi.getRRBinaryOne();
		}
		autoModeDecimal = Integer.parseInt(autoModeBinary, 2);
		autoMode = autos[autoModeDecimal];
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
	public double getDelay() {
		delayBinary = "" +  hi.getDelayEight() + hi.getDelayFour() + hi.getDelayTwo() + hi.getDelayOne();
		delayDecimal = Integer.parseInt(delayBinary, 2);
		return delayDecimal;
	}
}
