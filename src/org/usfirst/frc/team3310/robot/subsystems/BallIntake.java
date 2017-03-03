
package org.usfirst.frc.team3310.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.utility.CANTalonEncoder;
import org.usfirst.frc.team3310.utility.ControlLoopable;
import org.usfirst.frc.team3310.utility.MPTalonPIDController;
import org.usfirst.frc.team3310.utility.PIDParams;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BallIntake extends Subsystem implements ControlLoopable {
    
	private static enum LiftControlMode { MANUAL, MP_POSITION };

	private static final double ENCODER_TICKS_TO_WORLD = (4096.0 / 360.0) * (32.0 / 18.0);  // 18/16 practice

	public static final double BALL_INTAKE_LOAD_SPEED = 0.8;
	public static final double BALL_INTAKE_EJECT_SPEED = -1.0;
	public static final double BALL_INTAKE_OFF_SPEED = 0.0;
	
	public final static double RETRACT_POSITION_DEG = 0;
	public final static double BALL_INTAKE_POSITION_DEG = 100.5; // Roxanne 99.0;
	public final static double GEAR_INTAKE_POSITION_DEG = 117; // Roxanne 120.0;
	public final static double GEAR_PRESENT_POSITION_DEG = 10; //0;
	public final static double GEAR_DEPLOY_POSITION_DEG = GEAR_PRESENT_POSITION_DEG;
	public final static double SHOOT_POSITION_DEG = 58;
	
	// Motion profile max velocities and accel times
	public static final double RETRACT_MAX_RATE_DEG_PER_SEC = 450;
	public static final double DEPLOY_MAX_RATE_DEG_PER_SEC = 400;
	
	public static final double MP_T1 = 300;
	public static final double MP_T2 = 150;

	private CANTalon rollerMotor;
	private CANTalonEncoder liftMotor;
	private ArrayList<CANTalonEncoder> motorControllers = new ArrayList<CANTalonEncoder>();	
	
	private MPTalonPIDController mpController;
	private PIDParams mpPIDParams = new PIDParams(4.0, 0.0, 0, 0.0, 0.2);
	private boolean isAtTarget = true;
	private LiftControlMode controlMode = LiftControlMode.MANUAL;

	public BallIntake() {
		try {
			rollerMotor = new CANTalon(RobotMap.BALL_INTAKE_ROLLER_MOTOR_CAN_ID);

			liftMotor = new CANTalonEncoder(RobotMap.BALL_INTAKE_LIFT_MOTOR_CAN_ID, ENCODER_TICKS_TO_WORLD, false, FeedbackDevice.QuadEncoder);
	        liftMotor.clearStickyFaults();
			liftMotor.setSafetyEnabled(false);
			liftMotor.reverseSensor(true);
			liftMotor.reverseOutput(true);
//	        if (liftMotor.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Absolute) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
//	            DriverStation.reportError("Could not detect ball intake encoder!", false);
//	        }
	        
			motorControllers.add(liftMotor);
			mpPIDParams.iZone = 128;
			setLiftSpeed(0.0);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the BallIntake constructor");
		}
	}
	
	public void setRollerSpeed(double speed) {
		rollerMotor.set(-speed);
		Robot.ledLights.setIntakeRollerOn(Math.abs(speed) > 0.01);
	}
	
	public void setLiftPosition(IntakePosition position) {
		double targetAngleDegrees = 0;
		if (position == IntakePosition.BALL_INTAKE) {
			targetAngleDegrees = BALL_INTAKE_POSITION_DEG;
		}
		else if (position == IntakePosition.GEAR_INTAKE) {
			targetAngleDegrees = GEAR_INTAKE_POSITION_DEG;
		}
		else if (position == IntakePosition.GEAR_PRESENT) {
			targetAngleDegrees = GEAR_PRESENT_POSITION_DEG;
		}
		else if (position == IntakePosition.GEAR_DEPLOY) {
			targetAngleDegrees = GEAR_DEPLOY_POSITION_DEG;
		}
		else if (position == IntakePosition.RETRACT) {
			targetAngleDegrees = RETRACT_POSITION_DEG;
		}
		else if (position == IntakePosition.SHOOT) {
			targetAngleDegrees = SHOOT_POSITION_DEG;
		}
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
		mpController.resetZeroPosition();
	}
	
	public void setLiftSpeed(double speed) {
		controlMode = LiftControlMode.MANUAL;
		liftMotor.changeControlMode(TalonControlMode.PercentVbus);
		liftMotor.set(-speed);
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
		SmartDashboard.putNumber("Ball Intake Position (deg)", getLiftPosition());
		SmartDashboard.putNumber("Ball Intake Absolute Position (deg)", liftMotor.getPulseWidthPosition());
	}

	public void initDefaultCommand() {
    }
}

