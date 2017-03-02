package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CameraReadAndProcessImage extends Command
{
	public CameraReadAndProcessImage() {
		requires(Robot.camera);
	}

	@Override
	protected void initialize() {
		Robot.camera.readImagePostProcessedImageToDashboard();
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