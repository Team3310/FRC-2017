
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;

/**
 *
 */
public class GearIntakeRollerSetDeployAuton extends ExtraTimeoutCommand {

	private IntakePosition position;
	private double timeout;
	
	// Constructor with speed
    public GearIntakeRollerSetDeployAuton(double timeout) {
    	this.timeout = timeout;
   }

    // Called just before this Command runs the first time
    protected void initialize() {
        this.setTimeout(timeout);
    	Robot.gearIntake.setRollerSpeed(GearIntake.GEAR_INTAKE_LOAD_SPEED);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       if (isTimedOut()) {
        	return true;
        }
         
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.gearIntake.setRollerSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
