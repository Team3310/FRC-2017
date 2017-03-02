package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CameraOffset extends Command
{
	private double deltaAngle;
	
	public CameraOffset(double deltaAngle) {
		requires(Robot.camera);
		this.deltaAngle = deltaAngle;
	}

	@Override
	protected void initialize() {
		Robot.camera.incrementAngleOffset(deltaAngle);;
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