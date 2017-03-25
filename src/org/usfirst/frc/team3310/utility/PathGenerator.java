package org.usfirst.frc.team3310.utility;

import org.usfirst.frc.team3310.robot.subsystems.Drive;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

public class PathGenerator {
	
	private Segment[] centerPoints;
	private Segment[] leftPoints;
	private Segment[] rightPoints;
	private int curIndex = 0;
	
	public PathGenerator(Waypoint[] points, double timeStep, double maxVelocity, double maxAccel, double maxJerk) {
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_LOW, timeStep, maxVelocity, maxAccel, maxJerk);
        Trajectory trajectory = Pathfinder.generate(points, config);
        TankModifier modifier = new TankModifier(trajectory).modify( Drive.TRACK_WIDTH_INCHES);
        centerPoints = trajectory.segments;
        leftPoints = modifier.getLeftTrajectory().segments;
        rightPoints = modifier.getRightTrajectory().segments;
	}
	
	public Segment getLeftPoint() {
		return (curIndex < leftPoints.length) ? leftPoints[curIndex] : null;
	}
	
	public Segment getRightPoint() {
		return (curIndex < rightPoints.length) ? rightPoints[curIndex] : null;
	}
	
	public Double getHeading() {
		return (curIndex < centerPoints.length) ? centerPoints[curIndex].heading : null;
	}
	
	public void incrementCounter() {
		curIndex++;
	}
}
