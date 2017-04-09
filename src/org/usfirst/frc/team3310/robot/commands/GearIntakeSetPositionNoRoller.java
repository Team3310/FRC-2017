package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeSetPositionNoRoller extends Command
{
	private IntakePosition position;
	
	public GearIntakeSetPositionNoRoller(IntakePosition position) {
		requires(Robot.gearIntake);
		this.position = position;
	}

	@Override
	protected void initialize() {
		Robot.gearIntake.setLiftPosition(position);
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