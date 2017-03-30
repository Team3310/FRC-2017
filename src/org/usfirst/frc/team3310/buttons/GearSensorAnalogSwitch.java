package org.usfirst.frc.team3310.buttons;

import org.usfirst.frc.team3310.robot.Robot;

import edu.wpi.first.wpilibj.buttons.Button;

public class GearSensorAnalogSwitch extends Button {

	public boolean get() {
		return Robot.gearIntake.isGearPresent();
	}
}