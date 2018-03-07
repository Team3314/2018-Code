package org.usfirst.frc.team3314.robot.motion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.usfirst.frc.team3314.robot.Constants;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.Path.Mode;
import org.usfirst.frc.team3314.robot.subsystems.Drive;

import com.ctre.phoenix.motion.TrajectoryPoint;

public class CSVParser {

	private Drive drive = Drive.getInstance();
	private BufferedReader leftReader = null;
	private  BufferedReader rightReader = null;
	private  String leftLine = "";
	private String rightLine = "";
	private  String csvSplitBy = ",";
	private Thread thread = null;
	private boolean firstRun = true;

	private Mode mode;
	private double heading = 0;

	public void parse(Path path) {
		File leftPath = path.getLeftPath();
		File rightPath = path.getRightPath();
		if(leftPath != null && rightPath != null) {
			firstRun = true;
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
					switch(mode) {
					case BACKWARD_HIGH:
						nextPointLeft.position =  -setpointsRight[3] * Constants.kFeetToEncoderCodes;
						nextPointLeft.velocity = addAccelerationAndVoltageCompensation(-setpointsRight[4], -setpointsRight[5], 
								Constants.kMotionProfileLeftBackHigh_kV, 
								Constants.kMotionProfileLeftBackHigh_kA, 
								Constants.kMotionProfileLeftBackHigh_Intercept);
						nextPointRight.position = -setpointsLeft[3] * Constants.kFeetToEncoderCodes;
						nextPointRight.velocity = addAccelerationAndVoltageCompensation(-setpointsLeft[4], -setpointsLeft[5], 
								Constants.kMotionProfileRightBackHigh_kV, 
								Constants.kMotionProfileRightBackHigh_kA, 
								Constants.kMotionProfileRightBackHigh_Intercept);
						break;
					case BACKWARD_LOW:
						nextPointLeft.position =  -setpointsRight[3] * Constants.kFeetToEncoderCodes;
						nextPointLeft.velocity = addAccelerationAndVoltageCompensation(-setpointsRight[4], -setpointsRight[5], 
								Constants.kMotionProfileLeftBackLow_kV, 
								Constants.kMotionProfileLeftBackLow_kA, 
								Constants.kMotionProfileLeftBackLow_Intercept);
						nextPointRight.position = -setpointsLeft[3] * Constants.kFeetToEncoderCodes;
						nextPointRight.velocity =  addAccelerationAndVoltageCompensation(-setpointsLeft[4], -setpointsLeft[5], 
								Constants.kMotionProfileRightBackLow_kV, 
								Constants.kMotionProfileRightBackLow_kA, 
								Constants.kMotionProfileRightBackLow_Intercept);
						break;
					case FORWARD_HIGH:
						nextPointLeft.position =  setpointsLeft[3] * Constants.kFeetToEncoderCodes;
						nextPointLeft.velocity = addAccelerationAndVoltageCompensation(setpointsLeft[4], setpointsLeft[5], 
								Constants.kMotionProfileLeftForeHigh_kV, 
								Constants.kMotionProfileLeftForeHigh_kA, 
								Constants.kMotionProfileLeftForeHigh_Intercept);
						nextPointRight.position = setpointsRight[3] * Constants.kFeetToEncoderCodes;
						nextPointRight.velocity = addAccelerationAndVoltageCompensation(setpointsRight[4], setpointsRight[5], 
								Constants.kMotionProfileRightForeHigh_kV, 
								Constants.kMotionProfileRightForeHigh_kA, 
								Constants.kMotionProfileRightForeHigh_Intercept);
						break;
					case FORWARD_LOW:
						nextPointLeft.position =  setpointsLeft[3] * Constants.kFeetToEncoderCodes;
						nextPointLeft.velocity = addAccelerationAndVoltageCompensation(setpointsLeft[4], setpointsLeft[5], 
								Constants.kMotionProfileLeftForeLow_kV, 
								Constants.kMotionProfileLeftForeLow_kA, 
								Constants.kMotionProfileLeftForeLow_Intercept);
						nextPointRight.position = setpointsRight[3] * Constants.kFeetToEncoderCodes;
						nextPointRight.velocity = addAccelerationAndVoltageCompensation(setpointsRight[4], setpointsRight[5], 
								Constants.kMotionProfileRightForeLow_kV, 
								Constants.kMotionProfileRightForeLow_kA, 
								Constants.kMotionProfileRightForeLow_Intercept);
						break;
					default:
						break;
					}
					
					heading = setpointsRight[7];
					if(heading >= Math.PI) {
						heading -= 2*Math.PI;
					}
					
					heading = Math.toDegrees(heading);
					
					nextPointLeft.timeDur =  TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
					nextPointLeft.velocity *= Constants.kFPSToTicksPer100ms;
					nextPointLeft.profileSlotSelect0 = Constants.kMotionProfileSlot;
					nextPointLeft.profileSlotSelect1 = Constants.kMotionProfileHeadingSlot;
					nextPointLeft.auxiliaryPos = heading * (8192.0/360.0);
					nextPointLeft.isLastPoint = false;
					
					nextPointRight.timeDur =  TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
					nextPointRight.velocity *= Constants.kFPSToTicksPer100ms;
					nextPointRight.profileSlotSelect0 = Constants.kMotionProfileSlot;
					nextPointRight.profileSlotSelect1 = Constants.kMotionProfileHeadingSlot;
					nextPointRight.auxiliaryPos = heading * (8192.0/360.0);
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

	public double addAccelerationAndVoltageCompensation(double velSetpoint, double accelSetpoint, 
			double kV, 
			double kA, 
			double vIntercept) {
		return (velSetpoint + (accelSetpoint * kA + vIntercept) / kV) ; // 
	}

	public void start(Path path) {
		mode = path.getMode();
		switch(mode) {
		case BACKWARD_HIGH:
			drive.setFeedForward(Constants.kMotionProfileLeftBackHigh_kF, Constants.kMotionProfileRightBackHigh_kF);
			break;
		case BACKWARD_LOW:
			drive.setFeedForward(Constants.kMotionProfileLeftBackLow_kF, Constants.kMotionProfileRightBackLow_kF);
			break;
		case FORWARD_HIGH:
			drive.setFeedForward(Constants.kMotionProfileLeftForeHigh_kF, Constants.kMotionProfileRightForeHigh_kF);
			break;
		case FORWARD_LOW:
			drive.setFeedForward(Constants.kMotionProfileLeftForeLow_kF, Constants.kMotionProfileRightForeLow_kF);
			break;
		default:
			break;
		}
		thread = new Thread(() -> {
			parse(path);
		});
		thread.start();
	}
}
