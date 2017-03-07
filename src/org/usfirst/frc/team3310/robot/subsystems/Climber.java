
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climber extends Subsystem {
    
	public static final double CLIMB_SPEED = 0.8;
	
	private CANTalon roller;

	public Climber() {
		try {
			roller = new CANTalon(RobotMap.CLIMBER_MOTOR_CAN_ID);
			roller.clearStickyFaults();
			roller.setSafetyEnabled(false);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Climber constructor");
		}
	}
	
	public void setSpeed(double speed) {
		roller.set(speed);
	}

	public double getAmps() {
		return roller.getOutputCurrent();
	}
	
    public void initDefaultCommand() {
    }
    
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Climber Amps", roller.getOutputCurrent());
    }
}

