
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
    
	public static final double CLIMB_SPEED = 1.0;
	
	private CANTalon rollerLeft;
	private CANTalon rollerRight;

	public Climber() {
		try {
			rollerLeft = new CANTalon(RobotMap.CLIMBER_MOTOR_lEFT_CAN_ID);
			rollerLeft.clearStickyFaults();
			rollerLeft.setSafetyEnabled(false);

			rollerRight = new CANTalon(RobotMap.CLIMBER_MOTOR_RIGHT_CAN_ID);
			rollerRight.clearStickyFaults();
			rollerRight.setSafetyEnabled(false);
} 
		catch (Exception e) {
			System.err.println("An error occurred in the Climber constructor");
		}
	}
	
	public void setSpeed(double speed) {
		rollerLeft.set(speed);
		rollerRight.set(-speed);
	}

	public double getLeftAmps() {
		return rollerLeft.getOutputCurrent();
	}

	public double getRightAmps() {
		return rollerRight.getOutputCurrent();
	}

    public void initDefaultCommand() {
    }
    
	public void updateStatus(Robot.OperationMode operationMode) {
		if (operationMode == Robot.OperationMode.TEST) {
		SmartDashboard.putNumber("Left Climber Amps", rollerLeft.getOutputCurrent());
		SmartDashboard.putNumber("Right Climber Amps", rollerRight.getOutputCurrent());
    }
	}
}

