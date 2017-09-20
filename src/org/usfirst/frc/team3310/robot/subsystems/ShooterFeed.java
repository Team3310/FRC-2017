
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
    
	private CANTalon rollerLeft;
	private CANTalon rollerRight;
	private CANTalon oldClimber;
	
	public static final double SHOOTER_FEED_SHOOT_FAR_SPEED = 1.0;
	public static final double SHOOTER_FEED_SHOOT_CLOSE_SPEED = 0.6;
	public static final double SHOOTER_FEED_BALL_INTAKE_SPEED = -0.5;
	public static final double SHOOT_FEED_OFF_SPEED = 0.0;
	public enum RobotType {COMP, PRACTICE};

	public static final double CLIMB_SPEED = 1.0;
	public static RobotType robotType = RobotType.PRACTICE;
	
	public ShooterFeed() {
		try {
			rollerLeft = new CANTalon(RobotMap.SHOOTER_FEED_MOTOR_CAN_ID);
			rollerLeft.clearStickyFaults();
			rollerLeft.setSafetyEnabled(false);
			rollerLeft.enableBrakeMode(true);
			
			rollerRight = new CANTalon(RobotMap.CLIMBER_MOTOR_RIGHT_CAN_ID);
			rollerRight.clearStickyFaults();
			rollerRight.setSafetyEnabled(false);

			oldClimber = new CANTalon(RobotMap.CLIMBER_MOTOR_lEFT_CAN_ID);
			oldClimber.clearStickyFaults();
			oldClimber.setSafetyEnabled(false);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Shooter Lift/Climber Subsystem constructor");
		}
	}
	
	public void setSpeed(double speed) {
		if (robotType == RobotType.COMP) {
			rollerLeft.set(speed);
			rollerRight.set(-speed);
			oldClimber.set(speed);
		}
		else {
			oldClimber.set(speed);
		}
	}
	
	public void setClimberSpeed(double speed) {
		if (robotType == RobotType.COMP) {
			rollerLeft.set(speed);
			rollerRight.set(-speed);
			oldClimber.set(speed);
		}
		else {
			rollerLeft.set(speed);
		}
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

