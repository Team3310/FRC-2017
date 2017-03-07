
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.BallIntake;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BallIntakeRollerSetDeploy extends Command {

	private IntakePosition position;
	private double timeout;
	private boolean isDeploy = false;
	
	// Constructor with speed
    public BallIntakeRollerSetDeploy(double timeout, IntakePosition position) {
    	this.position = position;
    	this.timeout = timeout;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	this.setTimeout(timeout);
    	isDeploy = false;
    	if (position == IntakePosition.GEAR_DEPLOY) {
    		Robot.ballIntake.setRollerSpeed(BallIntake.BALL_INTAKE_GEAR_DEPLOY_SPEED);
    		isDeploy = true;
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (isDeploy == false) || isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
		Robot.ballIntake.setRollerSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
