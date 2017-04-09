package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Drive.DriveControlMode;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import org.usfirst.frc.team3310.utility.MotionProfileCache;

import edu.wpi.first.wpilibj.command.Command;

public class DriveAbsoluteTurnMPCached extends Command
{
	private String key;
	private MPSoftwareTurnType turnType;

	public DriveAbsoluteTurnMPCached(double startTurnAngleDeg, double absoluteTurnAngleDeg, double maxTurnRateDegPerSec, MPSoftwareTurnType turnType) {
		requires(Robot.drive);
		this.turnType = turnType;
		
		MotionProfileCache cache = MotionProfileCache.getInstance();
		key = cache.addMP(startTurnAngleDeg, absoluteTurnAngleDeg, maxTurnRateDegPerSec, Robot.drive.getPeriodMs(), Drive.MP_TURN_T1, Drive.MP_TURN_T2);
	}

	protected void initialize() {
//		if (Robot.drive.isRed() == false) {
//			absoluteTurnAngleDeg = absoluteTurnAngleDeg * -1;
//		}
		Robot.drive.setAbsoluteTurnMPCached(key, turnType);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return Robot.drive.isFinished(); 
	}

	protected void end() {
		Robot.drive.setControlMode(DriveControlMode.JOYSTICK);
	}

	protected void interrupted() {
		end();
	}
}