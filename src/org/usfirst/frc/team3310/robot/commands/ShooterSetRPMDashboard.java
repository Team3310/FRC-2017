
package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterSetRPMDashboard extends Command {

	// Constructor with speed
    public ShooterSetRPMDashboard() {
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double shooterSpeed = SmartDashboard.getNumber("Shooter Target RPM", 0);
    	DriverStation.reportWarning("Shooter Main RPM = " + shooterSpeed, false);
    	Robot.shooter.setMainRPM(shooterSpeed);
    	Robot.shooter.setFeedRPM(shooterSpeed);
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
