package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Drive.DriveControlMode;
import org.usfirst.frc.team3310.utility.PathGenerator;

import edu.wpi.first.wpilibj.command.Command;

public class DrivePathMP extends Command
{
	private PathGenerator path;

	public DrivePathMP(PathGenerator path) {
		requires(Robot.drive);
		this.path = path;
	}

	protected void initialize() {
		Robot.drive.setPathMP(path);
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