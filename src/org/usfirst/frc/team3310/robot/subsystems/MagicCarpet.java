
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MagicCarpet extends Subsystem {
    
	public static final double MAGIC_CARPET_BALL_LOAD_SPEED = 0.8;
	public static final double MAGIC_CARPET_BALL_SHOOT_SPEED = 0.8;
	public static final double MAGIC_CARPET_BALL_MIX_SPEED = -0.5;
	public static final double MAGIC_CARPET_GEAR_LOAD_SPEED = 0.8;
	public static final double MAGIC_CARPET_GEAR_PRESENT_SPEED = -0.4;
	public static final double MAGIC_CARPET_GEAR_EJECT_SPEED = -0.8;
	public static final double MAGIC_CARPET_OFF_SPEED = 0.0;

	private CANTalon roller;
	
	public MagicCarpet() {
		try {
			roller = new CANTalon(RobotMap.MAGIC_CARPET_ROLLER_MOTOR_CAN_ID);
			roller.clearStickyFaults();
			roller.setSafetyEnabled(false);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the MagicCarpet constructor");
		}
	}
	
	public void setSpeed(double speed) {
		roller.set(speed);
	}
	
    public void initDefaultCommand() {
    }
    
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Magic Carpet Amps", roller.getOutputCurrent());
    }

}

