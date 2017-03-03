package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;

import edu.wpi.first.wpilibj.command.Command;

public class BallIntakeLiftMoveMP extends Command
{
	private IntakePosition position;
	
	public BallIntakeLiftMoveMP(IntakePosition position) {
		requires(Robot.ballIntake);
		this.position = position;
	}
	
	protected void initialize() {
		Robot.ballIntake.setLiftPosition(position);
		Robot.ledLights.setIntakePosition(position);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return Robot.ballIntake.isAtTarget(); 
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}