package org.usfirst.frc.team3310.utility;

import org.usfirst.frc.team3310.robot.subsystems.Drive;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

public class PathfinderTest {
    public static void main(String[] args) {
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_LOW, 0.01, 120, 200.0, 700.0);
        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(48, 12, Pathfinder.d2r(20)),
                new Waypoint(96, 24, 0),
        };

        Trajectory trajectory = Pathfinder.generate(points, config);

        TankModifier modifier = new TankModifier(trajectory).modify(Drive.TRACK_WIDTH_INCHES);

        // Do something with the new Trajectories...
        Trajectory left = modifier.getLeftTrajectory();
        Trajectory right = modifier.getRightTrajectory();

        Segment[] centerSegments = trajectory.segments;
        Segment[] rightSegments = right.segments;
        Segment[] leftSegments = left.segments;
    	System.out.println("i,center.position,center.velocity,center.acceleration,center.heading,right.position,right.velocity,right.acceleration,left.position,left.velocity,left.acceleration" );
        for (int i = 0; i < right.segments.length; i++) {
        	System.out.println(i + "," + centerSegments[i].position + "," + centerSegments[i].velocity + "," + centerSegments[i].acceleration + "," + Math.toDegrees(centerSegments[i].heading) + "," + rightSegments[i].position + "," + rightSegments[i].velocity + "," + rightSegments[i].acceleration+ "," + leftSegments[i].position + "," + leftSegments[i].velocity + "," + leftSegments[i].acceleration );
        }
    }

}
