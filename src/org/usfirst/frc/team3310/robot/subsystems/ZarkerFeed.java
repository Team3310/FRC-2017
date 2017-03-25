
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ZarkerFeed extends Subsystem {
    
	public static final double ZARKER_FEED_SHOOT_SPEED = 0.8;
	public static final double ZARKER_FEED_EJECT_SPEED = -0.8;
	public static final double ZARKER_FEED_OFF_SPEED = 0.0;

	private CANTalon rollerLeft;
	private CANTalon rollerRight;
	
	public ZarkerFeed() {
		try {
			rollerLeft = new CANTalon(RobotMap.ZARKER_FEED_ROLLER_LEFT_MOTOR_CAN_ID);
			rollerLeft.clearStickyFaults();
			rollerLeft.setSafetyEnabled(false);
			rollerRight = new CANTalon(RobotMap.ZARKER_FEED_ROLLER_RIGHT_MOTOR_CAN_ID);
			rollerRight.clearStickyFaults();
			rollerRight.setSafetyEnabled(false);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Zarker feed constructor");
		}
	}
	
	public void setSpeed(double speed) {
		rollerLeft.set(-speed);
		rollerRight.set(speed);
	}
	
    public void initDefaultCommand() {
    }
    
	public void updateStatus(Robot.OperationMode operationMode) {
		if (operationMode == Robot.OperationMode.TEST) {
			SmartDashboard.putNumber("Barker Feed Left Amps", rollerLeft.getOutputCurrent());
			SmartDashboard.putNumber("Barker Feed Right Amps", rollerRight.getOutputCurrent());
		}
    }

}

