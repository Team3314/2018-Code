package org.usfirst.frc.team3314.robot.autos;

import org.usfirst.frc.team3314.robot.HumanInput;
import org.usfirst.frc.team3314.robot.subsystems.Camera;
import org.usfirst.frc.team3314.robot.motion.PathFollower;
import org.usfirst.frc.team3314.robot.paths.Path;
import org.usfirst.frc.team3314.robot.paths.PathList;
import org.usfirst.frc.team3314.robot.subsystems.Arm;
import org.usfirst.frc.team3314.robot.subsystems.Arm.ArmState;
import org.usfirst.frc.team3314.robot.subsystems.Drive;
import org.usfirst.frc.team3314.robot.subsystems.Drive.driveMode;
import org.usfirst.frc.team3314.robot.subsystems.Intake;
import org.usfirst.frc.team3314.robot.subsystems.Intake.IntakeState;
import edu.wpi.first.wpilibj.Timer;

public abstract class Autonomous {
	
	private char switchSide = ' ';
	private char scaleSide = ' ';
	
	private Drive drive = Drive.getInstance();
	private Intake intake = Intake.getInstance();
	private Arm arm = Arm.getInstance();
	private HumanInput hi = HumanInput.getInstance();
	private Camera camera = Camera.getInstance();
	
	private PathFollower pathFollower = new PathFollower();
	private Timer timer = new Timer();
	
	public abstract void reset();

	public abstract void update();
	
	protected void resetSensors() {
		drive.resetSensors();
	}
	
	//gear
	
	protected void setHighGear(boolean highGear) {
		drive.setHighGear(highGear);
	}
	
	protected void drivePower(double speed) {
		drive.setDriveMode(driveMode.OPEN_LOOP);
		drive.setDesiredSpeed(speed);
	}
	
	protected void drivePower(double leftSpeed, double rightSpeed) {
		drive.setDriveMode(driveMode.OPEN_LOOP);
		drive.setDesiredSpeed(leftSpeed, rightSpeed);
	}
	
	protected void driveMotionMagic(double desiredPos, double desiredAngle, double desiredSpeed) {
		drive.setDesiredPosition(desiredPos);
		drive.setDesiredAngle(desiredAngle);
		drive.configCruiseVelocity(desiredSpeed);
		drive.setDriveMode(driveMode.MOTION_MAGIC);
	}
	
	protected void turnMotionMagic(double desiredAngle) {
		drive.setDesiredAngle(desiredAngle);
	}
	
	protected double getDesiredAngle() {
		return drive.getDesiredAngle();
	}
	
	protected boolean isMotionMagicDone() {
		return (drive.getAveragePositionTicks() > drive.getDesiredPosition() - 50) || (drive.getAveragePositionTicks() < drive.getDesiredPosition() + 50);
	}
	
	protected double getAngle() {
		return drive.getAngle();
	}
	
	protected void driveGyrolock(double desiredSpeed, double desiredAngle, driveMode mode) {
		drive.setDriveMode(mode);
		drive.setDesiredSpeed(desiredSpeed);
		drive.setDesiredAngle(desiredAngle);
	}
	
	protected boolean gyroTurnDone() {
		return drive.gyroInPosition();
	}
	protected boolean targetInView() {
		return camera.isTargetInView();
	}
	//motion profiling
	
	protected void loadPath(Path path) {
		pathFollower.loadPoints(path);
	}
	protected void startPathFollower() {
		pathFollower.start();
	}
	protected boolean isPathDone() {
		return pathFollower.isDone();
	}
	protected Path getPath(String path) {
		return PathList.getPath(path);
	}
	
	//fms information
	
	protected String getSwitch() {
		return "Switch" + switchSide;
	}
	protected String getScale() {
		return "Scale" + scaleSide;
	}
	protected String getStart() {
		return hi.getLeftRightCenter();
	}
	public void setGameData(String data) {
		if(data.length() >= 2) {
			switchSide = data.charAt(0);
			scaleSide = data.charAt(1);
		}
		else {
			System.out.println("NO MATCH DATA RECIEVED");
		}
	}
	
	//timer
	
	public void resetTimer() {
		timer.stop();
		timer.reset();
	}
	public void startTimer() {
		timer.start();
	}
	public double getTime() {
		return timer.get();
	}
	
	//intake
	
	public void intakeCube() {
		intake.setDesiredState(IntakeState.INTAKING);
	}
	public void releaseCubeFast() {
		intake.setDesiredState(IntakeState.RELEASING);
	}
	public void releaseCubeSlow() {
		intake.setDesiredState(IntakeState.RELEASE_SLOW);
	}
	public void stopIntake()  {
		intake.setDesiredState(IntakeState.HOLDING);
	}
	
	//cube sensor
	
	public boolean hasCube() {
		return intake.getState() == IntakeState.HOLDING && intake.senseCube();
	}
	public boolean hasNoCube() {
		return intake.getState() == IntakeState.HOLDING && !intake.senseCube();
	}
	
	//arm
	
	public void armToScaleHigh() {
		arm.setDesiredState(ArmState.TO_SCALE_HIGH);
		arm.setTargetSpeed(1);
	}
	public void armToScaleLow() {
		arm.setDesiredState(ArmState.TO_SCALE_AUTO);
		arm.setTargetSpeed(1);
	}
	public void armToSwitch() {
		arm.setDesiredState(ArmState.TO_SWITCH);
		arm.setTargetSpeed(1);
	}
	public void armToPickUp() {
		arm.setDesiredState(ArmState.TO_PICKUP);
		arm.setTargetSpeed(1);
	}
	public void armToHolding() {
		arm.setDesiredState(ArmState.TO_HOLDING);
		arm.setTargetSpeed(1);
	}
	public boolean armStopped() {
		return arm.getState() == ArmState.STOPPED;
	}
	
	public void startTracking() {
		camera.setTrackingRequest(true);
	}
	
	public void endTracking() {
		camera.setTrackingRequest(false);
	}
}
