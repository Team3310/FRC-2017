package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Drive.DriveControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightMM extends Command
{
	private double distanceInches, maxVelocityInchesPerSec, maxAccelerationInchesPerSec2, desiredAbsoluteAngle;
	private boolean useGyroLock, useAbsolute;

	public DriveStraightMM(double distanceInches, double maxVelocityInchesPerSec, double maxAccelerationInchesPerSec2, boolean useGyroLock, boolean useAbsolute, double desiredAbsoluteAngle) {
		requires(Robot.drive);
		this.distanceInches = distanceInches;
		this.maxVelocityInchesPerSec = maxVelocityInchesPerSec;
		this.maxAccelerationInchesPerSec2 = maxAccelerationInchesPerSec2;
		this.desiredAbsoluteAngle = desiredAbsoluteAngle;
		this.useGyroLock = useGyroLock;
		this.useAbsolute = useAbsolute;
	}

	protected void initialize() {
		Robot.drive.setStraightMM(distanceInches, maxVelocityInchesPerSec, maxAccelerationInchesPerSec2, useGyroLock, useAbsolute, desiredAbsoluteAngle);
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