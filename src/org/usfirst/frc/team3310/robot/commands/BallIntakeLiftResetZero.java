package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class BallIntakeLiftResetZero extends Command
{
	private double angle;
	
	public BallIntakeLiftResetZero(double angle) {
		this.angle = angle;
		requires(Robot.ballIntake);
	}

	protected void initialize() {
		Robot.ballIntake.setZeroLiftPosition(angle);
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