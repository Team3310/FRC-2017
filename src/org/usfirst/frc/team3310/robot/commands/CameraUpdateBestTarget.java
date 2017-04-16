package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Camera.ImageOutput;

import edu.wpi.first.wpilibj.command.Command;

public class CameraUpdateBestTarget extends Command
{
	
	public CameraUpdateBestTarget() {
		requires(Robot.camera);
	}

	@Override
	protected void initialize() {
		Robot.camera.getBestTarget(ImageOutput.DASHBOARD, true);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return true;
	}

	protected void end() {
		
	}

	protected void interrupted() {
		end();
	}
}