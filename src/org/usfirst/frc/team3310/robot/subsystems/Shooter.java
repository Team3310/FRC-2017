
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;
import org.usfirst.frc.team3310.robot.subsystems.Drive.SpeedShiftState;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem {
    
	public static enum ShotState { CLOSE, FAR };
	public static final int ENCODER_TICKS_PER_REV = 4096/4;

	private CANTalon shooterMainLeft;
	private CANTalon shooterMainRight;
	private CANTalon shooterFeedLeft;
	private CANTalon shooterFeedRight;

	private Solenoid shotPosition;
	
	public Shooter() {
		try {
			shooterMainLeft = new CANTalon(RobotMap.SHOOTER_MAIN_LEFT_MOTOR_CAN_ID);
			shooterMainLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			shooterMainLeft.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
			shooterMainLeft.changeControlMode(TalonControlMode.PercentVbus);
			shooterMainLeft.setSafetyEnabled(false);
			shooterMainLeft.enableBrakeMode(false);
			shooterMainLeft.setProfile(0);
			shooterMainLeft.setF(0.034);
			shooterMainLeft.setP(0.0);
			shooterMainLeft.setI(0.0005);
			shooterMainLeft.setIZone(2000);
			shooterMainLeft.setD(0);
			shooterMainLeft.setVoltageCompensationRampRate(96.0);
			shooterMainLeft.configNominalOutputVoltage(0.0,0.0);
			shooterMainLeft.configPeakOutputVoltage(12.0, 0);
			
			shooterMainRight = new CANTalon(RobotMap.SHOOTER_MAIN_RIGHT_MOTOR_CAN_ID);
			shooterMainRight.changeControlMode(TalonControlMode.Follower);
			shooterMainRight.set(shooterMainLeft.getDeviceID());
			shooterMainRight.enableBrakeMode(false);
			shooterMainRight.setSafetyEnabled(false);
			shooterMainRight.reverseOutput(true);
			
			shooterFeedLeft = new CANTalon(RobotMap.SHOOTER_FEED_LEFT_MOTOR_CAN_ID);
			shooterFeedLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			shooterFeedLeft.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
			shooterFeedLeft.changeControlMode(TalonControlMode.PercentVbus);
			shooterFeedLeft.setSafetyEnabled(false);
			shooterFeedLeft.enableBrakeMode(false);
			shooterFeedLeft.setProfile(0);
			shooterFeedLeft.setF(0.038);
			shooterFeedLeft.setP(0.0);
			shooterFeedLeft.setI(0.0005);
			shooterFeedLeft.setIZone(2000);
			shooterFeedLeft.setD(0);
			shooterFeedLeft.setVoltageCompensationRampRate(96.0);
			shooterFeedLeft.configNominalOutputVoltage(0.0,0.0);
			shooterFeedLeft.configPeakOutputVoltage(12.0, 0);

			shooterFeedRight = new CANTalon(RobotMap.SHOOTER_FEED_RIGHT_MOTOR_CAN_ID);
			shooterFeedRight.changeControlMode(TalonControlMode.Follower);
			shooterFeedRight.set(shooterFeedLeft.getDeviceID());
			shooterFeedRight.enableBrakeMode(false);
			shooterFeedRight.setSafetyEnabled(false);			
			shooterFeedRight.reverseOutput(true);

			shotPosition = new Solenoid(RobotMap.SHOOTER_SHOT_POSITION_PCM_ID);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the ShooterSubsystem constructor");
		}
	}
	
	public void setMainSpeed(double vbus) {
		shooterMainLeft.changeControlMode(TalonControlMode.PercentVbus);
		shooterMainLeft.set(vbus);
	}
	
	public void setFeedSpeed(double vbus) {
		shooterFeedLeft.changeControlMode(TalonControlMode.PercentVbus);
		shooterFeedLeft.set(vbus);
	}
	
	public void setMainRPM(double rpm) {
		shooterMainLeft.changeControlMode(TalonControlMode.Speed);
		shooterMainLeft.set(rpm);
	}
	
	public void setFeedRPM(double rpm) {
		shooterFeedLeft.changeControlMode(TalonControlMode.Speed);
		shooterFeedLeft.set(rpm);
	}
	
	public void setShotPosition(ShotState state) {
		if(state == ShotState.FAR) {
			shotPosition.set(true);
		}
		else if(state == ShotState.CLOSE) {
			shotPosition.set(false);
		}
	}
	
    public void initDefaultCommand() {
    }
    
	public void updateStatus(Robot.OperationMode operationMode) {
//		SmartDashboard.putNumber("Shooter Main Closed Loop Error", shooterMainLeft.getClosedLoopError());
//		SmartDashboard.putNumber("Shooter Main Motor Output", shooterMainLeft.getOutputVoltage()/shooterMainLeft.getBusVoltage());
//		SmartDashboard.putNumber("Shooter Main Output Voltage", shooterMainLeft.getOutputVoltage());
		SmartDashboard.putNumber("Shooter Main Actual RPM", shooterMainLeft.getSpeed());
		SmartDashboard.putNumber("Shooter Main Plot RPM", shooterMainLeft.getSpeed());
//		SmartDashboard.putNumber("Shooter Main Amps Left", shooterMainLeft.getOutputCurrent());
//		SmartDashboard.putNumber("Shooter Main Amps Right", shooterMainRight.getOutputCurrent());

//		SmartDashboard.putNumber("Shooter Feed Closed Loop Error", shooterFeedLeft.getClosedLoopError());
//		SmartDashboard.putNumber("Shooter Feed Motor Output", shooterFeedLeft.getOutputVoltage()/shooterFeedLeft.getBusVoltage());
//		SmartDashboard.putNumber("Shooter Feed Output Voltage", shooterFeedLeft.getOutputVoltage());
		SmartDashboard.putNumber("Shooter Feed Actual RPM", shooterFeedLeft.getSpeed());
		SmartDashboard.putNumber("Shooter Feed Plot RPM", shooterFeedLeft.getSpeed());
//		SmartDashboard.putNumber("Shooter Feed Amps Left", shooterFeedLeft.getOutputCurrent());
//		SmartDashboard.putNumber("Shooter Feed Amps Right", shooterFeedRight.getOutputCurrent());
    }
}

