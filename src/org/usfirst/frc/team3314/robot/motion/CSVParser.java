package org.usfirst.frc.team3314.robot.motion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.subsystems.Drive;

import com.ctre.phoenix.motion.TrajectoryPoint;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CSVParser {

	private Drive drive = Drive.getInstance();
	private BufferedReader leftReader = null;
	private  BufferedReader rightReader = null;
	private  String leftLine = "";
	private String rightLine = "";
	private  String csvSplitBy = ",";
	private boolean finishedParsing = false;
	private Thread thread = null;
	private boolean firstRun = true;
	public void parse(Path path) {
		File leftPath = path.getLeftPath();
		File rightPath = path.getRightPath();
		if(leftPath != null && rightPath != null) {
			firstRun = true;
			finishedParsing = false;
			TrajectoryPoint nextPointLeft = new TrajectoryPoint();
			TrajectoryPoint nextPointRight = new TrajectoryPoint();
			try {
				leftReader = new BufferedReader(new FileReader(leftPath));
				rightReader = new BufferedReader(new FileReader(rightPath));
				double[] setpointsLeft = new double[8], setpointsRight = new double[8];
				String[] tempLeft, tempRight;	
				leftLine = leftReader.readLine();
				rightLine = rightReader.readLine();
				leftLine = leftReader.readLine();
				rightLine = rightReader.readLine();
				while((leftLine  != null) && (rightLine != null)) {
					tempLeft = leftLine.split(csvSplitBy);
					tempRight = rightLine.split(csvSplitBy);
					for(int i = 0; i < tempLeft.length; i++) {
						setpointsLeft[i] = Double.parseDouble(tempLeft[i]);
						setpointsRight[i] = Double.parseDouble(tempRight[i]);
					}
					nextPointLeft.position = setpointsLeft[3] * Constants.kFeetToEncoderCodes;
					nextPointLeft.velocity = setpointsLeft[4] * Constants.kFeetToEncoderCodes / 10;
					nextPointLeft.timeDur =  TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
					nextPointLeft.headingDeg = setpointsLeft[7];
					nextPointLeft.isLastPoint = false;

					nextPointRight.position = setpointsRight[3] * Constants.kFeetToEncoderCodes;
					nextPointRight.velocity = setpointsRight[4] * Constants.kFeetToEncoderCodes / 10;
					nextPointRight.timeDur =  TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
					nextPointRight.headingDeg = setpointsRight[7];
					nextPointRight.isLastPoint = false;

					if(firstRun) {
						nextPointLeft.zeroPos = true;
						nextPointRight.zeroPos = true;
						firstRun = false;
					} else {
						nextPointLeft.zeroPos = false;
						nextPointRight.zeroPos= false;
					}

					leftLine = leftReader.readLine();
					rightLine = rightReader.readLine();

					if(leftLine == null && rightLine == null) {
						nextPointLeft.isLastPoint = true;
						nextPointRight.isLastPoint = true;
					}

					drive.pushPoints(nextPointLeft, nextPointRight);

				}
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			finally {
				if((leftReader != null) && (rightReader != null)) {
					try {
						leftReader.close();
						rightReader.close();
						System.out.println("Done Parisng");
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void parseInverted(Path path) {
		File leftPath = path.getLeftPath();
		File rightPath = path.getRightPath();
		if(leftPath != null && rightPath != null) {
			firstRun = true;
			finishedParsing = false;
			TrajectoryPoint nextPointLeft = new TrajectoryPoint();
			TrajectoryPoint nextPointRight = new TrajectoryPoint();
			try {
				leftReader = new BufferedReader(new FileReader(leftPath));
				rightReader = new BufferedReader(new FileReader(rightPath));
				double[] setpointsLeft = new double[8], setpointsRight = new double[8];
				String[] tempLeft, tempRight;	
				leftLine = leftReader.readLine();
				rightLine = rightReader.readLine();
				leftLine = leftReader.readLine();
				rightLine = rightReader.readLine();
				while((leftLine  != null) && (rightLine != null)) {
					tempLeft = leftLine.split(csvSplitBy);
					tempRight = rightLine.split(csvSplitBy);
					for(int i = 0; i < tempLeft.length; i++) {
						setpointsLeft[i] = Double.parseDouble(tempLeft[i]);
						setpointsRight[i] = Double.parseDouble(tempRight[i]);
					}
					nextPointLeft.position = setpointsRight[3] * -Constants.kFeetToEncoderCodes;
					nextPointLeft.velocity = setpointsRight[4] * -Constants.kFeetToEncoderCodes / 10;
					nextPointLeft.timeDur =  TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
					nextPointLeft.headingDeg = setpointsLeft[7];
					nextPointLeft.isLastPoint = false;

					nextPointRight.position = setpointsLeft[3] * -Constants.kFeetToEncoderCodes;
					nextPointRight.velocity = setpointsLeft[4] * -Constants.kFeetToEncoderCodes / 10;
					nextPointRight.timeDur =  TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
					nextPointRight.headingDeg = setpointsRight[7];
					nextPointRight.isLastPoint = false;

					if(firstRun) {
						nextPointLeft.zeroPos = true;
						nextPointRight.zeroPos = true;
						firstRun = false;
					} else {
						nextPointLeft.zeroPos = false;
						nextPointRight.zeroPos= false;
					}

					leftLine = leftReader.readLine();
					rightLine = rightReader.readLine();

					if(leftLine == null && rightLine == null) {
						nextPointLeft.isLastPoint = true;
						nextPointRight.isLastPoint = true;
					}

					drive.pushPoints(nextPointLeft, nextPointRight);

				}
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			finally {
				if((leftReader != null) && (rightReader != null)) {
					try {
						leftReader.close();
						rightReader.close();
						System.out.println("Done Parisng");
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void start(Path path) {
		thread = new Thread(() -> {
			if(path.getBackwards())
				parseInverted(path);
			else 
				parse(path);
		});
		thread.start();
	}
}
