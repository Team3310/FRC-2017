package org.usfirst.frc.team3310.robot.commands;

import java.util.Set;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveWaitForMarker extends Command
{
	private String targetMarker;
	private double timeout;
	
	public DriveWaitForMarker(String targetMarker, double timeout) {
		this.targetMarker = targetMarker;
		this.timeout = timeout;
	}

	@Override
	protected void initialize() {
		setTimeout(timeout);
	}

	@Override
	protected void execute() {		
	}

	@Override
	protected boolean isFinished() {
		if (isTimedOut()) {
			return true;
		}
		
		Set<String> markers = Robot.drive.getPathMarkersCrossed();
		if (markers == null) {
			return false;
		}
		
		for (String currentMarker : markers) {
			if (currentMarker.equals(targetMarker)) {
				return true;
			}
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