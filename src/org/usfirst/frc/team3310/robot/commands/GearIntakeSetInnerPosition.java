package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake.GearPositionState;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeSetInnerPosition extends Command
{
	private GearPositionState position;
	
	public GearIntakeSetInnerPosition(GearPositionState position) {
//		requires(Robot.gearIntake);
		this.position = position;
	}

	@Override
	protected void initialize() {
		Robot.gearIntake.setGearInnerPosition(position);
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