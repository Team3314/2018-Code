package org.usfirst.frc.team3314.robot.paths;

import java.util.HashMap;
import java.util.Map;

public class PathList {
	
	 private static Map<String, Path> paths;
		 /**
		 * 
		 */
	 static {
		 paths = new HashMap<String, Path>();
		 paths.put("StartLSwitchL", new Path("StartLeftToSwitchLeft", Mode.BACKWARD_HIGH));
		 paths.put("StartLSwitchR", new Path("StartLeftToSwitchRight", Mode.BACKWARD_HIGH));
		 paths.put("StartRSwitchL", new Path("StartRightToSwitchLeft", Mode.BACKWARD_HIGH);
		 paths.put("StartRSwitchR", new Path("StartRightToSwitchRight", Mode.BACKWARD_HIGH);
		 paths.put("StartCSwitchL", new Path("StartCenterToSwitchLeft", Mode.FORWARD_HIGH);
		 paths.put("StartCSwitchR", new Path("StartCenterToSwitchLeft", Mode.FORWARD_HIGH);
		 paths.put("SwitchLScaleL", new Path("SwitchLeftToScaleLeft", Mode.BACKWARD_HIGH);
		 paths.put("SwitchLScaleR", new Path("SwitchLeftToScaleRight", Mode.BACKWARD_HIGH);
		 paths.put("SwitchRScaleL", new Path("SwitchRightToScaleLeft", Mode.BACKWARD_HIGH);
		 paths.put("SwitchRScaleR", new Path("SwitchRightToScaleRight", Mode.BACKWARD_HIGH);
		 paths.put("ScaleLSwitchL", new Path("ScaleLeftToSwitchLeft", Mode.FORWARD_HIGH);
		 paths.put("ScaleLSwitchR", new Path("ScaleLeftToSwitchRight", Mode.FORWARD_HIGH);
		 paths.put("ScaleRSwitchL", new Path("ScaleRightToSwitchLeft", Mode.FORWARD_HIGH);
		 paths.put("ScaleRSwitchR", new Path("ScaleRightToSwitchRight", Mode.FORWARD_HIGH);
		 paths.put("Turn" , new "Turn", Mode.FORWARD_HIGH);
		 paths.put("Drive straight", new Path("MotionProfileOne", Mode.FORWARD_HIGH);
	 };
	 public static Path getPath(String path) {
		 return paths.get(path);
	 }
	
}
