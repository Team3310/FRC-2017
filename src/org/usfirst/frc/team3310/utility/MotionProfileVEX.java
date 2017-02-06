package org.usfirst.frc.team3310.utility;

public class MotionProfileVEX
{
	public MotionProfileVEX() {
	} 
	
	public void moveStraightMP(double targetDistanceInches, double maxVelocityInchesPerSecond) {
		calculateMP(targetDistanceInches, maxVelocityInchesPerSecond, true);
	}
	
	public void turnGyroMP(double targetAngleDegrees, double maxTurnRateDegPerSecond) {
		calculateMP(targetAngleDegrees, maxTurnRateDegPerSecond, false);
	}
	
	public void calculateMP(double targetPositionInput, double maxVelocityInput, boolean isStraight) {
		double startPosition = 0;  						// any distance unit
		double targetPosition = targetPositionInput;  	// any distance unit
		double maxVelocity = maxVelocityInput;			// velocity unit consistent with targetPosition

		// Accel profile
		//
		//  0 T2   T1
		//  | |    |
		//     _____
		//    /     \
		//   /       \___
		//               \      /
		//                \____/   
		
		
		double t1 = 400;	// time when accel = max accel.  millisecond 
		double t2 = 300; 	// time when accel starts back to 0.  millisecond 
		double itp = 10;  	// time between points millisecond
		
		double t4;
		int numFilter1Boxes;
		int numFilter2Boxes;
		int numPoints;
		
		int numITP;
		double filter1;
		double filter2;
		double previousPosition;
		double previousVelocity;
		double deltaFilter1;
		double[] filter2Window;
		int windowIndex;
		int pointIndex;
		
		// t4 is the time in ms it takes to get to the end point when at max velocity
		t4 = Math.abs((targetPosition - startPosition)/maxVelocity) * 1000;
		
		// We need to make t4 an even multiple of itp
		t4 = (int)(itp * Math.ceil(t4/itp));
		
		// In the case where t4 is less than the accel times, we need to adjust the
		// accel times down so the filters works out.  Lots of ways to do this but
		// to keep things simple we will make t4 slightly larger than the sum of 
		// the accel times.
		if (t4 < t1 + t2) {
			double total = t1 + t2 + t4;
			double t1t2Ratio = t1/t2;
			double t2Adjusted = Math.floor(total / 2 / (1 + t1t2Ratio) / itp);
			if (t2Adjusted % 2 != 0) {
				t2Adjusted -= 1;
			}
			t2 = t2Adjusted * itp;
			t1 = t2 * t1t2Ratio;
			t4 = total - t1 - t2;
		}
		
		// Adjust max velocity so that the end point works out to the correct position.
		maxVelocity = Math.abs((targetPosition - startPosition) / t4) * 1000;

		numFilter1Boxes = (int)Math.ceil(t1/itp);
		numFilter2Boxes = (int)Math.ceil(t2/itp);
		numPoints = (int)Math.ceil(t4/itp);

		numITP = numPoints + numFilter1Boxes + numFilter2Boxes;
		filter1 = 0;
		filter2 = 0;
		previousVelocity = 0;
		previousPosition = startPosition;
		deltaFilter1 = 1.0/numFilter1Boxes;
		filter2Window = new double[numFilter2Boxes];
		windowIndex = 0;
		pointIndex = 0;
		if (startPosition > targetPosition && maxVelocity > 0) {
			maxVelocity = -maxVelocity;
		}
		
		double time = 0;
		double position = 0;
		double velocity = 0;
		double acceleration = 0;
		
		// First point
		if (isStraight) {
			updateStraightController(time, position, velocity, acceleration);
		}
		else {
			updateTurnController(time, position, velocity, acceleration);
		}
		pointIndex++;
		
		// Start looping through the points
		while (pointIndex <= numITP) {			
			int input = (pointIndex - 1) < numPoints ? 1 : 0;		
			if (input > 0) {
				filter1 = Math.min(1, filter1 + deltaFilter1);
			}
			else {
				filter1 = Math.max(0, filter1 - deltaFilter1);				
			}
			
			double firstFilter1InWindow = filter2Window[windowIndex];
			if (pointIndex <= numFilter2Boxes) {
				firstFilter1InWindow = 0;
			}
			filter2Window[windowIndex] = filter1;
			
			filter2 += (filter1 - firstFilter1InWindow) / numFilter2Boxes;
			
			time = pointIndex * itp / 1000.0;
			velocity = filter2 * maxVelocity;
			position = previousPosition + (velocity + previousVelocity) /  2 * itp / 1000;
			acceleration = (velocity - previousVelocity) / itp * 1000;
						
			previousVelocity = velocity;
			previousPosition = position;
			windowIndex++;
			if (windowIndex == numFilter2Boxes) {
				windowIndex = 0;
			}			

			if (isStraight) {
				updateStraightController(time, position, velocity, acceleration);
			}
			else {
				updateTurnController(time, position, velocity, acceleration);
			}
			
			pointIndex++;
		}
	}
	
	private void updateStraightController(double time, double position, double velocity, double acceleration) {
		System.out.println(time + ", " + position + ", " + velocity + ", " + acceleration);	
	}

	private void updateTurnController(double time, double position, double velocity, double acceleration) {
		System.out.println(time + ", " + position + ", " + velocity + ", " + acceleration);	
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		MotionProfileVEX mp = new MotionProfileVEX();
		System.out.println("Time, Position, Velocity, Acceleration");
		
//		mp.moveStraightMP(24,  24);
		mp.turnGyroMP(-90,  90);
		
		long deltaTime = System.nanoTime() - startTime;
		System.out.println("Time Box Car = " + (double)deltaTime * 1E-6 + " ms");
	}
}