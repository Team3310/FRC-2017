package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.HopperState;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterSetHopperPosition extends Command
{
	private HopperState state;
	
	public ShooterSetHopperPosition(HopperState state) {
		requires(Robot.shooter);
		this.state = state;
	}

	@Override
	protected void initialize() {
		Robot.shooter.setHopperPosition(state);
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