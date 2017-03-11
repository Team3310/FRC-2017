package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class BallIntakeManualStart extends Command
{	
	public BallIntakeManualStart() {
	}

	@Override
	protected void initialize() {
		Robot.ballIntake.setManualJoyStickMode();
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