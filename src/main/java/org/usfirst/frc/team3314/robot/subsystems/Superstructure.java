package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Superstructure implements Subsystem {
	
	public static Superstructure mInstance = new Superstructure();
	
	private Compressor pcm1;
	private Solenoid rampPiston;
	private boolean mRampReleased;
	
	public static Superstructure getInstance() {
		return mInstance;
	}
	
	
	public Superstructure() {
		pcm1 = new Compressor();
		pcm1.setClosedLoopControl(true);
		rampPiston = new Solenoid(6);
	}


	@Override
	public void update() {
		if(mRampReleased) {
			rampPiston.set(Constants.kRampPistonIn);
		}
		else {
			rampPiston.set(Constants.kRampPistonOut);
		}
	}


	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putBoolean("Pressure Switch", pcm1.getPressureSwitchValue());
		SmartDashboard.putBoolean("Ramp Piston", mRampReleased);
	}


	@Override
	public void resetSensors() {
	}
	
	public void releaseRamp(boolean release) {
		mRampReleased = release;
	}
	
	public boolean getRamp() {
		return mRampReleased;
	}
}
