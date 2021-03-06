
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterSetToggle extends Command {

	private double stage1Rpm;
	private double stage2Rpm;
	
	// Constructor with speed
    public ShooterSetToggle(double stage1Rpm, double stage2Rpm) {
    	this.stage1Rpm = stage1Rpm;
    	this.stage2Rpm = stage2Rpm;
        requires(Robot.shooter);
    }

	// Called just before this Command runs the first time
    protected void initialize() {    	
    	boolean isShooterOn = Robot.shooter.isShooterOn();
    	
		if (isShooterOn) {
			Robot.shooter.setStage1Speed(0.0);
			Robot.shooter.setStage2Speed(0.0);
	    	Robot.ledLights.setShooterWheelsOn(false);
		}
		else {
			Robot.shooter.setStage1Rpm(stage1Rpm);
			Robot.shooter.setStage2Rpm(stage2Rpm);
	    	Robot.ledLights.setShooterWheelsOn(true);
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
