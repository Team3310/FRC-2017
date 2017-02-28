package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterSetShotPosition extends Command
{
	private ShotState state;
	
	public ShooterSetShotPosition(ShotState state) {
		requires(Robot.shooter);
		this.state = state;
	}

	@Override
	protected void initialize() {
		Robot.shooter.setShotPosition(state);
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