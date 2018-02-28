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
		 paths.put("SwitchLScaleR", new Path("SwitchLeftToScaleRight",);
		 paths.put("SwitchRScaleL", new SwitchRightToScaleLeft());
		 paths.put("SwitchRScaleR", new SwitchRightToScaleRight());
		 paths.put("ScaleLSwitchL", new ScaleLeftToSwitchLeft());
		 paths.put("ScaleLSwitchR", new ScaleLeftToSwitchRight());
		 paths.put("ScaleRSwitchL", new ScaleRightToSwitchLeft());
		 paths.put("ScaleRSwitchR", new ScaleRightToSwitchRight());
		 paths.put("Turn" , new PathTwo());
		 paths.put("Drive straight", new PathOne());
	 };
	 public static Path getPath(String path) {
		 return paths.get(path);
	 }
	
}
