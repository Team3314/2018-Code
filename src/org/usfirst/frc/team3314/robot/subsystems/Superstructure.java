package org.usfirst.frc.team3314.robot.subsystems;

import org.usfirst.frc.team3314.robot.Constants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Superstructure implements Subsystem {
	
	public static Superstructure mInstance = new Superstructure();
	
	private Compressor pcm1;
	private DoubleSolenoid rampPiston;
	private boolean mRampReleased;
	
	public static Superstructure getInstance() {
		return mInstance;
	}
	
	
	public Superstructure() {
		pcm1 = new Compressor();
		pcm1.setClosedLoopControl(true);
		rampPiston = new DoubleSolenoid(4,5);
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
		SmartDashboard.putString("Ramp Piston", rampPiston.get().toString());
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
