package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Drive.DriveControlMode;
import org.usfirst.frc.team3310.utility.MotionProfileCache;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightMPCached extends Command
{
	private String key;
	private double desiredAbsoluteAngle;
	private boolean useGyroLock, useAbsolute;

	public DriveStraightMPCached(double distanceInches, double maxVelocityInchesPerSec, boolean useGyroLock, boolean useAbsolute, double desiredAbsoluteAngle) {
		requires(Robot.drive);

		this.desiredAbsoluteAngle = desiredAbsoluteAngle;
		this.useGyroLock = useGyroLock;
		this.useAbsolute = useAbsolute;
		
		MotionProfileCache cache = MotionProfileCache.getInstance();
		key = cache.addMP(0, distanceInches, maxVelocityInchesPerSec, Robot.drive.getPeriodMs(), Drive.MP_STRAIGHT_T1, Drive.MP_STRAIGHT_T2);
	}

	protected void initialize() {
		Robot.drive.setStraightMPCached(key, useGyroLock, useAbsolute, desiredAbsoluteAngle);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return Robot.drive.isFinished(); 
	}

	protected void end() {
		Robot.drive.setControlMode(DriveControlMode.JOYSTICK);
	}

	protected void interrupted() {
		end();
	}
}