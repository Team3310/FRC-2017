package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class BallIntakeIncrementOffset extends Command
{
	private double deltaAngle;
	
	public BallIntakeIncrementOffset(double deltaAngle) {
		requires(Robot.ballIntake);
		this.deltaAngle = deltaAngle;
	}

	@Override
	protected void initialize() {
		Robot.ballIntake.incrementOffsetAngle(deltaAngle);
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
			
	}
}