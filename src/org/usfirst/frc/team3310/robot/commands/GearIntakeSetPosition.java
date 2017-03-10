package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeSetPosition extends Command
{
	private IntakePosition position;
	
	public GearIntakeSetPosition(IntakePosition position) {
		requires(Robot.gearIntake);
		this.position = position;
	}

	@Override
	protected void initialize() {
		if (position == IntakePosition.GEAR_PRESENT) {
			setTimeout(0.3);
		}
		else {
			Robot.gearIntake.setLiftPosition(position);
		}
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		if (position == IntakePosition.GEAR_PRESENT) {
			if (isTimedOut()) {
				Robot.gearIntake.setLiftPosition(position);
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
			
	}
}