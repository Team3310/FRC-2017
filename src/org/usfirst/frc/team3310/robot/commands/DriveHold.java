package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveHold extends Command
{
	private boolean status;
	
	public DriveHold(boolean status) {
		requires(Robot.drive);
		this.status = status;
	}

	@Override
	protected void initialize() {
		Robot.drive.setDriveHold(status);
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