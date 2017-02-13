package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Climber.DoorOpenState;

import edu.wpi.first.wpilibj.command.Command;

public class ClimberDoorPosition extends Command
{
	private DoorOpenState state;
	
	public ClimberDoorPosition(DoorOpenState state) {
		requires(Robot.climber);
		this.state = state;
	}

	@Override
	protected void initialize() {
		Robot.climber.setDoorPosition(state);
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