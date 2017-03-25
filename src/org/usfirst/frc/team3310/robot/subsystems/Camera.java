package org.usfirst.frc.team3310.robot.subsystems;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.vision.ImageProcessor;
import org.usfirst.frc.team3310.vision.ImageProcessor.TargetInfo;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Camera extends Subsystem
{	
	public enum ImageOutput {NONE, DASHBOARD, FILE_OUTPUT, FILE_PROCESSED};
	
    private UsbCamera boilerCamera; 
    private UsbCamera frontCamera;
    private ImageProcessor imageProcessor;
    private CvSink cvSink;
    private CvSource outputStream;
    private Mat currentImageMat;

    private TargetInfo bestTarget;
	private int imageCounter = 0;
	private long processTimeMs = 0;
	private double offsetAngleDeg = 0;
	private boolean lastTargetValid = false;
	private boolean alignmentFinished = false;

    public Camera() {
    }
    
    public void initialize() {
		try {
			/*
			boilerCamera = CameraServer.getInstance().startAutomaticCapture("BoilerCamera", 1);
			boilerCamera.setResolution(640, 480);
			boilerCamera.setExposureManual(0);
			boilerCamera.setBrightness(50);

			Thread.sleep(1000);
			
			frontCamera = CameraServer.getInstance().startAutomaticCapture("FrontCamera", 0);
			frontCamera.setResolution(640, 480);
			frontCamera.setExposureAuto();

			// Get a CvSink. This will capture Mats from the camera
			cvSink = CameraServer.getInstance().getVideo(boilerCamera);
			// Setup a CvSource. This will send images back to the Dashboard
			outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			currentImageMat = new Mat();
			
	    	imageProcessor = new ImageProcessor(); 
	    	*/
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Camera constructor");
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	private void getImageFromCamera() {
		// Tell the CvSink to grab a frame from the camera and put it
		// in the source mat.  If there is an error notify the output.
		if (cvSink.grabFrame(currentImageMat) == 0) {
			// Send the output the error.
			DriverStation.reportError("Error getting image: " + cvSink.getError(), false);		
		}
	}
	
	private void readImageFromFile() {
		String fileName = "/home/lvuser/processed/image" + imageCounter + ".jpg";
		try {
    		currentImageMat = Imgcodecs.imread(fileName);
    	}
    	catch (Exception e) {
    		imageCounter = 0;
			DriverStation.reportError("Unable to read file = " + fileName, false);		
     	}
	}
	
	private void writeImage() {
		Imgcodecs.imwrite( "/home/lvuser/output/output" + imageCounter + ".png", currentImageMat);
	}
	
	private void writeProcessedImage() {
		Imgcodecs.imwrite( "/home/lvuser/output/processed" + imageCounter + ".png", currentImageMat);
	}
	
	private void saveImage(ImageOutput output) {
		if (output == ImageOutput.DASHBOARD) {
			outputStream.putFrame(currentImageMat);
		}
		else if (output == ImageOutput.FILE_OUTPUT) {
			writeImage();
			imageCounter++;
		}
		else if (output == ImageOutput.FILE_PROCESSED) {
			writeProcessedImage();
			imageCounter++;
		}
	}

	public void saveCameraImage(ImageOutput output) {
    	try {
			getImageFromCamera();        
			saveImage(output);
    	}
		catch (Exception e) {
			DriverStation.reportError("Error saving camera image", false);
		}
	}
	
	public TargetInfo readImageGetBestTarget(ImageOutput output) {
    	try {
			readImageFromFile();
	
			long startTime = System.currentTimeMillis();
			bestTarget = imageProcessor.findBestTarget(currentImageMat, true);
			processTimeMs = System.currentTimeMillis() - startTime;
	
			saveImage(output);
			
			return bestTarget;
		}
		catch (Exception e) {
			DriverStation.reportError("Error reading image and finding best target", false);
			return null;
		}
	}
	
	public TargetInfo getBestTarget(ImageOutput output) {
		lastTargetValid = false;
    	try {
			long startTime = System.currentTimeMillis();
    		getImageFromCamera();        
			bestTarget = imageProcessor.findBestTarget(currentImageMat, true);
			if (bestTarget != null) {
				bestTarget.angleToTargetDeg += offsetAngleDeg;
				if (bestTarget.compositeScore < ImageProcessor.MINIMUM_VALID_COMPOSITE_SCORE) {
					lastTargetValid = true;
				}
			}
			processTimeMs = System.currentTimeMillis() - startTime;
			saveImage(output);
			
			return bestTarget;
    	}
    	catch (Exception e) {
    		DriverStation.reportError("Error getting camera image and finding best target", false);
    		return null;
    	}
	}
	
	public boolean isTargetValid() {
		return lastTargetValid;
	}
		
	public void incrementOffsetAngle(double deltaAngle) {
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
		try {
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
		catch (Exception e) {
			
		}
	}
}