package org.usfirst.frc.team3310.vision;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * @author rhhs
 * 
 * This is a helper class to describe the details of a target.  Based on the camera
 * field of view (FOV), image width in pixels, and a known target size in ft, it
 * calculates the distance from the camera focal point to the target and the angle
 * from the center of image to the selected target.  It is set up to use the target
 * width but could also calculate the distance based on the target/image height.
 * 
 */
public class Target {
    
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int TOP = 3;
    
    public static final String[] LOCATION_NAMES = {"Left", "Right", "Bottom", "Top"};
    
    public static final double TARGET_HEIGHT_FT = 1.5;
    public static final double TARGET_WIDTH_FT = 2.0;
    public static final double TARGET_ASPECT_RATIO = TARGET_HEIGHT_FT / TARGET_WIDTH_FT;
    public static final double TARGET_RIGHT_LEFT_OFFSET_FT = 27.375 / 12;
    public static final double OVERALL_TARGET_HEIGHT_FT = 88.0 / 12.0;
    public static final double OVERALL_TARGET_WIDTH_FT = 78.75 / 12.0;
        
    /* 
     * This aray contains the x-distance from one target to the other targets.  It is used 
     * to calculate angle from the "best" identified target and the target we are shooting at.
     * 
     * Left target to Left, Right, Bottom, Top
     * Right target to Left, Right, Bottom, Top
     * Bottom target to Left, Right, Bottom, Top
     * Top target to Left, Right, Bottom, Top
     */
    public static final double[] TARGET_OFFSETS_FT = {
                0, TARGET_RIGHT_LEFT_OFFSET_FT * 2, TARGET_RIGHT_LEFT_OFFSET_FT, TARGET_RIGHT_LEFT_OFFSET_FT,
                -TARGET_RIGHT_LEFT_OFFSET_FT * 2, 0, -TARGET_RIGHT_LEFT_OFFSET_FT, -TARGET_RIGHT_LEFT_OFFSET_FT,
                -TARGET_RIGHT_LEFT_OFFSET_FT, TARGET_RIGHT_LEFT_OFFSET_FT, 0, 0,
                -TARGET_RIGHT_LEFT_OFFSET_FT, TARGET_RIGHT_LEFT_OFFSET_FT, 0, 0};
    
    private static final double VALID_COMPOSITE_SCORE_MAX = 10;

    private double m_cameraFOVHorizontalAngleDeg;  // M206 54 deg actual
    
    private double m_cameraDistanceFt;
    private double m_horizontalAngleToSelectedTargetDeg;
    private double m_xCenterSelectedTargetPixels;
    
    private int m_targetLocation = TOP;
    private int m_selectedTargetLocation = TOP;
    
    private boolean m_calculationsPerformed = false;

    private ParticleAnalysisReport m_report;
    
    public Target(ParticleAnalysisReport report, double cameraFOVHorizontalAngleDeg) {
        this.m_cameraFOVHorizontalAngleDeg = cameraFOVHorizontalAngleDeg;       
        this.m_report = report;
    }
    
    public ParticleAnalysisReport getReport() {
        return m_report;
    }
    
    public double getRectangleScore() {
        return m_report.particleArea / ((double)m_report.boundingRectWidth * (double)m_report.boundingRectHeight) * 100;
    } 
    
    public double getAspectRatioScore() {
        return (double)m_report.boundingRectWidth / (double)m_report.boundingRectHeight * 100 * TARGET_ASPECT_RATIO;
    }
    
    public double getCompositeScore() {
        return Math.abs(getRectangleScore() - 100) + Math.abs(getAspectRatioScore() - 100);
    }
    
    public boolean isValid() {
//        double ar = getAspectRatioScore();
//        double rect = getRectangleScore();
//        double composite = getCompositeScore();
        return (getCompositeScore() < VALID_COMPOSITE_SCORE_MAX);
    }
    
    public void setTargetLocations(int thisTargetLocation, int selectedTargetLocation) {
        this.m_targetLocation = thisTargetLocation;
        this.m_selectedTargetLocation = selectedTargetLocation;
    }
    
    public double getCameraDistanceFt() {
        updateCalculations();
        return m_cameraDistanceFt;
    }
    
    public double getHorizontalAngleToSelectedTargetDeg() {
        updateCalculations();
        return m_horizontalAngleToSelectedTargetDeg;
    }
    
    public double getXCenterSelectedTargetPixels() {
        updateCalculations();
        return m_xCenterSelectedTargetPixels;
    }
    
    public int getLocation() {
        return m_targetLocation;
    }
    
    private void updateCalculations() {
        if (m_calculationsPerformed == true) {
            return;
        }
        double tanHalfFOV = Math.tan(Math.toRadians(m_cameraFOVHorizontalAngleDeg / 2));

        // Account for targets off center
        double focalDistancePixels = (double)m_report.imageWidth / 2 / tanHalfFOV;
        double targetCenterPixels = (double)m_report.boundingRectLeft + (double)m_report.boundingRectWidth / 2;
        double targetOffsetPixels =  targetCenterPixels - (double)m_report.imageWidth / 2;
        double offsetAngle = Math.atan(targetOffsetPixels / focalDistancePixels);

        // Calculate the distance from the camera focal point to the target
        double imageWidthFt = Math.cos(offsetAngle) * (double)m_report.imageWidth * TARGET_WIDTH_FT / (double)m_report.boundingRectWidth;
        m_cameraDistanceFt = imageWidthFt / 2.0 / tanHalfFOV;

        // Calculate the angle from the center of the image to the selected target
        double targetOffsetFt = imageWidthFt * targetOffsetPixels / (double)m_report.imageWidth;
        double offsetThisTargetToSelectedTargetFt = getTargetOffset();
        double centerOffsetFt = targetOffsetFt + offsetThisTargetToSelectedTargetFt;
        m_xCenterSelectedTargetPixels = targetCenterPixels + (double)m_report.boundingRectWidth * offsetThisTargetToSelectedTargetFt / imageWidthFt;
        m_horizontalAngleToSelectedTargetDeg = Math.atan2(centerOffsetFt, m_cameraDistanceFt) * 180.0 / Math.PI;
        
        m_calculationsPerformed = true;
    }
    
    private double getTargetOffset() {
            return TARGET_OFFSETS_FT[m_targetLocation * 4 + m_selectedTargetLocation];
    }
}
