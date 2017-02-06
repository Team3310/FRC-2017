
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterLift extends Subsystem {
    
	private CANTalon roller;
	
	public ShooterLift() {
		try {
			roller = new CANTalon(RobotMap.SHOOTER_LIFT_MOTOR_CAN_ID);
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

