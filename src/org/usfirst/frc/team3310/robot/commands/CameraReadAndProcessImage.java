package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Camera.ImageOutput;

import edu.wpi.first.wpilibj.command.Command;

public class CameraReadAndProcessImage extends Command
{
	public CameraReadAndProcessImage() {
		requires(Robot.camera);
	}

	@Override
	protected void initialize() {
		Robot.camera.readImageGetBestTarget(ImageOutput.DASHBOARD);
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