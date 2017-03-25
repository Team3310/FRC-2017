package org.usfirst.frc.team3310.utility;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import org.usfirst.frc.team3310.robot.subsystems.Drive;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;

public class PathfinderTest {
    public static void main(String[] args) {
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_LOW, 0.0001, 120, 200.0, 700.0);
        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(72, 10, Pathfinder.d2r(15)),
                new Waypoint(144, 36, Pathfinder.d2r(30)),
        };

        Trajectory trajectory = Pathfinder.generate(points, config);
        Segment[] centerSegments = trajectory.segments;
        
        TankModifier2 modifier = new TankModifier2();
        ArrayList<Segment[]> leftRight = modifier.modify(centerSegments, Drive.TRACK_WIDTH_INCHES);

        Segment[] rightSegments = leftRight.get(1);
        Segment[] leftSegments = leftRight.get(0);
        
        Pathfinder.writeToCSV(new File("center.csv"), trajectory);
//        Pathfinder.writeToCSV(new File("left.csv"), left);
//        Pathfinder.writeToCSV(new File("right.csv"), right);
        System.out.println("i,center.position,center.velocity,center.acceleration,center.heading,right.position,right.velocity,right.acceleration,left.position,left.velocity,left.acceleration" );
        for (int i = 0; i < rightSegments.length; i++) {
        	System.out.println(i + "," + centerSegments[i].position + "," + centerSegments[i].velocity + "," + centerSegments[i].acceleration + "," + Math.toDegrees(centerSegments[i].heading) + "," + rightSegments[i].position + "," + rightSegments[i].velocity + "," + rightSegments[i].acceleration+ "," + leftSegments[i].position + "," + leftSegments[i].velocity + "," + leftSegments[i].acceleration );
        }
        
        double[] centerX = new double[centerSegments.length];
        double[] centerY = new double[centerSegments.length];
        double[] centerAccel = new double[centerSegments.length];
        double[] centerVelocity = new double[centerSegments.length];
        double[] centerPosition = new double[centerSegments.length];
        double[] leftAccel = new double[leftSegments.length];
        double[] leftVelocity = new double[leftSegments.length];
        double[] leftPosition = new double[leftSegments.length];
        double[] rightAccel = new double[rightSegments.length];
        double[] rightVelocity = new double[rightSegments.length];
        double[] rightPosition = new double[rightSegments.length];
        for (int i = 0; i < centerSegments.length; i++) {
        	centerX[i] = centerSegments[i].x;
        	centerY[i] = centerSegments[i].y;
        	centerAccel[i] = centerSegments[i].acceleration;
        	centerVelocity[i] = centerSegments[i].velocity;
        	centerPosition[i] = centerSegments[i].position;
        	leftAccel[i] = leftSegments[i].acceleration;
        	leftVelocity[i] = leftSegments[i].velocity;
        	leftPosition[i] = leftSegments[i].position;
        	rightAccel[i] = rightSegments[i].acceleration;
        	rightVelocity[i] = rightSegments[i].velocity;
        	rightPosition[i] = rightSegments[i].position;
        }
        
		FalconLinePlot fig0 = new FalconLinePlot(centerX, centerY);
		fig0.yGridOn();
		fig0.xGridOn();
		fig0.setYLabel("Y (in)");
		fig0.setXLabel("X (in)");
		fig0.setTitle("Position Profile XY");

		FalconLinePlot fig1 = new FalconLinePlot(centerPosition);
		fig1.yGridOn();
		fig1.xGridOn();
		fig1.setYLabel("Position (in)");
		fig1.setXLabel("time (seconds)");
		fig1.setTitle("Position Profile for Left and Right Wheels \n Left = Blue, Right = Magenta");
		fig1.addData(rightPosition, Color.magenta);
		fig1.addData(leftPosition, Color.blue);

		FalconLinePlot fig2 = new FalconLinePlot(centerVelocity, Color.green, Color.green);
		fig2.yGridOn();
		fig2.xGridOn();
		fig2.setYLabel("Velocity (in/sec)");
		fig2.setXLabel("time (seconds)");
		fig2.setTitle("Velocity Profile for Left and Right Wheels \n Left = Blue, Right = Magenta");
		fig2.addData(rightVelocity, Color.magenta);
		fig2.addData(leftVelocity, Color.blue);

		FalconLinePlot fig3 = new FalconLinePlot(centerAccel, Color.green, Color.green);
		fig3.yGridOn();
		fig3.xGridOn();
		fig3.setYLabel("Accel (in/sec^2)");
		fig3.setXLabel("time (seconds)");
		fig3.setTitle("Accel Profile for Left and Right Wheels \n Left = Blue, Right = Magenta");
		fig3.addData(rightAccel, Color.magenta);
		fig3.addData(leftAccel, Color.blue);

    }

}
