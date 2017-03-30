package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;

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
		Robot.gearIntake.setLiftPosition(position);
		if (position == IntakePosition.GEAR_INTAKE) {
			Robot.gearIntake.setRollerSpeed(GearIntake.GEAR_INTAKE_LOAD_SPEED);
		}
		else if (position == IntakePosition.GEAR_PRESENT) {
			Robot.gearIntake.setRollerSpeed(0.0);
		}
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