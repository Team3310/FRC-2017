
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.BallIntake;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BallIntakeRollerSetSpeedPosition extends Command {

	// Constructor with speed
    public BallIntakeRollerSetSpeedPosition() {
         requires(Robot.ballIntake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if (Robot.ballIntake.getIntakePosition() == IntakePosition.BALL_INTAKE) {
    		Robot.ballIntake.setRollerSpeed(BallIntake.BALL_INTAKE_LOAD_SPEED);
    	}
    	else if (Robot.ballIntake.getIntakePosition() == IntakePosition.GEAR_INTAKE) {
    		Robot.ballIntake.setRollerSpeed(BallIntake.GEAR_INTAKE_LOAD_SPEED);
    	}
    	else {
    		Robot.ballIntake.setRollerSpeed(BallIntake.BALL_INTAKE_LOAD_SPEED);
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
