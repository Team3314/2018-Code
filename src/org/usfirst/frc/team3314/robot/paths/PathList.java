package org.usfirst.frc.team3314.robot.paths;

import java.util.HashMap;
import java.util.Map;

public class PathList {
	
	 private static Map<String, Path> paths = new HashMap<String, Path>(){
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			 paths.put("StartLSwitchL", new StartLeftToSwitchLeft());
			 paths.put("StartLSwitchR", new StartLeftToSwitchRight());
			 paths.put("StartRSwitchL", new StartRightToSwitchLeft());
			 paths.put("StartRSwitchR", new StartRightToSwitchRight());
			 paths.put("StartCSwitchL", new StartCenterToSwitchLeft());
			 paths.put("StartCSwitchR", new StartCenterToSwitchRight());
			 paths.put("SwitchLScaleL", new SwitchLeftToScaleLeft());
			 paths.put("SwitchLScaleR", new SwitchLeftToScaleRight());
			 paths.put("SwitchRScaleL", new SwitchRightToScaleLeft());
			 paths.put("SwitchRScaleR", new SwitchRightToScaleRight());
			 paths.put("ScaleLSwitchL", new ScaleLeftToSwitchLeft());
			 paths.put("ScaleLSwitchR", new ScaleLeftToSwitchRight());
			 paths.put("ScaleRSwitchL", new ScaleRightToSwitchLeft());
			 paths.put("ScaleRSwitchR", new ScaleRightToSwitchRight());
			 
		 }
	 };
	 public static Path getPath(String path) {
		 return paths.get(path);
	 }
	
}
