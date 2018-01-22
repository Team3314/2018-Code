package org.usfirst.frc.team3314.robot;

import org.usfirst.frc.team3314.robot.autos.*;
import edu.wpi.first.wpilibj.DriverStation;

public class AutoModeSelector {

	private  HumanInput hi = HumanInput.getInstance();
	private  String autoModeBinary;
	private  int autoModeDecimal;
	private DriverStation fms = DriverStation.getInstance();
	private  Autonomous autoMode;
	private String gameData;
	private Autonomous auto0 = new AutoNothing(), 
			auto1 = new AutoCrossBaseline(),
			auto2 = new MotionProfile();
	private  Autonomous[] autos = {auto0, auto1, auto2};
	
	public Autonomous getSelectedAutoMode() {
		autoModeBinary = "" + hi.getBinaryEight() + hi.getBinaryFour() + hi.getBinaryTwo() + hi.getBinaryOne();
		autoModeDecimal = Integer.parseInt(autoModeBinary, 2);
		autoMode = autos[2];//autoModeDecimal];
		pollFMS();
		autoMode.setGameData(gameData);
		autoMode.reset();
 		return autoMode;
	}
	
	public void pollFMS() {
			gameData = fms.getGameSpecificMessage();
	}
}
