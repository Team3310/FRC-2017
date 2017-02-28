package org.usfirst.frc.team3310.buttons;

import org.usfirst.frc.team3310.controller.LogitechController;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * @author bselle
 */
public class LogitechDPadTriggerButton extends Button 
{
	public static final int UP 			= 0;
	public static final int UP_RIGHT 	= 45;
	public static final int RIGHT 		= 90;
	public static final int DOWN_RIGHT 	= 135;
	public static final int DOWN 		= 180;
	public static final int DOWN_LEFT 	= 225;
	public static final int LEFT 		= 270;
	public static final int UP_LEFT	 	= 315;

	private int buttonAngle;
	private LogitechController controller;

	public LogitechDPadTriggerButton(LogitechController superior, int dPadButtonAngle) {
		this.buttonAngle = dPadButtonAngle;
		this.controller = superior;
	}
	
	@Override
	public boolean get() {
		return controller.getDpadAngle() == buttonAngle;
	}
}