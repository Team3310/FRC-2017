package org.usfirst.frc.team3310.utility;

public class MotionProfileBoxCarArray
{
	static double DEFAULT_T1 = 200;	// millisecond
	static double DEFAULT_T2 = 100; // millisecond
	
	private double targetDistance;  // any distance unit
	private double maxVelocity;		// velocity unit consistent with targetDistance
	private double itp;	  			// millisecond
	private double t1 = DEFAULT_T1;
	private double t2 = DEFAULT_T2;
	
	public MotionProfileBoxCarArray(double targetDistance, double maxVelocity, double itp) {
		this.targetDistance = targetDistance;
		this.maxVelocity = maxVelocity;
		this.itp = itp;
	}
	
	public MotionProfilePoint[] getProfile() {
		double t4 = targetDistance/maxVelocity * 1000;
		int numFilter1Boxes = (int)Math.ceil(t1/itp);
		int numFilter2Boxes = (int)Math.ceil(t2/itp);
		int numPoints = (int)(t4/itp);
		
		int numITP = numPoints + numFilter1Boxes + numFilter2Boxes;
		double filter1 = 0;
		double filter2 = 0;
		double previousVelocity = 0;
		double previousDistance = 0;
		double deltaFilter1 = 1.0/numFilter1Boxes;
		double[] filter2Window = new double[numFilter2Boxes];
		int windowIndex = 0;
		MotionProfilePoint[] points = new MotionProfilePoint[numITP + 1];
		points[0] = new MotionProfilePoint();

		for (int i = 0; i < numITP; i++) {
			int input = i < numPoints ? 1 : 0;		
			if (input > 0) {
				filter1 = Math.min(1, filter1 + deltaFilter1);
			}
			else {
				filter1 = Math.max(0, filter1 - deltaFilter1);				
			}
			
			double firstFilter1InWindow = filter2Window[windowIndex];
			if (i < numFilter2Boxes) {
				firstFilter1InWindow = 0;
			}
			filter2Window[windowIndex] = filter1;
			filter2 += (filter1 - firstFilter1InWindow) / numFilter2Boxes;
			
			MotionProfilePoint currentPoint = new MotionProfilePoint(); 
			points[i+1] = currentPoint;
			currentPoint.time = (i + 1) * itp / 1000.0;
			currentPoint.velocity = filter2 * maxVelocity;
			currentPoint.position = previousDistance + (currentPoint.velocity + previousVelocity) /  2 * itp / 1000;
			currentPoint.acceleration = (currentPoint.velocity - previousVelocity) / itp * 1000;
						
			previousDistance = currentPoint.position;
			previousVelocity = currentPoint.velocity;
			windowIndex++;
			if (windowIndex == numFilter2Boxes) {
				windowIndex = 0;
			}	
		}
		
		return points;
	}

	public double getTargetDistance() {
		return targetDistance;
	}

	public double getMaxVelocity() {
		return maxVelocity;
	}

	public double getItp() {
		return itp;
	}

	public double getT1() {
		return t1;
	}

	public void setT1(double t1) {
		this.t1 = t1;
	}

	public double getT2() {
		return t2;
	}

	public void setT2(double t2) {
		this.t2 = t2;
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
//		MotionProfileBoxCarArray mp = new MotionProfileBoxCarArray(4, 10, 10);
//		MotionProfilePoint[] points = mp.getProfile();

//		System.out.println("Time, Position, Velocity, Acceleration");
//		for (int i = 0; i < points.length; i++) {
//			MotionProfilePoint point = points[i];
//			System.out.println(point.time + ", " + point.position + ", " + point.velocity + ", " + point.acceleration);
//		}
		
		long deltaTime = System.nanoTime() - startTime;
		System.out.println("Time Box Car Array = " + (double)deltaTime * 1E-6 + " ms");
	}

}
