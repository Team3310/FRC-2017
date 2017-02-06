package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeLiftResetZero extends Command
{
	public GearIntakeLiftResetZero() {
		requires(Robot.gearIntake);
	}

	protected void initialize() {
		Robot.gearIntake.setZeroLiftPosition();
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return true; 
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}