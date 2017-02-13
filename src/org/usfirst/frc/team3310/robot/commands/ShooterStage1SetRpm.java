
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterStage1SetRpm extends Command {

	private double rpm;
	
	// Constructor with speed
    public ShooterStage1SetRpm(double rpm) {
    	this.rpm = rpm;
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.setStage1Rpm(rpm);
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
