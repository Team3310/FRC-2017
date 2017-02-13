
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;
import org.usfirst.frc.team3310.robot.subsystems.Drive.SpeedShiftState;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem {
    
	public static enum ShotState { CLOSE, FAR };
	public static final int ENCODER_TICKS_PER_REV = 1024;

	private CANTalon shooterStage2Left;
	private CANTalon shooterStage2Right;
	private CANTalon shooterStage1Left;
	private CANTalon shooterStage1Right;

	private Solenoid shotPosition;
	
	public Shooter() {
		try {
			shooterStage2Left = new CANTalon(RobotMap.SHOOTER_STAGE_2_LEFT_MOTOR_CAN_ID);
			shooterStage2Left.clearStickyFaults();
			shooterStage2Left.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			shooterStage2Left.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
			shooterStage2Left.changeControlMode(TalonControlMode.PercentVbus);
			shooterStage2Left.setSafetyEnabled(false);
			shooterStage2Left.enableBrakeMode(false);
			shooterStage2Left.setProfile(0);
			shooterStage2Left.setF(0.034);
			shooterStage2Left.setP(0.0);
			shooterStage2Left.setI(0.0005);
			shooterStage2Left.setIZone(2000);
			shooterStage2Left.setD(0);
			shooterStage2Left.setVoltageCompensationRampRate(96.0);
			shooterStage2Left.configNominalOutputVoltage(0.0,0.0);
			shooterStage2Left.configPeakOutputVoltage(12.0, 0);
			shooterStage2Left.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 10);
//	        if (shooterStage2Left.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
//	            DriverStation.reportError("Could not detect shooter stage 2 encoder!", false);
//	        }
			
			shooterStage2Right = new CANTalon(RobotMap.SHOOTER_STAGE_2_RIGHT_MOTOR_CAN_ID);
			shooterStage2Right.changeControlMode(TalonControlMode.Follower);
			shooterStage2Right.set(shooterStage2Left.getDeviceID());
			shooterStage2Right.setSafetyEnabled(false);
			shooterStage2Right.enableBrakeMode(false);
			shooterStage2Right.reverseOutput(true);
			
			shooterStage1Left = new CANTalon(RobotMap.SHOOTER_STAGE_1_LEFT_MOTOR_CAN_ID);
			shooterStage1Left.clearStickyFaults();
			shooterStage1Left.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			shooterStage1Left.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
			shooterStage1Left.changeControlMode(TalonControlMode.PercentVbus);
			shooterStage1Left.setSafetyEnabled(false);
			shooterStage1Left.enableBrakeMode(false);
			shooterStage1Left.setProfile(0);
			shooterStage1Left.setF(0.038);
			shooterStage1Left.setP(0.0);
			shooterStage1Left.setI(0.0005);
			shooterStage1Left.setIZone(2000);
			shooterStage1Left.setD(0);
			shooterStage1Left.setVoltageCompensationRampRate(96.0);
			shooterStage1Left.configNominalOutputVoltage(0.0,0.0);
			shooterStage1Left.configPeakOutputVoltage(12.0, 0);
			shooterStage1Left.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 10);
//	        if (shooterStage1Left.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
//	            DriverStation.reportError("Could not detect shooter stage 1 encoder!", false);
//	        }

			shooterStage1Right = new CANTalon(RobotMap.SHOOTER_STAGE_1_RIGHT_MOTOR_CAN_ID);
			shooterStage1Right.changeControlMode(TalonControlMode.Follower);
			shooterStage1Right.set(shooterStage1Left.getDeviceID());
			shooterStage1Right.setSafetyEnabled(false);			
			shooterStage1Right.enableBrakeMode(false);
			shooterStage1Right.reverseOutput(true);

			shotPosition = new Solenoid(RobotMap.SHOOTER_SHOT_POSITION_PCM_ID);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the ShooterSubsystem constructor");
		}
	}
	
	public void setStage2Speed(double vbus) {
		shooterStage2Left.changeControlMode(TalonControlMode.PercentVbus);
		shooterStage2Left.set(vbus);
	}
	
	public void setStage1Speed(double vbus) {
		shooterStage1Left.changeControlMode(TalonControlMode.PercentVbus);
		shooterStage1Left.set(vbus);
	}
	
	public void setStage2Rpm(double rpm) {
		shooterStage2Left.changeControlMode(TalonControlMode.Speed);
		shooterStage2Left.set(rpm);
	}
	
	public void setStage1Rpm(double rpm) {
		shooterStage1Left.changeControlMode(TalonControlMode.Speed);
		shooterStage1Left.set(rpm);
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
		SmartDashboard.putNumber("Shooter Stage 2 Closed Loop Error", shooterStage2Left.getClosedLoopError());
		SmartDashboard.putNumber("Shooter Stage 2 Motor Output", shooterStage2Left.getOutputVoltage()/shooterStage2Left.getBusVoltage());
		SmartDashboard.putNumber("Shooter Stage 2 Output Voltage", shooterStage2Left.getOutputVoltage());
		SmartDashboard.putNumber("Shooter Stage 2 Actual RPM", shooterStage2Left.getSpeed());
		SmartDashboard.putNumber("Shooter Stage 2 Plot RPM", shooterStage2Left.getSpeed());
		SmartDashboard.putNumber("Shooter Stage 2 Amps Left", shooterStage2Left.getOutputCurrent());
		SmartDashboard.putNumber("Shooter Stage 2 Amps Right", shooterStage2Right.getOutputCurrent());

		SmartDashboard.putNumber("Shooter Stage 1 Closed Loop Error", shooterStage1Left.getClosedLoopError());
		SmartDashboard.putNumber("Shooter Stage 1 Motor Output", shooterStage1Left.getOutputVoltage()/shooterStage1Left.getBusVoltage());
		SmartDashboard.putNumber("Shooter Stage 1 Output Voltage", shooterStage1Left.getOutputVoltage());
		SmartDashboard.putNumber("Shooter Stage 1 Actual RPM", shooterStage1Left.getSpeed());
		SmartDashboard.putNumber("Shooter Stage 1 Plot RPM", shooterStage1Left.getSpeed());
		SmartDashboard.putNumber("Shooter Stage 1 Amps Left", shooterStage1Left.getOutputCurrent());
		SmartDashboard.putNumber("Shooter Stage 1 Amps Right", shooterStage1Left.getOutputCurrent());
    }
}

