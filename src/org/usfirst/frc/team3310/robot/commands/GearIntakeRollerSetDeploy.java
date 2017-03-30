
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;

/**
 *
 */
public class GearIntakeRollerSetDeploy extends ExtraTimeoutCommand {

	private IntakePosition position;
	private double timeout;
	private double secondTimeout;
	private boolean isDeploy = false;
	private boolean secondTimerSet = false;
	
	// Constructor with speed
    public GearIntakeRollerSetDeploy(double timeout, double secondTimeout, IntakePosition position) {
    	this.position = position;
    	this.timeout = timeout;
    	this.secondTimeout = secondTimeout;
   }

    // Called just before this Command runs the first time
    protected void initialize() {
    	isDeploy = false;
    	secondTimerSet = false;
    	if (position == IntakePosition.GEAR_DEPLOY) {
        	this.setTimeout(timeout);
    		Robot.gearIntake.setRollerSpeed(GearIntake.GEAR_INTAKE_DEPLOY_SPEED);
    		isDeploy = true;
    	}
    	else {
        	this.setTimeout(0);
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (isDeploy == false) {
        	return true;
        }
        
        if (isTimedOut() && secondTimerSet == false) {
        	startExtraTimeout(secondTimeout);
        	return false;
        }
        else if (isExtraTimedOut()) {
        	return true;
        }
        
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	if (isDeploy == true) {
    		Robot.gearIntake.setRollerSpeed(0);
    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
