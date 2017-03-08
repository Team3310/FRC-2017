
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimberSetSpeed extends Command {

	private double climbSpeed;
	private double driveSpeed;
	
	// Constructor with speed
    public ClimberSetSpeed(double climbSpeed, double driveSpeed) {
    	this.climbSpeed = climbSpeed;
    	this.driveSpeed = driveSpeed;
        requires(Robot.climber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.climber.setSpeed(climbSpeed);
    	Robot.drive.setClimbSpeed(driveSpeed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
