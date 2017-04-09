package org.usfirst.frc.team3310.utility;

import java.util.ArrayList;

import com.ctre.CANTalon.TalonControlMode;

import jaci.pathfinder.Trajectory.Segment;

public class MPTalonPIDPathVelocityController
{	
	protected ArrayList<CANTalonEncoder> motorControllers;	
	protected long periodMs;
	protected PIDParams pidParams;	
	protected PathGenerator path;
	protected double startGyroAngle;
	protected double targetGyroAngle;
	protected double trackDistance;
	
	public MPTalonPIDPathVelocityController(long periodMs, PIDParams pidParams, ArrayList<CANTalonEncoder> motorControllers) 
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
			motorController.configNominalOutputVoltage(+0.0f, -0.0f);
			motorController.configPeakOutputVoltage(+12.0f, -12.0f);
			motorController.setProfile(0);
		}
	}
	
	public void setMPPathTarget(PathGenerator path) {
		this.path = path;
		path.resetCounter();
		
		// Set up the motion profile 
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
			motorController.set(0);
			motorController.changeControlMode(TalonControlMode.Speed);
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

	public boolean controlLoopUpdate(double currentGyroAngle) {
		Segment leftPoint = path.getLeftPoint();
		Segment rightPoint = path.getRightPoint();
		
		// Check if we are finished
		if (leftPoint == null) {
			return true;
		}
		
		// Calculate the motion profile feed forward and gyro feedback terms
		double rightRevs = rightPoint.velocity * 100;
		double leftRevs = leftPoint.velocity * 100;
		
		// Update the controllers Kf and set point.
		for (CANTalonEncoder motorController : motorControllers) {
			if (motorController.isRight()) {
				motorController.set(rightRevs);
			}
			else {
				motorController.set(leftRevs);
			}
		}
		
		path.incrementCounter();
		
		return false;
	}
}