package org.usfirst.frc.team3310.vision;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * @author rhhs
 * 
 * This class takes the identified targets and tries to which position
 * they are and which one is the "best"
 */
public class TargetSelector {
   
    private static final double ASPECT_RATIO_THRESHOLD = 60;
    
    private Target[] m_targets;
    private Target m_bestTarget = null;
    private int m_selectedTarget = 0;
    
    public TargetSelector(ParticleAnalysisReport[] reports, int selectedTarget, double cameraFOVHorizontalAngleDeg) {
        m_selectedTarget = selectedTarget;
        m_targets = new Target[reports.length];
        for (int i = 0; i < reports.length; i++) {
            m_targets[i] = new Target(reports[i], cameraFOVHorizontalAngleDeg);
        }
        evaluateTargets();
    }

    // Find the "best" target and identify the location of each target
    private void evaluateTargets() { 
        
        int minX = 1000;
        int maxX = 0;
        int minY = 1000;
        int maxY = 0;

        int leftIndex = -1;
        int rightIndex = -1;
        int topIndex = -1;
        int bottomIndex = -1;

        int numValidTargets = 0;
        for (int i = 0; i < m_targets.length; i++) {           
            Target curTarget = m_targets[i];

            // The "best" target is based on the composite score.  We may want to include a term
            // in the composite score that includes the distance from the current target to the
            // selected target.
            if (curTarget.isValid()) {
                if (m_bestTarget == null || (curTarget.getCompositeScore() < m_bestTarget.getCompositeScore())) {
                    m_bestTarget = m_targets[i];
                }
                numValidTargets++;
            }

            ParticleAnalysisReport report = curTarget.getReport();
            if (report.boundingRectLeft < minX) {
                minX = report.boundingRectLeft;
                if (curTarget.getAspectRatioScore() > ASPECT_RATIO_THRESHOLD) {
                    leftIndex = i;
                }
            }
            if (report.boundingRectLeft + report.boundingRectWidth > maxX) {
                maxX = report.boundingRectLeft + report.boundingRectWidth;
                if (curTarget.getAspectRatioScore() > ASPECT_RATIO_THRESHOLD) {
                    rightIndex = i;
                }
            }
            if (report.boundingRectTop < minY) {
                minY = report.boundingRectTop;
                topIndex = i;
            }
            if (report.boundingRectTop + report.boundingRectHeight > maxY) {
                maxY = report.boundingRectTop + report.boundingRectHeight;
                bottomIndex = i;
            }
        }
 
        // Check for only one target (it must be either right or left 
        // assuming top AND bottom are not obscured or filtered out).
        if (numValidTargets == 1) {
            leftIndex = -1;
            rightIndex = -1;
            topIndex = -1;
            bottomIndex = -1;

            for (int i = 0; i < m_targets.length; i++) {
                if (m_targets[i].isValid()) {
                    ParticleAnalysisReport report = m_targets[i].getReport();
                    if (report.boundingRectLeft + report.boundingRectWidth / 2 < report.imageWidth / 2) {
                        rightIndex = 0;
                    } else {
                        leftIndex = 0;
                    }
                    break;
                }
            }
        }

        // Set the locations of the targets
        if (rightIndex != -1) {
            m_targets[rightIndex].setTargetLocations(Target.RIGHT, m_selectedTarget);
        }
        if (leftIndex != -1) {
            m_targets[leftIndex].setTargetLocations(Target.LEFT, m_selectedTarget);
        }
        if (topIndex != -1) {
            m_targets[topIndex].setTargetLocations(Target.TOP, m_selectedTarget);
        }
        if (bottomIndex != -1) {
            m_targets[bottomIndex].setTargetLocations(Target.BOTTOM, m_selectedTarget);
        }
    }
 
    public int getSelectedTarget() {
        return m_selectedTarget;
    }
    
    public int getBestTarget() {
        if (m_bestTarget != null) {
            return m_bestTarget.getLocation();
        }
        return -1;
    }
    
    public double getBestTargetCameraDistanceFt() {
        if (m_bestTarget != null) {
            return m_bestTarget.getCameraDistanceFt();
        }
        
        return 0;
    }
    
    public double getAverageTargetCameraDistanceFt() {
        double cameraDistance = 0;
        int numValidTargets = 0;
        for (int i = 0; i < m_targets.length; i++) {
            if (m_targets[i].isValid()) {
                cameraDistance += m_targets[i].getCameraDistanceFt();
                numValidTargets++;
            }
        }
        if (numValidTargets > 0) {
            return cameraDistance / (double)numValidTargets;
        }
        
        return 0;
    }
    
    public double getBestHorizontalAngleToSelectedTargetDeg() {
        if (m_bestTarget != null) {
            return m_bestTarget.getHorizontalAngleToSelectedTargetDeg();
        }
        
        return 0;
    }
    
    public double getAverageHorizontalAngleToSelectedTargetDeg() {
        double horizontalAngle = 0;
        int numValidTargets = 0;
        for (int i = 0; i < m_targets.length; i++) {
            if (m_targets[i].isValid()) {
                horizontalAngle += m_targets[i].getHorizontalAngleToSelectedTargetDeg();
                numValidTargets++;
            }
        }
        if (numValidTargets > 0) {
            return horizontalAngle / (double)numValidTargets;
        }
        
        return 0;
    }

}
