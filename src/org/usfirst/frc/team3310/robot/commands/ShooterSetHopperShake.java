package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.HopperState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class ShooterSetHopperShake extends Command
{
	private double initialDelay;
	private double totalTime;
	private double shakeInterval;
	private boolean initialDelayDone = false;
	private double startTime;
	private HopperState state = HopperState.CLOSE;
	
	public ShooterSetHopperShake(double initialDelay, double totalTime, double shakeInterval) {
		requires(Robot.shooter);
		this.initialDelay = initialDelay * 1000;
		this.totalTime = totalTime;
		this.shakeInterval = shakeInterval * 1000;
	}

	@Override
	protected void initialize() {
		this.setTimeout(totalTime);
		startTime = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
		if (initialDelayDone == false) {
			if (System.currentTimeMillis() - startTime > initialDelay) {
				initialDelayDone = true;
				startTime = System.currentTimeMillis();
			}
		}
		else {
			if ((System.currentTimeMillis() - startTime) > shakeInterval) {
				Robot.shooter.setHopperPosition(state);
				if (state == HopperState.CLOSE) {
					state = HopperState.OPEN;
				}
				else {
					state = HopperState.CLOSE;
				}
				startTime = System.currentTimeMillis();
			}
		}
	}

	@Override
	protected boolean isFinished() {
		if (isTimedOut()) {
			return true;
		}
		return false;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
			
	}
}