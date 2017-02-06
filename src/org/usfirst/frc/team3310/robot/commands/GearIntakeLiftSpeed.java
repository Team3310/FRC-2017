package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeLiftSpeed extends Command
{
	private double speed;
	
	public GearIntakeLiftSpeed(double speed) {
		requires(Robot.gearIntake);
		this.speed = speed;
	}

	@Override
	protected void initialize() {
		Robot.gearIntake.setLiftSpeed(speed);
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