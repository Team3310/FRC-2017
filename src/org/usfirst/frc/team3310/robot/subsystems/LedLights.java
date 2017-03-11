package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.utility.DotStarsLEDStrip;

import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.command.Subsystem;

public class LedLights extends Subsystem {
	
	// Data [ startIndex, endIndex, red, green, blue]
	private static final int[] allLights = {0, 24, 0, 255, 0};

	private static final int[] intakeRollerOn = {0, 7, 0, 0, 255};
	private static final int[] gearLoaded = {8, 15, 0, 255, 0};
	private static final int[] shooterWheelsOn = {16, 24, 255, 0, 0};
	
	private static final int[] shootFar = {26, 30, 0, 255, 0};
	private static final int[] shootClose = {26, 30, 255, 255, 0};
	
	private static final int[] intakePositionGear = {21, 25, 255, 0, 0};
	private static final int[] intakePositionBall = {21, 25, 0, 255, 0};
	private static final int[] intakePositionPresent = {21, 25, 0, 0, 255};
	private static final int[] intakePositionShoot = {21, 25, 255, 255, 0};
	
	private static final int numLeds = 31;
	private static final Port spiPort = Port.kOnboardCS0;
	
	private DotStarsLEDStrip lights;
	
	public LedLights() {
		lights = new DotStarsLEDStrip(numLeds, spiPort);
	}
	
	public void setAllLightsOn(boolean status) {
		setLights(status, allLights);
	}

	public void setIntakeRollerOn(boolean status) {
		setLights(status, intakeRollerOn);
	}

	public void setGearLoaded(boolean status) {
		setLights(status, gearLoaded);
	}

	public void setShooterWheelsOn(boolean status) {
		setLights(status, shooterWheelsOn);
	}

	public void setShootFar(boolean status) {
//		setLights(status, shootFar);
	}

	public void setShootClose(boolean status) {
//		setLights(status, shootClose);
	}

	public void setIntakePosition(IntakePosition position) {
		if (position == IntakePosition.BALL_INTAKE) {
//			setLights(true, intakePositionBall);
		}
		else if (position == IntakePosition.GEAR_INTAKE) {
//			setLights(true, intakePositionGear);
		}
		else if (position == IntakePosition.GEAR_PRESENT) {
//			setLights(true, intakePositionPresent);
		}
		else if (position == IntakePosition.SHOOT) {
//			setLights(true, intakePositionShoot);
		}
		else if (position == IntakePosition.RETRACT) {
//			setLights(false, null);
		}
	}

	private void setLights(boolean status, int[] data) {
		if ( status ) {
			lights.setLEDColors(data[0], data[1] + 1, data[2], data[3], data[4]);
		}
		else {
			lights.clearColorBuffer(data[0], data[1] + 1);
		}
		lights.updateColors();
	}
	
	public void initDefaultCommand() {
	}
}
