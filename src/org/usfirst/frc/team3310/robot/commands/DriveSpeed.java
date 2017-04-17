package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveSpeed extends Command
{
	private double speed;
	private double timeout;
	
    public DriveSpeed(double speed, double timeout) {
        requires(Robot.drive);
        this.speed = speed;
        this.timeout = timeout;
    }

    public DriveSpeed(double speed) {
        requires(Robot.drive);
        this.speed = speed;
    }

    protected void initialize() {
    	if (timeout > 0) {
    		setTimeout(timeout);
    	}
    	Robot.drive.setSpeed(speed);
    }

    protected void execute() {
    	
    }

    protected boolean isFinished() {
    	if (timeout > 0) {
    		return isTimedOut();
    	}
        return true;
    }

    protected void end() {
    	if (timeout > 0) {
        	Robot.drive.setSpeed(0);
    	}
    }

    protected void interrupted() {
    	
    }
}