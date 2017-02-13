
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterFeed extends Subsystem {
    
	private CANTalon roller;
	
	public ShooterFeed() {
		try {
			roller = new CANTalon(RobotMap.SHOOTER_FEED_MOTOR_CAN_ID);
			roller.clearStickyFaults();
			roller.setSafetyEnabled(false);
			roller.enableBrakeMode(true);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the ShooterLiftSubsystem constructor");
		}
	}
	
	public void setSpeed(double speed) {
		roller.set(speed);
	}
	
    public void initDefaultCommand() {
    }
    
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Lift Amps", roller.getOutputCurrent());
    }

}

