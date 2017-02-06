package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class GearIntakeLiftMoveMP extends Command
{
	private double position;
	
	public GearIntakeLiftMoveMP(double position) {
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