package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveMPCancel extends Command
{
	public DriveMPCancel() {
		requires(Robot.drive);
	}
	
	@Override
	protected void initialize() {
		Robot.drive.setFinished(true);
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