
package org.usfirst.frc.team3310.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;
import org.usfirst.frc.team3310.utility.CANTalonEncoder;
import org.usfirst.frc.team3310.utility.ControlLoopable;
import org.usfirst.frc.team3310.utility.MPTalonPIDController;
import org.usfirst.frc.team3310.utility.PIDParams;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearIntake extends Subsystem implements ControlLoopable {
    
	private static enum LiftControlMode { MANUAL, MP_POSITION };

	private static final double ENCODER_TICKS_TO_WORLD = (4096.0 / 360.0) * (32.0 / 18.0); 

	public final static double RETRACTED_POSITION_DEG = 0;
	public final static double BALL_INTAKE_POSITION_DEG = 68.4;
	public final static double GEAR_INTAKE_POSITION_DEG = 68.4; //128;
	public final static double GEAR_PRESENT_POSITION_DEG = 13; //0;
	
	// Motion profile max velocities and accel times
	public static final double RETRACT_MAX_RATE_DEG_PER_SEC = 650;
	public static final double DEPLOY_MAX_RATE_DEG_PER_SEC = 650;
	
	public static final double MP_T1 = 200;
	public static final double MP_T2 = 100;

	private CANTalonEncoder liftMotor;
	private ArrayList<CANTalonEncoder> motorControllers = new ArrayList<CANTalonEncoder>();	
	
	private MPTalonPIDController mpController;
	private PIDParams mpPIDParams = new PIDParams(4.0, 0.0, 0, 0.0, 0.2);
	private boolean isAtTarget = true;
	private LiftControlMode controlMode = LiftControlMode.MANUAL;

	public GearIntake() {
		try {
			liftMotor = new CANTalonEncoder(RobotMap.GEAR_INTAKE_LIFT_MOTOR_CAN_ID, ENCODER_TICKS_TO_WORLD, false, FeedbackDevice.QuadEncoder);
			liftMotor.setSafetyEnabled(false);
			liftMotor.reverseSensor(false);
			liftMotor.reverseOutput(false);
	        liftMotor.clearStickyFaults();
//	        if (liftMotor.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
//	            DriverStation.reportError("Could not detect gear intake encoder!", false);
//	        }

			motorControllers.add(liftMotor);
			mpPIDParams.iZone = 128;
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the GearIntake constructor");
		}
	}
	
	public void setLiftPosition(double targetAngleDegrees) {		
		double startAngleDegrees = getLiftPosition();
		double maxVelocityDegreePerSec = (targetAngleDegrees > startAngleDegrees) ? DEPLOY_MAX_RATE_DEG_PER_SEC : RETRACT_MAX_RATE_DEG_PER_SEC;
		controlMode = LiftControlMode.MP_POSITION;
		mpController.setMPTarget(startAngleDegrees, targetAngleDegrees, maxVelocityDegreePerSec, MP_T1, MP_T2); 
		isAtTarget = false;
	}
		
	public double getLiftPosition() {
		return liftMotor.getPositionWorld();
	}
	
	public void setZeroLiftPosition() {
		mpController.setZeroPosition();
	}
	
	public void setLiftSpeed(double speed) {
		controlMode = LiftControlMode.MANUAL;
		liftMotor.changeControlMode(TalonControlMode.PercentVbus);
		liftMotor.set(speed);
	}

	@Override
	public void controlLoopUpdate() {
		if (controlMode != LiftControlMode.MANUAL && !isAtTarget) {
			isAtTarget = mpController.controlLoopUpdate();
		}
	}
	
	public boolean isAtTarget() {
		return isAtTarget;
	}

	@Override
	public void setPeriodMs(long periodMs) {
		mpController = new MPTalonPIDController(periodMs, mpPIDParams, motorControllers);
		
		// Set the startup position to zero
		setZeroLiftPosition();
	}
	
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Gear Intake Position (deg)", getLiftPosition());
	}

	public void initDefaultCommand() {
    }
}

