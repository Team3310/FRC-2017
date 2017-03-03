package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeLiftMoveMP extends Command
{
	private IntakePosition position;
	
	public GearIntakeLiftMoveMP(IntakePosition position) {
		requires(Robot.gearIntake);
		this.position = position;
	}
	
	protected void initialize() {
		Robot.gearIntake.setLiftPosition(position);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return Robot.gearIntake.isAtTarget(); 
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}