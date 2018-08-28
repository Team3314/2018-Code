package org.usfirst.frc.team3314.robot;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DataLogger {
	
	private static DataLogger mInstance = new DataLogger();
	
	public static DataLogger getInstance() {
		return mInstance;
	}
	
	File data;
	PrintWriter pw;
	StringBuilder sb;
    boolean run = false;
    private long timestamp;

	public void createNewFile(String name) {
		run = false;
		data = new File("/home/lvuser/"+ name + System.currentTimeMillis() + ".csv");
		try {
			if(!data.exists()) {
				data.createNewFile();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		if(pw != null)
			pw.flush();
	}
	
	public void logData(String[] names, String[] values) {
	     if (!run) {
	    	 try {
	 			pw = new PrintWriter(data);
	 			sb = new StringBuilder();
	 			SmartDashboard.putBoolean("try block run", true);
	 		}
	 		catch (IOException e) {
	 			e.printStackTrace();
	 		}
	    	 sb.append("Time");
		     sb.append(',');
		     for(int i = 0; i < names.length; i++) {
			     sb.append(names[i].toString());
			     sb.append(',');
		     }
		     sb.append('\n');
		     run = true;
		     timestamp = System.currentTimeMillis();
	     }
	     sb.append(System.currentTimeMillis() - timestamp);
	     sb.append(',');
	     for(int i = 0; i < values.length; i++) {
	    	 sb.append(values[i].toString());
	    	 sb.append(',');
	     }
	     sb.append('\n');
	     pw.print(sb.toString());
	     sb.setLength(0);
	     
	}

}
