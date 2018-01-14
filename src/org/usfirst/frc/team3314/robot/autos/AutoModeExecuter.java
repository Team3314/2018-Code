package org.usfirst.frc.team3314.robot.autos;

public class AutoModeExecuter {

	private Autonomous autoMode;
	private double updateRate = 1/50;
	private long waitTime = (long) (updateRate * 1000.0);
	private Thread thread = null;
	
	public void setAutoMode(Autonomous newAutoMode) {
		autoMode = newAutoMode;
	}
	
	public void start() {
		autoMode.reset();
		if(thread == null) {
			thread = new Thread(() -> {
				while(autoMode != null) {
					autoMode.update();
					try {
						Thread.sleep(waitTime);
					}catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		thread.start();
		}
	}
	public void stop() {
		if(autoMode != null) {
			autoMode = null;
		}
		thread = null;
	}
}
