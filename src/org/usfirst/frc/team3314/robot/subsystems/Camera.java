package org.usfirst.frc.team3314.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Camera implements Subsystem {

	private static Camera mInstance = new Camera();
	
	public static Camera getInstance() {
		return mInstance;
	}
	
	private NetworkTableInstance table = NetworkTableInstance.create();
	private NetworkTableEntry values = new NetworkTableEntry(table, 1);
	
	double tx, ty, ta, ts; //horiz offset, vert offset, area, skew
	
	private Camera() {
		table.getTable("limelight");
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		tx = values.getDouble(0);
		ty = values.getDouble(0);
		ta = values.getDouble(0);
		ts = values.getDouble(0);
	}

	@Override
	public void outputToSmartDashboard() {
		// TODO Auto-generated method stub

	}

}
