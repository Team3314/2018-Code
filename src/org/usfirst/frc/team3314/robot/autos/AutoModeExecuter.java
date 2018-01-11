package org.usfirst.frc.team3314.robot.autos;

public class AutoModeExecuter {

	private Autonomous autoMode;
	private Thread thread = null;
	
	public void setAutoMode(Autonomous newAutoMode) {
		autoMode = newAutoMode;
	}
	
	public void start() {
		if(thread == null) {
			thread = new Thread(() -> {
						if(autoMode != null) {
							autoMode.update();
						}
				});
				thread.start();
			}
		}
	}
