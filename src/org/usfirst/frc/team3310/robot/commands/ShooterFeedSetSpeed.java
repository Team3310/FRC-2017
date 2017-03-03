
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterFeedSetSpeed extends Command {

	private double speed;
	
	// Constructor with speed
    public ShooterFeedSetSpeed(double speed) {
    	this.speed = speed;
        requires(Robot.shooterFeed);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooterFeed.setSpeed(speed);
    	if (Robot.shooter.getShotPosition() == ShotState.FAR) {
    		Robot.ledLights.setShootFar(speed > 0.01);
    	}
    	else {
    		Robot.ledLights.setShootClose(speed > 0.01);
    	}
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
