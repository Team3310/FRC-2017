package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Camera.ImageOutput;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Drive.DriveControlMode;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import org.usfirst.frc.team3310.vision.ImageProcessor.TargetInfo;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class CameraTurnToBestTarget extends Command
{
	private boolean targetFound;
	
	public CameraTurnToBestTarget() {
		requires(Robot.camera);
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		TargetInfo bestTarget = Robot.camera.getBestTarget(ImageOutput.DASHBOARD, true);
		if (bestTarget != null) {
			if(bestTarget.angleToTargetDeg > 15) {
				Robot.drive.setRelativeTurnMP(bestTarget.angleToTargetDeg, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK);			
			}
			else {
				Robot.drive.setRelativeTurnPID(bestTarget.angleToTargetDeg, 0.4, 0.4, MPSoftwareTurnType.TANK);
			}
			targetFound = true;
		}
		else {
			targetFound = false;
			DriverStation.reportError("Null target", false);
		}
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		if (!targetFound) {
			return true;
		}
		return Robot.drive.isFinished(); 
	}

	protected void end() {
		Robot.drive.setControlMode(DriveControlMode.JOYSTICK);
	}

	protected void interrupted() {
		end();
	}
}