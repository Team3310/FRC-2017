package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStopOnHopperSensor extends Command
{	
	public DriveStopOnHopperSensor() {
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return Robot.drive.isHopperSensorRedOn();
	}

	@Override
	protected void end() {
		Robot.drive.setFinished(true);
	}

	@Override
	protected void interrupted() {
			
	}
}