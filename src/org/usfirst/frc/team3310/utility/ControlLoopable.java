package org.usfirst.frc.team3310.utility;

public interface ControlLoopable 
{
	public void controlLoopUpdate();
	public void setPeriodMs(long periodMs);
}