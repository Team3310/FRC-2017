package org.usfirst.frc.team3310.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team3310.robot.OI;
import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;
import org.usfirst.frc.team3310.utility.BHRMathUtils;
import org.usfirst.frc.team3310.utility.BHR_ADSXRS453_Gyro;
import org.usfirst.frc.team3310.utility.CANTalonEncoder;
import org.usfirst.frc.team3310.utility.ControlLoopable;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import org.usfirst.frc.team3310.utility.MPTalonPIDController;
import org.usfirst.frc.team3310.utility.MotionProfilePoint;
import org.usfirst.frc.team3310.utility.PIDParams;
import org.usfirst.frc.team3310.utility.SoftwarePIDController;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Subsystem implements ControlLoopable
{
	public static enum DriveControlMode { JOYSTICK, MP_STRAIGHT, MP_TURN, PID_TURN, HOLD, TEST };
	public static enum SpeedShiftState { HI, LO };
	public static enum ClimberState { DEPLOYED, RETRACTED };

	public static final double TRACK_WIDTH_INCHES = 20;
	public static final double ENCODER_TICKS_TO_INCHES = 4096 / (3.72 * Math.PI); //3.80
	
	public static final double VOLTAGE_RAMP_RATE = 96;  // Volts per second

	// Motion profile max velocities and accel times
	public static final double MAX_TURN_RATE_DEG_PER_SEC = 320;
	public static final double MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC = 72;
	public static final double MP_AUTON_LOWBAR_VELOCITY_INCHES_PER_SEC = 90;
	public static final double MP_AUTON_CDF_VELOCITY_INCHES_PER_SEC = 50;
	public static final double MP_AUTON_PORTCULLIS_VELOCITY_INCHES_PER_SEC = 80;
	public static final double MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC = 108;
	public static final double MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC = 180;
	public static final double MP_LASER_SEARCH_VELOCITY_INCHES_PER_SEC = 30;
	
	public static final double MP_STRAIGHT_T1 = 600;
	public static final double MP_STRAIGHT_T2 = 300;
	public static final double MP_TURN_T1 = 600;
	public static final double MP_TURN_T2 = 300;
	public static final double MP_MAX_TURN_T1 = 400;
	public static final double MP_MAX_TURN_T2 = 200;
	
	// Motor controllers
	private ArrayList<CANTalonEncoder> motorControllers = new ArrayList<CANTalonEncoder>();	

	private CANTalonEncoder leftDrive1;
	private CANTalon leftDrive2;
	private CANTalon leftDrive3;
	
	private CANTalonEncoder rightDrive1;
	private CANTalon rightDrive2;
	private CANTalon rightDrive3;

	private RobotDrive m_drive;

	// Pneumatics
	private Solenoid speedShift;

	// Input devices
	public static final int DRIVER_INPUT_JOYSTICK_ARCADE = 0;
	public static final int DRIVER_INPUT_JOYSTICK_TANK = 1;
	public static final int DRIVER_INPUT_JOYSTICK_CHEESY = 2;
	public static final int DRIVER_INPUT_XBOX_CHEESY = 3;
	public static final int DRIVER_INPUT_XBOX_ARCADE_LEFT = 4;
	public static final int DRIVER_INPUT_XBOX_ARCADE_RIGHT = 5;
	public static final int DRIVER_INPUT_WHEEL = 6;

	public static final double STEER_NON_LINEARITY = 0.5;
	public static final double MOVE_NON_LINEARITY = 1.0;
	
	public static final double STICK_DEADBAND = 0.02;

	private int m_moveNonLinear = 0;
	private int m_steerNonLinear = 3;

	private double m_moveScale = 1.0;
	private double m_steerScale = 1.0;

	private double m_moveInput = 0.0;
	private double m_steerInput = 0.0;

	private double m_moveOutput = 0.0;
	private double m_steerOutput = 0.0;

	private double m_moveTrim = 0.0;
	private double m_steerTrim = 0.0;

	private boolean isFinished;
	private DriveControlMode controlMode = DriveControlMode.JOYSTICK;
	
	private MPTalonPIDController mpStraightController;
	private PIDParams mpStraightPIDParams = new PIDParams(0.1, 0, 0, 0.005, 0.03, 0.15); 
	private PIDParams mpHoldPIDParams = new PIDParams(1, 0, 0, 0.0, 0.0, 0.0); 

	private MPSoftwarePIDController mpTurnController;
	private PIDParams mpTurnPIDParams = new PIDParams(0.09, 0.01, 0, 0.00025, 0.005, 0.0, 5); 
	
	private SoftwarePIDController pidTurnController;
	private PIDParams pidTurnPIDParams = new PIDParams(0.05, 0.007, .3, 0, 0, 0.0, 5);
	private double targetPIDAngle;

//	private BHR_ADSXRS453_Gyro gyro = new BHR_ADSXRS453_Gyro();
	private boolean useGyroLock;
	private double gyroLockAngleDeg;
	private double kPGyro = 0.04;
	private boolean isCalibrating = false;
	private double gyroOffsetDeg = 0;

	public Drive() {
		try {
			leftDrive1 = new CANTalonEncoder(RobotMap.DRIVETRAIN_LEFT_MOTOR1_CAN_ID, ENCODER_TICKS_TO_INCHES, false, FeedbackDevice.QuadEncoder);
			leftDrive2 = new CANTalon(RobotMap.DRIVETRAIN_LEFT_MOTOR2_CAN_ID);
			leftDrive3 = new CANTalon(RobotMap.DRIVETRAIN_LEFT_MOTOR3_CAN_ID);
			
			rightDrive1 = new CANTalonEncoder(RobotMap.DRIVETRAIN_RIGHT_MOTOR1_CAN_ID, ENCODER_TICKS_TO_INCHES, true, FeedbackDevice.QuadEncoder);
			rightDrive2 = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_MOTOR2_CAN_ID);
			rightDrive3 = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_MOTOR3_CAN_ID);
			
			leftDrive1.reverseSensor(true);
			leftDrive1.reverseOutput(false);
			leftDrive1.setVoltageRampRate(VOLTAGE_RAMP_RATE);
			leftDrive1.enableBrakeMode(true);
			
			leftDrive2.changeControlMode(TalonControlMode.Follower);
			leftDrive2.set(leftDrive1.getDeviceID());
			leftDrive2.enableBrakeMode(true);

			leftDrive3.changeControlMode(TalonControlMode.Follower);
			leftDrive3.set(leftDrive1.getDeviceID());
			leftDrive3.enableBrakeMode(true);
			
			rightDrive1.reverseSensor(false);
			rightDrive1.reverseOutput(true);
			rightDrive1.setVoltageRampRate(VOLTAGE_RAMP_RATE);
			rightDrive1.enableBrakeMode(true);
			
			rightDrive2.changeControlMode(TalonControlMode.Follower);
			rightDrive2.set(rightDrive1.getDeviceID());
			rightDrive2.enableBrakeMode(true);

			rightDrive3.changeControlMode(TalonControlMode.Follower);
			rightDrive3.set(rightDrive1.getDeviceID());
			rightDrive3.enableBrakeMode(true);
							
			motorControllers.add(leftDrive1);
			motorControllers.add(rightDrive1);
			
			m_drive = new RobotDrive(leftDrive1, rightDrive1);
			m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
			m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
			m_drive.setSafetyEnabled(false);
			
			speedShift = new Solenoid(RobotMap.DRIVETRAIN_SPEEDSHIFT_PCM_ID);
		}
		catch (Exception e) {
			System.err.println("An error occurred in the DriveTrain constructor");
		}
	}

	@Override
	public void initDefaultCommand() {
	}
	
	public double getGyroAngleDeg() {
		return 0; // gyro.getAngle() + gyroOffsetDeg;
	}
	
	public double getGyroRateDegPerSec() {
		return 0; // gyro.getRate();
	}
	
	public void resetGyro() {
		//gyro.reset();
	}
	
	public void calibrateGyro() {
		//gyro.calibrate();
	}
	
	public void endGyroCalibration() {
		if (isCalibrating == true) {
			//gyro.endCalibration();
			isCalibrating = false;
		}
	}
	
	public void setGyroOffset(double offsetDeg) {
		gyroOffsetDeg = offsetDeg;
	}

	public void setStraightMP(double distanceInches, double maxVelocity, boolean useGyroLock, boolean useAbsolute, double desiredAbsoluteAngle) {
		double yawAngle = useAbsolute ? BHRMathUtils.adjustAccumAngleToDesired(getGyroAngleDeg(), desiredAbsoluteAngle) : getGyroAngleDeg();
		mpStraightController.setPID(mpStraightPIDParams);
		mpStraightController.setMPStraightTarget(0, distanceInches, maxVelocity, MP_STRAIGHT_T1, MP_STRAIGHT_T2, useGyroLock, yawAngle, true); 
		setControlMode(DriveControlMode.MP_STRAIGHT);
	}
	
	public void setRelativeTurnMP(double relativeTurnAngleDeg, double turnRateDegPerSec, MPSoftwareTurnType turnType) {
		mpTurnController.setMPTurnTarget(getGyroAngleDeg(), relativeTurnAngleDeg + getGyroAngleDeg(), turnRateDegPerSec, MP_TURN_T1, MP_TURN_T2, turnType, TRACK_WIDTH_INCHES);
		setControlMode(DriveControlMode.MP_TURN);
	}
	
	public void setRelativeMaxTurnMP(double relativeTurnAngleDeg, double turnRateDegPerSec, MPSoftwareTurnType turnType) {
		mpTurnController.setMPTurnTarget(getGyroAngleDeg(), relativeTurnAngleDeg + getGyroAngleDeg(), turnRateDegPerSec, MP_MAX_TURN_T1, MP_MAX_TURN_T2, turnType, TRACK_WIDTH_INCHES);
		setControlMode(DriveControlMode.MP_TURN);
	}
	
	public void setAbsoluteTurnMP(double absoluteTurnAngleDeg, double turnRateDegPerSec, MPSoftwareTurnType turnType) {
		mpTurnController.setMPTurnTarget(getGyroAngleDeg(), BHRMathUtils.adjustAccumAngleToDesired(getGyroAngleDeg(), absoluteTurnAngleDeg), turnRateDegPerSec, MP_TURN_T1, MP_TURN_T2, turnType, TRACK_WIDTH_INCHES);
		setControlMode(DriveControlMode.MP_TURN);
	}
	
	public void setRelativeTurnPID(double relativeTurnAngleDeg, double maxError, double maxPrevError, MPSoftwareTurnType turnType) {
		this.targetPIDAngle = relativeTurnAngleDeg + getGyroAngleDeg();
		pidTurnController.setPIDTurnTarget(relativeTurnAngleDeg + getGyroAngleDeg(), maxError, maxPrevError, turnType);
		setControlMode(DriveControlMode.PID_TURN);
	}
	
	public void setDriveHold(boolean status) {
		if (status) {
			setControlMode(DriveControlMode.HOLD);
		}
		else {
			setControlMode(DriveControlMode.JOYSTICK);
		}
	}
	
	public void setControlMode(DriveControlMode controlMode) {
 		this.controlMode = controlMode;
		if (controlMode == DriveControlMode.JOYSTICK) {
			leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
			rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		}
		else if (controlMode == DriveControlMode.TEST) {
			leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
			rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		}
		else if (controlMode == DriveControlMode.HOLD) {
			mpStraightController.setPID(mpHoldPIDParams);
			leftDrive1.changeControlMode(TalonControlMode.Position);
			leftDrive1.setPosition(0);
			leftDrive1.set(0);
			rightDrive1.changeControlMode(TalonControlMode.Position);
			rightDrive1.setPosition(0);
			rightDrive1.set(0);
		}
		isFinished = false;
	}
	
	public void controlLoopUpdate() {
		if (controlMode == DriveControlMode.JOYSTICK) {
			driveWithJoystick();
		}
		else if (!isFinished) {
			if (controlMode == DriveControlMode.MP_STRAIGHT) {
				isFinished = mpStraightController.controlLoopUpdate(getGyroAngleDeg()); 
			}
			else if (controlMode == DriveControlMode.MP_TURN) {
				isFinished = mpTurnController.controlLoopUpdate(getGyroAngleDeg()); 
			}
			else if (controlMode == DriveControlMode.PID_TURN) {
				isFinished = pidTurnController.controlLoopUpdate(getGyroAngleDeg()); 
			}
		}
	}
	
	public void setSpeed(double speed) {
		if (speed == 0) {
			setControlMode(DriveControlMode.JOYSTICK);
		}
		else {
			setControlMode(DriveControlMode.TEST);
			rightDrive1.set(speed);
			leftDrive1.set(speed);
		}
	}
	
	public void setGyroLock(boolean useGyroLock, boolean snapToAbsolute0or180) {
		if (snapToAbsolute0or180) {
			gyroLockAngleDeg = BHRMathUtils.adjustAccumAngleToClosest180(getGyroAngleDeg());
		}
		else {
			gyroLockAngleDeg = getGyroAngleDeg();
		}
		this.useGyroLock = useGyroLock;
	}

	public void driveWithJoystick() {
		if(controlMode != DriveControlMode.JOYSTICK || m_drive == null) return;
		// switch(m_controllerMode) {
		// case CONTROLLER_JOYSTICK_ARCADE:
		// m_moveInput = OI.getInstance().getJoystick1().getY();
		// m_steerInput = OI.getInstance().getJoystick1().getX();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
		// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_JOYSTICK_TANK:
		// m_moveInput = OI.getInstance().getJoystick1().getY();
		// m_steerInput = OI.getInstance().getJoystick2().getY();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_steerInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_drive.tankDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_JOYSTICK_CHEESY:
		// m_moveInput = OI.getInstance().getJoystick1().getY();
		// m_steerInput = OI.getInstance().getJoystick2().getX();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
		// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_XBOX_CHEESY:
		// boolean turbo = OI.getInstance().getDriveTrainController()
		// .getLeftJoystickButton();
		// boolean slow = OI.getInstance().getDriveTrainController()
		// .getRightJoystickButton();
		// double speedToUseMove, speedToUseSteer;
		// if (turbo && !slow) {
		// speedToUseMove = m_moveScaleTurbo;
		// speedToUseSteer = m_steerScaleTurbo;
		// } else if (!turbo && slow) {
		// speedToUseMove = m_moveScaleSlow;
		// speedToUseSteer = m_steerScaleSlow;
		// } else {
		// speedToUseMove = m_moveScale;
		// speedToUseSteer = m_steerScale;
		// }

		// m_moveInput =
		// OI.getInstance().getDriveTrainController().getLeftYAxis();
		// m_steerInput =
		// OI.getInstance().getDriveTrainController().getRightXAxis();
		m_moveInput = -OI.getInstance().getDriverJoystickPower().getY();
		m_steerInput = -OI.getInstance().getDriverJoystickTurn().getX();

		m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
				m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
				m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		
		if (useGyroLock) {
			double yawError = gyroLockAngleDeg - getGyroAngleDeg();
			m_steerOutput = kPGyro * yawError;
		}
		
		m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_XBOX_ARCADE_RIGHT:
		// m_moveInput =
		// OI.getInstance().getDrivetrainController().getRightYAxis();
		// m_steerInput =
		// OI.getInstance().getDrivetrainController().getRightXAxis();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
		// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_XBOX_ARCADE_LEFT:
		// m_moveInput =
		// OI.getInstance().getDrivetrainController().getLeftYAxis();
		// m_steerInput =
		// OI.getInstance().getDrivetrainController().getLeftXAxis();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
		// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// }
	}

	private boolean inDeadZone(double input) {
		boolean inDeadZone;
		if (Math.abs(input) < STICK_DEADBAND) {
			inDeadZone = true;
		} else {
			inDeadZone = false;
		}
		return inDeadZone;
	}

	private double adjustForSensitivity(double scale, double trim,
			double steer, int nonLinearFactor, double wheelNonLinearity) {
		if (inDeadZone(steer))
			return 0;

		steer += trim;
		steer *= scale;
		steer = limitValue(steer);

		int iterations = Math.abs(nonLinearFactor);
		for (int i = 0; i < iterations; i++) {
			if (nonLinearFactor > 0) {
				steer = nonlinearStickCalcPositive(steer, wheelNonLinearity);
			} else {
				steer = nonlinearStickCalcNegative(steer, wheelNonLinearity);
			}
		}
		return steer;
	}

	private double limitValue(double value) {
		if (value > 1.0) {
			value = 1.0;
		} else if (value < -1.0) {
			value = -1.0;
		}
		return value;
	}

	private double nonlinearStickCalcPositive(double steer,
			double steerNonLinearity) {
		return Math.sin(Math.PI / 2.0 * steerNonLinearity * steer)
				/ Math.sin(Math.PI / 2.0 * steerNonLinearity);
	}

	private double nonlinearStickCalcNegative(double steer,
			double steerNonLinearity) {
		return Math.asin(steerNonLinearity * steer)
				/ Math.asin(steerNonLinearity);
	}

	public void setShiftState(SpeedShiftState state) {
		if(state == SpeedShiftState.HI) {
			speedShift.set(true);
		}
		else if(state == SpeedShiftState.LO) {
			speedShift.set(false);
		}
	}

	public boolean isFinished() {
		return isFinished;
	}
	
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	@Override
	public void setPeriodMs(long periodMs) {
		mpStraightController = new MPTalonPIDController(periodMs, mpStraightPIDParams, motorControllers);
		mpTurnController = new MPSoftwarePIDController(periodMs, mpTurnPIDParams, motorControllers);
		pidTurnController = new SoftwarePIDController(pidTurnPIDParams, motorControllers);
	}
	
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Yaw Angle Deg", getGyroAngleDeg());
		if (operationMode == Robot.OperationMode.TEST) {
			try {
				SmartDashboard.putNumber("Right Pos Inches", rightDrive1.getPositionWorld());
				SmartDashboard.putNumber("Left Pos Inches", leftDrive1.getPositionWorld());
				SmartDashboard.putNumber("Right Vel Ft-Sec", rightDrive1.getVelocityWorld() / 12);
				SmartDashboard.putNumber("Left Vel Ft-Sec", leftDrive1.getVelocityWorld() / 12);
				SmartDashboard.putNumber("Left 1 Amps", Robot.pdp.getCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR1_CAN_ID));
				SmartDashboard.putNumber("Left 2 Amps", Robot.pdp.getCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR2_CAN_ID));
				SmartDashboard.putNumber("Left 3 Amps", Robot.pdp.getCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR3_CAN_ID));
				SmartDashboard.putNumber("Right 1 Amps", Robot.pdp.getCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR1_CAN_ID));
				SmartDashboard.putNumber("Right 2 Amps", Robot.pdp.getCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR2_CAN_ID));
				SmartDashboard.putNumber("Right 3 Amps", Robot.pdp.getCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR3_CAN_ID));
				SmartDashboard.putBoolean("Drive Hold", controlMode == DriveControlMode.HOLD);
				MotionProfilePoint mpPoint = mpTurnController.getCurrentPoint(); 
				double delta = mpPoint != null ? getGyroAngleDeg() - mpTurnController.getCurrentPoint().position : 0;
				SmartDashboard.putNumber("Gyro Delta", delta);
				SmartDashboard.putBoolean("Gyro Calibrating", isCalibrating);
				SmartDashboard.putNumber("Gyro Offset", gyroOffsetDeg);
				SmartDashboard.putNumber("Yaw Rate", getGyroRateDegPerSec());
				SmartDashboard.putNumber("Delta PID Angle", targetPIDAngle - getGyroAngleDeg());
				SmartDashboard.putNumber("Steer Output", m_steerOutput);
				SmartDashboard.putNumber("Move Output", m_moveOutput);
				SmartDashboard.putNumber("Steer Input", m_steerInput);
				SmartDashboard.putNumber("Move Input", m_moveInput);
			}
			catch (Exception e) {
				System.err.println("Drivetrain update status error");
			}
		}
	}	
}