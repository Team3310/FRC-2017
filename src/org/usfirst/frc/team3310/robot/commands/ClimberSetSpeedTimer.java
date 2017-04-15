
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimberSetSpeedTimer extends Command {

	private double speed;
	private double time;
	
	// Constructor with speed
    public ClimberSetSpeedTimer(double speed, double time) {
    	this.speed = speed;
    	this.time = time;
        requires(Robot.shooterFeed);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	this.setTimeout(time);
    	Robot.shooterFeed.setClimberSpeed(speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooterFeed.setClimberSpeed(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
