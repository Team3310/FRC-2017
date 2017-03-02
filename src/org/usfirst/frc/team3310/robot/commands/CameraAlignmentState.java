package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CameraAlignmentState extends Command
{
	private boolean state;
	
	public CameraAlignmentState(boolean state) {
		requires(Robot.camera);
		this.state = state;
	}

	@Override
	protected void initialize() {
		Robot.camera.setAlignmentFinished(state);
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