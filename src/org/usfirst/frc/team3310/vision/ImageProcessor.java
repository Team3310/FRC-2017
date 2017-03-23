package org.usfirst.frc.team3310.vision;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {
	
	public static final double TARGET_HEIGHT_FT = 6.0/12;
    public static final double TARGET_WIDTH_FT = 12.75/12.0;
    public static final double TARGET_ASPECT_RATIO = TARGET_HEIGHT_FT / TARGET_WIDTH_FT;
    
    public static final double CAMERA_OFFSET_FT = 0.0;
            
    public static final double OPTIMAL_RECT = 0.6;
    public static final double OPTIMAL_AR = TARGET_ASPECT_RATIO;
    public static final double OPTIMAL_XX = 0;
    public static final double OPTIMAL_YY = 0;
    public static final double OPTIMAL_TOP_HEIGHT = 0.4;
    public static final double OPTIMAL_BOT_HEIGHT = 0.6;
    public static final double OPTIMAL_HEIGHT = 28;
    public static final double OPTIMAL_WIDTH = 68;
    
    public static final double WEIGHT_FACTOR_RECT = 1;
    public static final double WEIGHT_FACTOR_AR = 3;
    public static final double WEIGHT_FACTOR_XX = 1;
    public static final double WEIGHT_FACTOR_YY = 1;
    public static final double EDGE_FACTOR = 1;
    public static final double TOP_HEIGHT_FACTOR = 1;
    public static final double BOT_HEIGHT_FACTOR = 1;
    public static final double WEIGHT_FACTOR_HEIGHT = 1;
    public static final double WEIGHT_FACTOR_WIDTH = 1;
   
    public static final double CAMERA_AIM_VERTICAL_ANGLE = 35;
    public static final double CAMERA_FOV_HORIZONTAL_ANGLE = 53;  //70.42;  
    public static final double CAMERA_FOV_VERTICAL_ANGLE = 43.3;
	public static final double tanHalfFOV = Math.tan(Math.toRadians(CAMERA_FOV_HORIZONTAL_ANGLE / 2));
	
	public static final double MINIMUM_VALID_COMPOSITE_SCORE = 4;

	public static final double[] HSL_THRESHOLD_HUE = {50, 99};
	public static final double[] HSL_THRESHOLD_SATURATION = {67, 255};
	public static final double[] HSL_THRESHOLD_LUMINANCE = {37, 255};

	public static final double MIN_WIDTH = 40.0;
	public static final double MAX_WIDTH = 120.0;
	public static final double MIN_HEIGHT = 10.0;
	public static final double MAX_HEIGHT = 60.0;
	public static final double MIN_AREA = 400.0;
	public static final double MAX_AREA = 2000.0;
	public static final double MIN_ASPECT_RATIO = 2;
	public static final double MAX_ASPECT_RATIO = 5;

	private Mat hslThresholdOutput = new Mat();
	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
	
	private int textYOffset;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private void hslThreshold(Mat input, double[] hue, double[] sat, double[] lum,
		Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
		Core.inRange(out, new Scalar(hue[0], lum[0], sat[0]),
			new Scalar(hue[1], lum[1], sat[1]), out);
	}

	private void findContours(Mat input, boolean externalOnly,
		List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		}
		else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);
	}


	private ArrayList<Rect> filterContours(Mat cameraImage, List<MatOfPoint> inputContours, boolean includeOverlays) {
    	// Filter out contours based on criteria
    	textYOffset = 0;
		ArrayList<Rect> filteredRectOutput = new ArrayList<Rect>();
		for (int i = 0; i < findContoursOutput.size(); i++) {
			
			final MatOfPoint contour = findContoursOutput.get(i);
			Rect boundingRect = Imgproc.boundingRect(contour);
			
			if (includeOverlays) {
				Imgproc.rectangle(cameraImage, 
						new Point(boundingRect.x, boundingRect.y), 
						new Point(boundingRect.x + boundingRect.width, boundingRect.y + boundingRect.height),
						new Scalar(0, 255, 255), 1);
			}
			
			if (boundingRect.width < MIN_WIDTH || boundingRect.width > MAX_WIDTH) {
				if (includeOverlays) {
					addTextBestRect(cameraImage, boundingRect, "W: " + boundingRect.width);
				}
				continue;
			}

			if (boundingRect.height < MIN_HEIGHT || boundingRect.height > MAX_HEIGHT) {
				if (includeOverlays) {
					addTextBestRect(cameraImage, boundingRect, "H: " + boundingRect.height);
				}
				continue;
			}

			final double area = Imgproc.contourArea(contour);
			if (area < MIN_AREA || area > MAX_AREA) {
				if (includeOverlays) {
					addTextBestRect(cameraImage, boundingRect, "A: " + area);
				}
				continue;
			}
			
			final double ratio = boundingRect.width / (double)boundingRect.height;
			if (ratio < MIN_ASPECT_RATIO || ratio > MAX_ASPECT_RATIO) {
				if (includeOverlays) {
					addTextBestRect(cameraImage, boundingRect, "AR: " + ratio);
				}
				continue;
			}
			
			filteredRectOutput.add(boundingRect);
		}
		
		return filteredRectOutput;
	}

    public TargetInfo findBestTarget(Mat cameraImage, boolean includeOverlays) { 
        if (cameraImage == null) {
            return null;
        }
        
        try {
        	// Threshold colors
    		hslThreshold(cameraImage, HSL_THRESHOLD_HUE, HSL_THRESHOLD_SATURATION, HSL_THRESHOLD_LUMINANCE, hslThresholdOutput);

    		// Find contours
    		findContours(hslThresholdOutput, false, findContoursOutput);
	        
	    	// If there are no contours then exit
	    	if (findContoursOutput.size() == 0) {
	    		return null;
	    	}
	    	
	    	// Filter out contours based on criteria
			ArrayList<Rect> filteredRectOutput = filterContours(cameraImage, findContoursOutput, false);

	    	// If there are no rectangles after filtering then exit
	    	if (findContoursOutput.size() == 0) {
	    		return null;
	    	}

	    	// Loop through every pair of rectangles to determine the best combo
	    	int bestTopTargetIndex = -1;
	    	int bestBotTargetIndex = -1;
	    	double bestCompositeScore = 1000;

	    	for (int outerRectIndex = 0; outerRectIndex < filteredRectOutput.size(); outerRectIndex++) {    
				Rect outerRect = filteredRectOutput.get(outerRectIndex);

				for (int innerRectIndex = outerRectIndex + 1; innerRectIndex < filteredRectOutput.size(); innerRectIndex++) {    
					Rect innerRect = filteredRectOutput.get(innerRectIndex);

					// Figure out with rect is on top
					Rect topRect = outerRect;
					Rect botRect = innerRect;
					int topRectIndex = outerRectIndex;
					int botRectIndex = innerRectIndex;
					if (outerRect.y + outerRect.height > innerRect.y + innerRect.height) {
						topRect = innerRect;
						botRect = outerRect;
						topRectIndex = innerRectIndex;
						botRectIndex = outerRectIndex;
					}

					double currentCompositeScore = getCompositeScore(topRect, botRect);
		            if (currentCompositeScore < bestCompositeScore) {
		            	bestCompositeScore = currentCompositeScore;
		            	bestTopTargetIndex = topRectIndex;
		            	bestBotTargetIndex = botRectIndex;
		            }
				}
	    	}
	    		    	
	    	// Check if a valid target was identified
	    	if (bestCompositeScore > MINIMUM_VALID_COMPOSITE_SCORE) {
		    	
	    		// If no good target pairs are found, check for a single good target
		    	bestTopTargetIndex = -1;
		    	bestBotTargetIndex = -1;
		    	bestCompositeScore = 1000;
	    		for (int rectIndex = 0; rectIndex < filteredRectOutput.size(); rectIndex++) {    
					Rect singleRect = filteredRectOutput.get(rectIndex);
					
					double currentCompositeScore = getCompositeScore(singleRect);
		            if (currentCompositeScore < bestCompositeScore) {
		            	bestCompositeScore = currentCompositeScore;
		            	bestTopTargetIndex = rectIndex;
		            }
		    	}
	    		
	    		// One last check for a valid target
		    	if (bestCompositeScore > MINIMUM_VALID_COMPOSITE_SCORE) {
		    		return null;
		    	}
	    	}
	        
	    	// Calculate the distances and angle to target
			Rect bestRect = null;
			Rect topRect = null;
			Rect botRect = null;
			
			if (bestTopTargetIndex != -1 && bestBotTargetIndex != -1) {
				topRect = filteredRectOutput.get(bestTopTargetIndex);
				botRect = filteredRectOutput.get(bestBotTargetIndex);
				
				int x = Math.min(topRect.x, botRect.x);
				int y = Math.min(topRect.y, botRect.y);
				int width = Math.max(topRect.x + topRect.width, botRect.x + botRect.width) - x;
				int height = Math.max(topRect.y + topRect.height, botRect.y + botRect.height) - y;
				bestRect = new Rect(x, y, width, height);
			}
			else if (bestTopTargetIndex != -1) {
				bestRect = filteredRectOutput.get(bestTopTargetIndex);
			}
					
			double rectTop = bestRect.y + bestRect.height;
			double rectLeft =  bestRect.x;
			double rectWidth = bestRect.width;
			double rectHeight = bestRect.height;
			int imageWidth = cameraImage.cols();
			int imageHeight = cameraImage.rows();
	               
	    	// Account for targets off center
	        double widthOffsetPixels = ((rectLeft + rectWidth / 2) - (double)imageWidth / 2);
	        double focalDistancePixels = (double)imageWidth / 2 / tanHalfFOV;
	        double offsetAngle = Math.atan(widthOffsetPixels / focalDistancePixels);
	
	        double imageWidthFt = (double)imageWidth * TARGET_WIDTH_FT / rectWidth / Math.cos(offsetAngle);
	        double cameraDistanceWidthFt = Math.cos(Math.toRadians(CAMERA_AIM_VERTICAL_ANGLE)) * imageWidthFt / 2.0 / tanHalfFOV;
	
	        // Calculate the angle from the center of the image to the selected target
	        double targetOffsetFt = imageWidthFt * widthOffsetPixels / (double)imageWidth + CAMERA_OFFSET_FT;
	        double angleToTargetDeg = Math.atan2(targetOffsetFt, cameraDistanceWidthFt) * 180.0 / Math.PI;
	
	        if (includeOverlays) {
	        	textYOffset = 0;
				DecimalFormat df = new DecimalFormat("#.00"); 
	       	 
				Imgproc.rectangle(cameraImage, 
						new Point(bestRect.x, bestRect.y), 
						new Point(bestRect.x + bestRect.width, bestRect.y + bestRect.height),
						new Scalar(0, 0, 255), 2);
				
	        	if (topRect != null) {
					Imgproc.rectangle(cameraImage, 
							new Point(topRect.x, topRect.y), 
							new Point(topRect.x + topRect.width, topRect.y + topRect.height),
							new Scalar(0, 255, 0), 2);
	        	}
				
	        	if (botRect != null) {
					Imgproc.rectangle(cameraImage, 
							new Point(botRect.x, botRect.y), 
							new Point(botRect.x + botRect.width, botRect.y + botRect.height),
							new Scalar(0, 255, 0), 2);
	        	}
				
				addTextBestRect(cameraImage, bestRect, "W: " + bestRect.width);
				addTextBestRect(cameraImage, bestRect, "H: " + bestRect.height);
				addTextBestRect(cameraImage, bestRect, "S: " + df.format(bestCompositeScore));
		        
		        int targetXCoord = (int)(rectLeft + rectWidth / 2);
		        Point startPoint = new Point(targetXCoord, 0);
		        Point endPoint = new Point(targetXCoord, imageHeight);
				Imgproc.line(cameraImage, startPoint, endPoint, new Scalar(0, 0, 255), 2);
				
		        Point centerTopPoint = new Point(imageWidth/2, 0);
		        Point centerBotPoint = new Point(imageWidth/2, imageHeight);
				Imgproc.line(cameraImage, centerTopPoint, centerBotPoint, new Scalar(0, 255, 0), 1);

				addTextTargetAngle(cameraImage, "Angle = " + df.format(angleToTargetDeg), 0);
				addTextTargetAngle(cameraImage, "Distance = " + df.format(cameraDistanceWidthFt), 20);
	        }
	                
	        return new TargetInfo(cameraDistanceWidthFt, angleToTargetDeg, bestCompositeScore);
        }
        catch (Exception e) {
        	System.err.println("An error occurred in vision processing.  Message = " + e.getMessage());
        	return null;
        }
    }
    
    private void addTextBestRect(Mat cameraImage, Rect rect, String text) {
    	Point loc = new Point(rect.x + rect.width + 10, rect.y + textYOffset); 
		Imgproc.putText(cameraImage, text, loc, Core.FONT_HERSHEY_DUPLEX, 0.75, new Scalar(0, 0, 255), 2);
		textYOffset += 20;
    }

    private void addTextTargetAngle(Mat cameraImage, String text, int yOffset) {
    	Point loc = new Point(10, 20 + yOffset); 
		Imgproc.putText(cameraImage, text, loc, Core.FONT_HERSHEY_DUPLEX, 0.75, new Scalar(0, 0, 255), 2);
    }

    private double getCompositeScore(Rect topRect, Rect botRect) {
        return Math.abs(getLeftScore(topRect, botRect) + getRightScore(topRect, botRect) + getTopHeightScore(topRect, botRect) + getBotHeightScore(topRect, botRect));
    }
    
    private double getLeftScore(Rect topRect, Rect botRect) {
        return  (double)(topRect.x - botRect.x) / topRect.width * EDGE_FACTOR;
    } 
    
    private double getRightScore(Rect topRect, Rect botRect) {
        return  (double)((topRect.x + topRect.width) - (botRect.x + botRect.width)) / topRect.width * EDGE_FACTOR;
    } 
    
    private double getTopHeightScore(Rect topRect, Rect botRect) {
    	double totalHeight = topRect.y + topRect.height - botRect.y;
        return  (double)topRect.height / totalHeight - OPTIMAL_TOP_HEIGHT * TOP_HEIGHT_FACTOR;
    } 
    
    private double getBotHeightScore(Rect topRect, Rect botRect) {
    	double totalHeight = topRect.y + topRect.height - botRect.y;
        return  (double)((topRect.y + topRect.height) - (botRect.y + botRect.height)) / totalHeight - OPTIMAL_BOT_HEIGHT * BOT_HEIGHT_FACTOR;
    } 
    
    private double getCompositeScore(Rect singleRect) {
        return Math.abs(getAspectRatioScore(singleRect.width, singleRect.height) + getWidthScore(singleRect.width) + getHeightScore(singleRect.height));
    }
    
    private double getAspectRatioScore(double width, double height) {
        return (height / width - OPTIMAL_AR) * WEIGHT_FACTOR_AR;
    }
    
    private double getWidthScore(double width) {
        return (width - OPTIMAL_WIDTH) / OPTIMAL_WIDTH * WEIGHT_FACTOR_WIDTH;
    }
    
    private double getHeightScore(double height) {
        return (height - OPTIMAL_HEIGHT) / OPTIMAL_HEIGHT * WEIGHT_FACTOR_HEIGHT;
    }
    
    private double getRectangleScore(double area, double width, double height) {
        return (area / (width * height) - OPTIMAL_RECT) * WEIGHT_FACTOR_RECT;
    } 
    
    private double getXXRatioScore(double momentXX) {
        return (momentXX - OPTIMAL_XX) * WEIGHT_FACTOR_XX;
    }
    
    private double getYYRatioScore(double momentYY) {
        return (momentYY - OPTIMAL_YY) * WEIGHT_FACTOR_YY;
    }
    
    public class TargetInfo {
    	public double distanceToTargetFt;
    	public double angleToTargetDeg;
    	public double compositeScore;
    	
    	public TargetInfo(double distanceToTargetFt, double angleToTargetDeg, double compositeScore) {
    		this.distanceToTargetFt = distanceToTargetFt;
    		this.angleToTargetDeg = angleToTargetDeg;
    		this.compositeScore = compositeScore;
    	}
    }

}
