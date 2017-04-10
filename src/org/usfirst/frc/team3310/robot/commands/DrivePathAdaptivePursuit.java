package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Drive.DriveControlMode;
import org.usfirst.frc.team3310.utility.Path;
import org.usfirst.frc.team3310.utility.PathGenerator;

import edu.wpi.first.wpilibj.command.Command;

public class DrivePathAdaptivePursuit extends Command
{
	private Path path;
	private boolean reversed;

	public DrivePathAdaptivePursuit(Path path, boolean reversed) {
		requires(Robot.drive);
		this.path = path;
		this.reversed = reversed;
	}

	protected void initialize() {
		Robot.drive.setPathAdaptivePursuit(path, reversed);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return Robot.drive.isFinished(); 
	}

	protected void end() {
		Robot.drive.setControlMode(DriveControlMode.JOYSTICK);
	}

	protected void interrupted() {
		end();
	}
}