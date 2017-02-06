
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem {
    
	public static final int ENCODER_TICKS_PER_REV = 4096/4;

	private CANTalon shooterMain;
	private CANTalon shooterFeed;
	
	public Shooter() {
		try {
			shooterMain = new CANTalon(RobotMap.SHOOTER_MAIN_MOTOR_CAN_ID);
			shooterMain.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			shooterMain.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
			shooterMain.changeControlMode(TalonControlMode.PercentVbus);
			shooterMain.setSafetyEnabled(false);
			shooterMain.enableBrakeMode(false);
			shooterMain.setProfile(0);
			shooterMain.setF(0.032);
			shooterMain.setP(0.1);
			shooterMain.setI(0.0);
			shooterMain.setIZone(500);
			shooterMain.setD(0);
			
			shooterFeed = new CANTalon(RobotMap.SHOOTER_FEED_MOTOR_CAN_ID);
			shooterFeed.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			shooterFeed.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
			shooterFeed.changeControlMode(TalonControlMode.PercentVbus);
			shooterFeed.setSafetyEnabled(false);
			shooterFeed.enableBrakeMode(false);
			shooterFeed.setProfile(0);
			shooterFeed.setF(0.032);
			shooterFeed.setP(0.1);
			shooterFeed.setI(0.0);
			shooterFeed.setIZone(500);
			shooterFeed.setD(0);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the ShooterSubsystem constructor");
		}
	}
	
	public void setMainSpeed(double vbus) {
		shooterMain.changeControlMode(TalonControlMode.PercentVbus);
		shooterMain.set(vbus);
	}
	
	public void setFeedSpeed(double vbus) {
		shooterFeed.changeControlMode(TalonControlMode.PercentVbus);
		shooterFeed.set(vbus);
	}
	
	public void setMainRPM(double rpm) {
		shooterMain.changeControlMode(TalonControlMode.Speed);
		shooterMain.set(rpm);
	}
	
	public void setFeedRPM(double rpm) {
		shooterFeed.changeControlMode(TalonControlMode.Speed);
		shooterFeed.set(rpm);
	}
	
    public void initDefaultCommand() {
    }
    
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Shooter Main Closed Loop Error", shooterMain.getClosedLoopError());
		SmartDashboard.putNumber("Shooter Main Motor Output", shooterMain.getOutputVoltage()/shooterMain.getBusVoltage());
		SmartDashboard.putNumber("Shooter Main Output Voltage", shooterMain.getOutputVoltage());
		SmartDashboard.putNumber("Shooter Main Actual RPM", shooterMain.getSpeed());
		SmartDashboard.putNumber("Shooter Main Plot RPM", shooterMain.getSpeed());
		SmartDashboard.putNumber("Shooter Main Amps", shooterMain.getOutputCurrent());

		SmartDashboard.putNumber("Shooter Feed Closed Loop Error", shooterFeed.getClosedLoopError());
		SmartDashboard.putNumber("Shooter Feed Motor Output", shooterFeed.getOutputVoltage()/shooterFeed.getBusVoltage());
		SmartDashboard.putNumber("Shooter Feed Output Voltage", shooterFeed.getOutputVoltage());
		SmartDashboard.putNumber("Shooter Feed Actual RPM", shooterFeed.getSpeed());
		SmartDashboard.putNumber("Shooter Feed Plot RPM", shooterFeed.getSpeed());
		SmartDashboard.putNumber("Shooter Feed Amps", shooterFeed.getOutputCurrent());
    }
}

