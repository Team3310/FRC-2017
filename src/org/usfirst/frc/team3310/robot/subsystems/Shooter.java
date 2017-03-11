
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.CANTalon.VelocityMeasurementPeriod;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem {
    
	public static enum ShotState { CLOSE, FAR };
	public static final int ENCODER_TICKS_PER_REV = 1024;

	public static final double SHOOTER_STAGE1_RPM_FAR = 2900;
	public static final double SHOOTER_STAGE2_RPM_FAR = 2900;
	public static final double SHOOTER_STAGE1_RPM_CLOSE = 2680;
	public static final double SHOOTER_STAGE2_RPM_CLOSE = 2680;
	public static final double SHOOTER_STAGE1_OFF = 0.0;
	public static final double SHOOTER_STAGE2_OFF = 0.0;
	
	private CANTalon shooterStage2Left;
	private CANTalon shooterStage2Right;
	private CANTalon shooterStage1Left;
	private CANTalon shooterStage1Right;

	private Solenoid shotPosition;
	
	public Shooter() {
		try {
			shooterStage2Left = new CANTalon(RobotMap.SHOOTER_STAGE_2_LEFT_MOTOR_CAN_ID);
			shooterStage2Left.clearStickyFaults();
			shooterStage2Left.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
//			shooterStage2Left.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
			shooterStage2Left.changeControlMode(TalonControlMode.PercentVbus);
			shooterStage2Left.setSafetyEnabled(false);
			shooterStage2Left.enableBrakeMode(false);
			shooterStage2Left.setProfile(0);
			shooterStage2Left.setF(0.0345);
			shooterStage2Left.setP(0.0); //0.12 alone
			shooterStage2Left.setI(0.0001); // 0.0001 alone
			shooterStage2Left.setIZone(2000);
			shooterStage2Left.setD(0);
			shooterStage2Left.setNominalClosedLoopVoltage(12.0);
//			shooterStage2Left.configNominalOutputVoltage(0.0,0.0);
//			shooterStage2Left.configPeakOutputVoltage(12.0, 0);
//			shooterStage2Left.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 10);
//			FeedbackDeviceStatus status = shooterStage2Left.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
//			DriverStation.reportWarning("Status=" + status.toString(), false);
//			if (shooterStage2Left.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
//	            DriverStation.reportError("Could not detect shooter stage 2 encoder!", false);
//	        }
	        shooterStage2Left.SetVelocityMeasurementPeriod(VelocityMeasurementPeriod.Period_10Ms);
//	        shooterStage2Left.SetVelocityMeasurementWindow(64);
	        
			shooterStage2Right = new CANTalon(RobotMap.SHOOTER_STAGE_2_RIGHT_MOTOR_CAN_ID);
			shooterStage2Right.changeControlMode(TalonControlMode.Follower);
			shooterStage2Right.set(shooterStage2Left.getDeviceID());
			shooterStage2Right.setSafetyEnabled(false);
			shooterStage2Right.enableBrakeMode(false);
			shooterStage2Right.reverseOutput(true);
			
			shooterStage1Left = new CANTalon(RobotMap.SHOOTER_STAGE_1_LEFT_MOTOR_CAN_ID);
			shooterStage1Left.clearStickyFaults();
			shooterStage1Left.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
//			shooterStage1Left.configEncoderCodesPerRev(ENCODER_TICKS_PER_REV);
			shooterStage1Left.changeControlMode(TalonControlMode.PercentVbus);
			shooterStage1Left.setSafetyEnabled(false);
			shooterStage1Left.enableBrakeMode(false);
			shooterStage1Left.setProfile(0);
			shooterStage1Left.setF(0.035);
			shooterStage1Left.setP(0.0); // 0.08 alone
			shooterStage1Left.setI(0.0001); //0.0001 alone
			shooterStage1Left.setIZone(2000);
			shooterStage1Left.setD(0);
			shooterStage1Left.setNominalClosedLoopVoltage(12.0);
//	        shooterStage1Left.SetVelocityMeasurementPeriod(VelocityMeasurementPeriod.Period_10Ms);
//			shooterStage1Left.configNominalOutputVoltage(0.0,0.0);
//			shooterStage1Left.configPeakOutputVoltage(12.0, 0);
//			shooterStage1Left.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 10);
//	        if (shooterStage1Left.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
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
		Robot.ledLights.setShooterWheelsOn(vbus > 0);
	}
	
	public boolean isShooterOn() {
		return shooterStage2Left.get() > 0.01;
	}

	public void setStage1Speed(double vbus) {
		shooterStage1Left.changeControlMode(TalonControlMode.PercentVbus);
		shooterStage1Left.set(vbus);
	}
	
	public void setStage2Rpm(double rpm) {
		shooterStage2Left.changeControlMode(TalonControlMode.Speed);
		shooterStage2Left.set(rpm);
		Robot.ledLights.setShooterWheelsOn(rpm > 0);
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
	
	public ShotState getShotPosition() {
		return (shotPosition.get() == true) ? ShotState.FAR : ShotState.CLOSE;
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

		if (operationMode == Robot.OperationMode.TEST) {
		SmartDashboard.putBoolean("Shooter On", isShooterOn());
    }
	}
}

