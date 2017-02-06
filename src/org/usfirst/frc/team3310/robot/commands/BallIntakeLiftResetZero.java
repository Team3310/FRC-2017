package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class BallIntakeLiftResetZero extends Command
{
	public BallIntakeLiftResetZero() {
		requires(Robot.ballIntake);
	}

	protected void initialize() {
		Robot.ballIntake.setZeroLiftPosition();
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