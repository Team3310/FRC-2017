package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.Robot.OperationMode;
import org.usfirst.frc.team3310.vision.ImageProcessor;
import org.usfirst.frc.team3310.vision.ImageProcessor.TargetInfo;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.CameraServer;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera extends Subsystem
{	
    private USBCamera centerCam; 
    private ImageProcessor imageProcessor;
	private Image currentImage = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

    private TargetInfo bestTarget;
	private int imageCounter = 0;
	private long processTimeMs = 0;
	private double offsetAngleDeg = 0;
	private boolean lastTargetValid = false;
	private boolean alignmentFinished = false;

    public Camera() {
		try {
	    	centerCam = new USBCamera("cam0");
//	    	centerCam.setBrightness(0);
//	    	centerCam.setExposureManual(0);
	    	centerCam.updateSettings();
	    	centerCam.startCapture();
	    	
	    	imageProcessor = new ImageProcessor();
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Camera constructor");
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void postCameraImageToDashboard() {
		getCamera().getImage(currentImage);        
        CameraServer.getInstance().setImage(currentImage);
	}
	
	public void readImagePostProcessedImageToDashboard() {
    	try {
    		NIVision.imaqReadFile(currentImage, "/home/lvuser/processed/image" + imageCounter + ".jpg");
    	}
    	catch (Exception e) {
    		imageCounter = 0;
    		NIVision.imaqReadFile(currentImage, "/home/lvuser/processed/image" + imageCounter + ".jpg");
    	}
        bestTarget = imageProcessor.findBestTarget(currentImage, true);
        CameraServer.getInstance().setImage(currentImage);
        
		imageCounter++;
	}
	
	public TargetInfo readImageGetBestTarget() {
		long startTime = System.currentTimeMillis();
    	try {
    		NIVision.imaqReadFile(currentImage, "/home/lvuser/input/Image" + imageCounter + ".jpg");
    	}
    	catch (Exception e) {
    		DriverStation.reportError("Unable to load file /home/lvuser/input/Image" + imageCounter + ".jpg", false);
    	}
		imageCounter++;

		bestTarget = imageProcessor.findBestTarget(currentImage, true);
		processTimeMs = System.currentTimeMillis() - startTime;

		writeProcessedImage(currentImage);
		
		return bestTarget;
	}
	
	public boolean isTargetValid() {
		return lastTargetValid;
	}
		
	public TargetInfo getBestTarget() {
		lastTargetValid = false;
    	try {
			getCamera().getImage(currentImage);        
			bestTarget = imageProcessor.findBestTarget(currentImage, Robot.operationMode == OperationMode.TEST);
			if (bestTarget != null) {
				bestTarget.angleToTargetDeg += offsetAngleDeg;
				if (bestTarget.compositeScore < ImageProcessor.MINIMUM_VALID_COMPOSITE_SCORE) {
					lastTargetValid = true;
				}
			}
			
			if (Robot.operationMode == OperationMode.TEST) {
				CameraServer.getInstance().setImage(currentImage);
			}		
			
			return bestTarget;
    	}
    	catch (Exception e) {
    		return null;
    	}
	}
	
	public USBCamera getCamera() {
		return centerCam;
	}
		
	public void writeImage() {
		getCamera().getImage(currentImage);

		NIVision.RGBValue rgbValues = new NIVision.RGBValue();
		NIVision.imaqWriteFile(currentImage, "/home/lvuser/output/image" + imageCounter + ".jpg", rgbValues);
	
		imageCounter++;
	}
	
	public void writeProcessedImage(Image processedImage) {
		NIVision.RGBValue rgbValues = new NIVision.RGBValue();
		NIVision.imaqWriteFile(processedImage, "/home/lvuser/processed/image" + imageCounter + ".jpg", rgbValues);
	}
	
	public void incrementAngleOffset(double deltaAngle) {
		offsetAngleDeg += deltaAngle;
	}
	
	public boolean isAlignmentFinished() {
		return alignmentFinished;
	}

	public void setAlignmentFinished(boolean state) {
		if (state && lastTargetValid) {
			alignmentFinished = true;
		}
		else {
			alignmentFinished = false;
		}
		
		return;
	}
	
	public void updateStatus(Robot.OperationMode operationMode) {
		if (operationMode == Robot.OperationMode.TEST) {
			SmartDashboard.putNumber("Image Counter", imageCounter);
			SmartDashboard.putNumber("Camera Distance", bestTarget == null ? 0.0 : bestTarget.distanceToTargetFt);
			SmartDashboard.putNumber("Camera Angle",  bestTarget == null ? 0.0 : bestTarget.angleToTargetDeg);
			SmartDashboard.putNumber("Camera Score",  bestTarget == null ? 0.0 : bestTarget.compositeScore);
			SmartDashboard.putNumber("Camera Time ms",  processTimeMs);
			SmartDashboard.putNumber("Camera Offset",  offsetAngleDeg);
			SmartDashboard.putBoolean("Camera Aligned",  alignmentFinished);
			SmartDashboard.putBoolean("Last Target Valid",  lastTargetValid);
		}
	}
}