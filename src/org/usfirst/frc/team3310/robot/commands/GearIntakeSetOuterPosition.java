package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake.GearPositionState;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeSetOuterPosition extends Command
{
	private GearPositionState position;
	
	public GearIntakeSetOuterPosition(GearPositionState position) {
		requires(Robot.gearIntake);
		this.position = position;
	}

	@Override
	protected void initialize() {
		Robot.gearIntake.setGearOuterPosition(position);
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