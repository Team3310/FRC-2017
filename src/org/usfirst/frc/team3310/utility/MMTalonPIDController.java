package org.usfirst.frc.team3310.utility;

import java.util.ArrayList;

import org.usfirst.frc.team3310.robot.subsystems.Drive;

import com.ctre.CANTalon.TalonControlMode;

public class MMTalonPIDController
{	
	protected static enum MMControlMode { STRAIGHT, TURN };
	public static enum MMTalonTurnType { TANK, LEFT_SIDE_ONLY, RIGHT_SIDE_ONLY };

	protected ArrayList<CANTalonEncoder> motorControllers;	
	protected long periodMs;
	protected PIDParams pidParams;	
	protected boolean useGyroLock;
	protected double startGyroAngle;
	protected double targetGyroAngle;
	protected double trackDistance;
	protected MMControlMode controlMode;
	protected MMTalonTurnType turnType;
	protected double targetValue;
	
	public MMTalonPIDController(long periodMs, PIDParams pidParams, ArrayList<CANTalonEncoder> motorControllers) 
	{
		this.motorControllers = motorControllers;
		this.periodMs = periodMs;
		setPID(pidParams);
	}
    
	public void setPID(PIDParams pidParams) {
		this.pidParams = pidParams;
		
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPID(pidParams.kP, pidParams.kI, pidParams.kD);
			motorController.setF(pidParams.kF);
		}
	}
	
	public void setMMStraightTarget(double startValue, double targetValue, double maxVelocity, double maxAcceleration, boolean useGyroLock, double desiredAngle, boolean resetEncoder) {
		controlMode = MMControlMode.STRAIGHT;
		this.startGyroAngle = desiredAngle;
		this.useGyroLock = useGyroLock;
		this.targetValue = targetValue;
		
		// Set up the motion profile 
		for (CANTalonEncoder motorController : motorControllers) {
			if (resetEncoder) {
				motorController.setPosition(0);
			}
			motorController.setMotionMagicCruiseVelocity(maxVelocity);
			motorController.setMotionMagicAcceleration(maxAcceleration);
			motorController.set(targetValue);
			motorController.changeControlMode(TalonControlMode.MotionMagic);
		}
	}
			
	public void setZeroPosition() {
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
			motorController.set(0);
			motorController.changeControlMode(TalonControlMode.Position);
		}
	}

	public void resetZeroPosition() {
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
		}
	}

	public void resetZeroPosition(double angle) {
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(angle);
		}
	}

	private double calcTrackDistance(double deltaAngleDeg, MMTalonTurnType turnType, double trackWidth) {
		double trackDistance = deltaAngleDeg / 360.0 * Math.PI * trackWidth;
		if (turnType == MMTalonTurnType.TANK) {
			return trackDistance;
		}
		else if (turnType == MMTalonTurnType.LEFT_SIDE_ONLY) {
			return trackDistance * 2.0;
		}
		else if (turnType == MMTalonTurnType.RIGHT_SIDE_ONLY) {
			return -trackDistance * 2.0;
		}
		return 0.0;
	}
	
	public boolean controlLoopUpdate(double currentGyroAngle) {		
		// Calculate the motion profile feed forward and gyro feedback terms
		double rightTarget = 0.0;
		double leftTarget = 0.0;

		// Update the set points 
		if (controlMode == MMControlMode.STRAIGHT) {
			double gyroDelta = useGyroLock ? startGyroAngle - currentGyroAngle: 0;
			double deltaDistance = calcTrackDistance(gyroDelta, MMTalonTurnType.TANK, Drive.TRACK_WIDTH_INCHES);
			rightTarget = targetValue + deltaDistance;
			leftTarget = targetValue - deltaDistance;
			
			// Update the controllers with updated set points.
			for (CANTalonEncoder motorController : motorControllers) {
				if (motorController.isRight()) {
					motorController.set(rightTarget);
				}
				else {
					motorController.set(leftTarget);
				}
			}
		}
		
		return false;
	}
}