package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveGyroOffset extends Command
{
	private double offsetDeg;
	
	public DriveGyroOffset(double offsetDeg) {
		requires(Robot.drive);
		this.offsetDeg = offsetDeg;
	}

	@Override
	protected void initialize() {
		Robot.drive.setGyroOffset(offsetDeg);
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