package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveSpeed extends Command
{
	private double speed;
	
    public DriveSpeed(double speed) {
        requires(Robot.drive);
        this.speed = speed;
    }

    protected void initialize() {
    	Robot.drive.setSpeed(speed);
    }

    protected void execute() {
    	
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	
    }

    protected void interrupted() {
    	
    }
}