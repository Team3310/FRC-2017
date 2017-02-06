package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveGyroLock extends Command
{
	private boolean useLock, snapToAbsolute0or180;
	
	public DriveGyroLock(boolean useLock, boolean snapToAbsolute0or180) {
		requires(Robot.drive);
		this.useLock = useLock;
		this.snapToAbsolute0or180 = snapToAbsolute0or180;
	}

	@Override
	protected void initialize() {
		Robot.drive.setGyroLock(useLock, snapToAbsolute0or180);;
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