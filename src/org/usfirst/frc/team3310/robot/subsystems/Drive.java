package org.usfirst.frc.team3310.robot.subsystems;

import java.util.ArrayList;
import java.util.Set;

import org.usfirst.frc.team3310.robot.Constants;
import org.usfirst.frc.team3310.robot.OI;
import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;
import org.usfirst.frc.team3310.utility.AdaptivePurePursuitController;
import org.usfirst.frc.team3310.utility.BHRMathUtils;
import org.usfirst.frc.team3310.utility.CANTalonEncoder;
import org.usfirst.frc.team3310.utility.ControlLoopable;
import org.usfirst.frc.team3310.utility.Kinematics;
import org.usfirst.frc.team3310.utility.MMTalonPIDController;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import org.usfirst.frc.team3310.utility.MPTalonPIDController;
import org.usfirst.frc.team3310.utility.MPTalonPIDPathController;
import org.usfirst.frc.team3310.utility.MPTalonPIDPathVelocityController;
import org.usfirst.frc.team3310.utility.MotionProfilePoint;
import org.usfirst.frc.team3310.utility.PIDParams;
import org.usfirst.frc.team3310.utility.Path;
import org.usfirst.frc.team3310.utility.PathGenerator;
import org.usfirst.frc.team3310.utility.RigidTransform2d;
import org.usfirst.frc.team3310.utility.Rotation2d;
import org.usfirst.frc.team3310.utility.SoftwarePIDController;
import org.usfirst.frc.team3310.utility.Translation2d;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.ctre.PigeonImu;
import com.ctre.PigeonImu.CalibrationMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Subsystem implements ControlLoopable
{
	public static enum DriveControlMode { JOYSTICK, MP_STRAIGHT, MP_TURN, PID_TURN, HOLD, MANUAL, CLIMB, MP_PATH, MP_PATH_VELOCITY, MOTION_MAGIC, ADAPTIVE_PURSUIT };
	public static enum SpeedShiftState { HI, LO };
	public static enum ClimberState { DEPLOYED, RETRACTED };

	public static final double TRACK_WIDTH_INCHES = 26.937;
	public static final double ENCODER_TICKS_TO_INCHES = 4096 / (3.7 * Math.PI); //3.70
	public static final double CLIMB_SPEED = 0.45;
	
	public static final double VOLTAGE_RAMP_RATE = 150;  // Volts per second

	// Motion profile max velocities and accel times
	public static final double MAX_TURN_RATE_DEG_PER_SEC = 320;
	public static final double MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC =  120;  //72;
	public static final double MP_AUTON_MAX_BOILER_STRAIGHT_VELOCITY_INCHES_PER_SEC =  200;  
	public static final double MP_AUTON_MAX_LO_GEAR_STRAIGHT_VELOCITY_INCHES_PER_SEC =  320;  
	public static final double MP_AUTON_MAX_HIGH_GEAR_STRAIGHT_VELOCITY_INCHES_PER_SEC =  400;  
	public static final double MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC =  270;
	public static final double MP_AUTON_MAX_BOILER_TURN_RATE_DEG_PER_SEC =  400;
	public static final double MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC = 25;
	public static final double MP_GEAR_DEPLOY_FASTER_VELOCITY_INCHES_PER_SEC = 80;
	
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

	private DigitalInput hopperSensorRed;
	private DigitalInput hopperSensorBlue;

	
	private double climbSpeed;
	
	private boolean isRed = true;
	
	private double periodMs;

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
	private int m_steerNonLinear = -3;

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
//	private PIDParams mpStraightPIDParams = new PIDParams(0.1, 0, 0, 0.005, 0.03, 0.15);  // 4 colsons
	private PIDParams mpStraightPIDParams = new PIDParams(0.1, 0, 0, 0.005, 0.03, 0.03);  // 4 omni
	private PIDParams mpHoldPIDParams = new PIDParams(1, 0, 0, 0.0, 0.0, 0.0); 

	private MMTalonPIDController mmStraightController;
	private PIDParams mmStraightPIDParams = new PIDParams(0, 0, 0, 0.24);  
	
	private MPSoftwarePIDController mpTurnController; // p    i   d     a      v      g    izone
//	private PIDParams mpTurnPIDParams = new PIDParams(0.07, 0.00002, 0.5, 0.00025, 0.008, 0.0, 100);  // 4 colson wheels
	private PIDParams mpTurnPIDParams = new PIDParams(0.03, 0.00002, 0.4, 0.0004, 0.0030, 0.0, 100);  // 4 omni
	
	private SoftwarePIDController pidTurnController;
	private PIDParams pidTurnPIDParams = new PIDParams(0.04, 0.001, 0.4, 0, 0, 0.0, 100); //i=0.0008
	private double targetPIDAngle;

	private MPTalonPIDPathController mpPathController;
	private PIDParams mpPathPIDParams = new PIDParams(0.1, 0, 0, 0.005, 0.03, 0.28, 100);  // 4 omni   g=.3

	private MPTalonPIDPathVelocityController mpPathVelocityController;
	private PIDParams mpPathVelocityPIDParams = new PIDParams(0.5, 0.001, 5, 0.44); 

	private AdaptivePurePursuitController adaptivePursuitController;
	private PIDParams adaptivePursuitPIDParams = new PIDParams(0.1, 0.00, 1, 0.44); 
	
	private RigidTransform2d zeroPose = new RigidTransform2d(new Translation2d(0,0), Rotation2d.fromDegrees(0));
	private RigidTransform2d currentPose = zeroPose;
	private RigidTransform2d lastPose = zeroPose;
    private double left_encoder_prev_distance_ = 0;
    private double right_encoder_prev_distance_ = 0;

	private PigeonImu gyroPigeon;
	private double[] yprPigeon = new double[3];
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
			
			gyroPigeon = new PigeonImu(leftDrive2);
			
			hopperSensorRed = new DigitalInput(RobotMap.HOPPER_SENSOR_RED_DIO_ID);
			hopperSensorBlue = new DigitalInput(RobotMap.HOPPER_SENSOR_BLUE_DIO_ID);
	
			leftDrive1.clearStickyFaults();
			leftDrive1.reverseSensor(true);
			leftDrive1.reverseOutput(true);
			leftDrive1.setVoltageRampRate(VOLTAGE_RAMP_RATE);
			leftDrive1.setSafetyEnabled(false);
			leftDrive1.enableBrakeMode(true);
			leftDrive1.setNominalClosedLoopVoltage(12.0);
//	        if (leftDrive1.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
//	            Driver.reportError("Could not detect left drive encoder encoder!", false);
//	        }
			
			leftDrive2.changeControlMode(TalonControlMode.Follower);
			leftDrive2.set(leftDrive1.getDeviceID());
			leftDrive2.setSafetyEnabled(false);
			leftDrive2.enableBrakeMode(true);

			leftDrive3.changeControlMode(TalonControlMode.Follower);
			leftDrive3.set(leftDrive1.getDeviceID());
			leftDrive3.setSafetyEnabled(false);
			leftDrive3.enableBrakeMode(true);
			
			rightDrive1.clearStickyFaults();
			rightDrive1.reverseSensor(false);
			rightDrive1.reverseOutput(false);
			rightDrive1.setVoltageRampRate(VOLTAGE_RAMP_RATE);
			rightDrive1.setSafetyEnabled(false);
			rightDrive1.enableBrakeMode(true);
			rightDrive1.setNominalClosedLoopVoltage(12.0);
//	        if (rightDrive1.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
//	            DriverStation.reportError("Could not detect right drive encoder encoder!", false);
//	        }
			
			rightDrive2.changeControlMode(TalonControlMode.Follower);
			rightDrive2.set(rightDrive1.getDeviceID());
			rightDrive2.setSafetyEnabled(false);
			rightDrive2.enableBrakeMode(true);

			rightDrive3.changeControlMode(TalonControlMode.Follower);
			rightDrive3.set(rightDrive1.getDeviceID());
			rightDrive3.setSafetyEnabled(false);
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
		return getGyroPigeonAngleDeg();
	}
	
	public double getGyroPigeonAngleDeg() {
		gyroPigeon.GetYawPitchRoll(yprPigeon);
		return -yprPigeon[0] + gyroOffsetDeg;
	}
			
	public void resetGyro() {
		gyroPigeon.SetYaw(0);
	}
	
	public void resetEncoders() {
		mpStraightController.resetZeroPosition();
	}
	
	public void calibrateGyro() {
		gyroPigeon.EnterCalibrationMode(CalibrationMode.Temperature);
	}
	
	public void endGyroCalibration() {
		if (isCalibrating == true) {
			isCalibrating = false;
		}
	}
	
	public void setGyroOffset(double offsetDeg) {
		gyroOffsetDeg = offsetDeg;
	}
	
	public boolean isHopperSensorRedOn() {
		return hopperSensorRed.get();
	}

	public boolean isHopperSensorBlueOn() {
		return hopperSensorBlue.get();
	}
	
	public boolean isHopperSensorOn() {
		if (isRed() == true) {
			return isHopperSensorRedOn();
		}		
		else {
			return isHopperSensorBlueOn();
		}
	}

	public void setStraightMM(double distanceInches, double maxVelocity, double maxAcceleration, boolean useGyroLock, boolean useAbsolute, double desiredAbsoluteAngle) {
		double yawAngle = useAbsolute ? BHRMathUtils.adjustAccumAngleToDesired(getGyroAngleDeg(), desiredAbsoluteAngle) : getGyroAngleDeg();
		mmStraightController.setPID(mmStraightPIDParams);
		mmStraightController.setMMStraightTarget(0, distanceInches, maxVelocity, maxAcceleration, useGyroLock, yawAngle, true); 
		setControlMode(DriveControlMode.MOTION_MAGIC);
	}
	
	public void setStraightMP(double distanceInches, double maxVelocity, boolean useGyroLock, boolean useAbsolute, double desiredAbsoluteAngle) {
		double yawAngle = useAbsolute ? BHRMathUtils.adjustAccumAngleToDesired(getGyroAngleDeg(), desiredAbsoluteAngle) : getGyroAngleDeg();
		mpStraightController.setPID(mpStraightPIDParams);
		mpStraightController.setMPStraightTarget(0, distanceInches, maxVelocity, MP_STRAIGHT_T1, MP_STRAIGHT_T2, useGyroLock, yawAngle, true); 
		setControlMode(DriveControlMode.MP_STRAIGHT);
	}
	
	public void setStraightMPCached(String key, boolean useGyroLock, boolean useAbsolute, double desiredAbsoluteAngle) {
		double yawAngle = useAbsolute ? BHRMathUtils.adjustAccumAngleToDesired(getGyroAngleDeg(), desiredAbsoluteAngle) : getGyroAngleDeg();
		mpStraightController.setPID(mpStraightPIDParams);
		mpStraightController.setMPStraightTarget(key, useGyroLock, yawAngle, true); 
		setControlMode(DriveControlMode.MP_STRAIGHT);
	}
	
	public void setRelativeTurnMP(double relativeTurnAngleDeg, double turnRateDegPerSec, MPSoftwareTurnType turnType) {
		mpTurnController.setMPTurnTarget(getGyroAngleDeg(), relativeTurnAngleDeg + getGyroAngleDeg(), turnRateDegPerSec, MP_TURN_T1, MP_TURN_T2, turnType, TRACK_WIDTH_INCHES);
		setControlMode(DriveControlMode.MP_TURN);
	}
	
	public void setRelativeTurnMPCached(String key, MPSoftwareTurnType turnType) {
		mpTurnController.setMPTurnTarget(key, turnType, TRACK_WIDTH_INCHES);
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
	
	public void setAbsoluteTurnMPCached(String key, MPSoftwareTurnType turnType) {
		mpTurnController.setMPTurnTarget(key, turnType, TRACK_WIDTH_INCHES);
		setControlMode(DriveControlMode.MP_TURN);
	}
	
	public void setRelativeTurnPID(double relativeTurnAngleDeg, double maxError, double maxPrevError, MPSoftwareTurnType turnType) {
		this.targetPIDAngle = relativeTurnAngleDeg + getGyroAngleDeg();
		pidTurnController.setPIDTurnTarget(relativeTurnAngleDeg + getGyroAngleDeg(), maxError, maxPrevError, turnType);
		setControlMode(DriveControlMode.PID_TURN);
	}
	
	public void setPathMP(PathGenerator path) {
		mpPathController.setPID(mpPathPIDParams);
		mpPathController.setMPPathTarget(path); 
		setControlMode(DriveControlMode.MP_PATH);
	}
	
	public void setPathVelocityMP(PathGenerator path) {
		mpPathVelocityController.setPID(mpPathPIDParams);
		mpPathVelocityController.setMPPathTarget(path); 
		setControlMode(DriveControlMode.MP_PATH_VELOCITY);
	}
	
    public void setPathAdaptivePursuit(Path path, boolean reversed) {
    	currentPose = zeroPose;
    	lastPose = zeroPose;
        left_encoder_prev_distance_ = 0;
        right_encoder_prev_distance_ = 0;
        adaptivePursuitController.setPID(adaptivePursuitPIDParams);
    	adaptivePursuitController.setPath(Constants.kPathFollowingLookahead, Constants.kPathFollowingMaxAccel, path, reversed, 0.25); 
		setControlMode(DriveControlMode.ADAPTIVE_PURSUIT);
    }

    public void setDriveHold(boolean status) {
		if (status) {
			setControlMode(DriveControlMode.HOLD);
		}
		else {
			setControlMode(DriveControlMode.JOYSTICK);
		}
	}
    
    public void updatePose() {
        double left_distance = leftDrive1.getPositionWorld();
        double right_distance = rightDrive1.getPositionWorld();
        Rotation2d gyro_angle = Rotation2d.fromDegrees(-getGyroAngleDeg());
        lastPose = currentPose;
        currentPose = generateOdometryFromSensors(left_distance - left_encoder_prev_distance_, right_distance - right_encoder_prev_distance_, gyro_angle);
        left_encoder_prev_distance_ = left_distance;
        right_encoder_prev_distance_ = right_distance;
    }
    
    public RigidTransform2d generateOdometryFromSensors(double left_encoder_delta_distance, double right_encoder_delta_distance, Rotation2d current_gyro_angle) {
        return Kinematics.integrateForwardKinematics(lastPose, left_encoder_delta_distance, right_encoder_delta_distance, current_gyro_angle);
    }
	
    /**
     * Path Markers are an optional functionality that name the various
     * Waypoints in a Path with a String. This can make defining set locations
     * much easier.
     * 
     * @return Set of Strings with Path Markers that the robot has crossed.
     */
    public synchronized Set<String> getPathMarkersCrossed() {
        if (controlMode != DriveControlMode.ADAPTIVE_PURSUIT) {
            return null;
        } else {
            return adaptivePursuitController.getMarkersCrossed();
        }
    }

    public synchronized void setControlMode(DriveControlMode controlMode) {
 		this.controlMode = controlMode;
		if (controlMode == DriveControlMode.JOYSTICK) {
			leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
			rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		}
		else if (controlMode == DriveControlMode.MANUAL) {
			leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
			rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		}
		else if (controlMode == DriveControlMode.CLIMB) {
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
	
	public synchronized void controlLoopUpdate() {
		if (controlMode == DriveControlMode.JOYSTICK || controlMode == DriveControlMode.CLIMB) {
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
			else if (controlMode == DriveControlMode.MP_PATH) {
				isFinished = mpPathController.controlLoopUpdate(getGyroAngleDeg()); 
			}
			else if (controlMode == DriveControlMode.MP_PATH_VELOCITY) {
				isFinished = mpPathVelocityController.controlLoopUpdate(getGyroAngleDeg()); 
			}
			else if (controlMode == DriveControlMode.MOTION_MAGIC) {
				isFinished = mpStraightController.controlLoopUpdate(getGyroAngleDeg()); 
			}
			else if (controlMode == DriveControlMode.ADAPTIVE_PURSUIT) {
				updatePose();
				isFinished = adaptivePursuitController.controlLoopUpdate(currentPose); 
			}
		}
	}
	
	public void setSpeed(double speed) {
		if (speed == 0) {
			setControlMode(DriveControlMode.JOYSTICK);
		}
		else {
			setControlMode(DriveControlMode.MANUAL);
			rightDrive1.set(-speed);
			leftDrive1.set(speed);
		}
	}
	
	public void setClimbSpeed(double climbSpeed) {
		this.climbSpeed = climbSpeed;
		if (climbSpeed == 0) {
			setControlMode(DriveControlMode.JOYSTICK);
		}
		else {
			setControlMode(DriveControlMode.CLIMB);
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
		if(m_drive == null) return;
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
		m_moveInput = -OI.getInstance().getDriverController().getLeftYAxis();
		m_steerInput = -OI.getInstance().getDriverController().getRightXAxis();

		if (controlMode == DriveControlMode.JOYSTICK) {
			m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
					m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		}
		else if (controlMode == DriveControlMode.CLIMB) {
			m_moveOutput = climbSpeed;
		}
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

	public double adjustForSensitivity(double scale, double trim,
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

	public synchronized boolean isFinished() {
		return isFinished;
	}
	
	public synchronized void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	@Override
	public void setPeriodMs(long periodMs) {
		mmStraightController = new MMTalonPIDController(periodMs, mmStraightPIDParams, motorControllers);
		mpStraightController = new MPTalonPIDController(periodMs, mpStraightPIDParams, motorControllers);
		mpTurnController = new MPSoftwarePIDController(periodMs, mpTurnPIDParams, motorControllers);
		pidTurnController = new SoftwarePIDController(pidTurnPIDParams, motorControllers);
		mpPathController = new MPTalonPIDPathController(periodMs, mpPathPIDParams, motorControllers);
		mpPathVelocityController = new MPTalonPIDPathVelocityController(periodMs, mpPathVelocityPIDParams, motorControllers);
		adaptivePursuitController = new AdaptivePurePursuitController(periodMs, adaptivePursuitPIDParams, motorControllers);
		this.periodMs = periodMs;
	}
	
	public double getPeriodMs() {
		return periodMs;
	}
	
	public boolean isRed() {
		return isRed;
	}
	
	public void setIsRed(boolean status) {
		isRed = status;
	}
	
	public static double rotationsToInches(double rotations) {
        return rotations * (Constants.kDriveWheelDiameterInches * Math.PI);
    }

    public static double rpmToInchesPerSecond(double rpm) {
        return rotationsToInches(rpm) / 60;
    }

    public static double inchesToRotations(double inches) {
        return inches / (Constants.kDriveWheelDiameterInches * Math.PI);
    }

    public static double inchesPerSecondToRpm(double inches_per_second) {
        return inchesToRotations(inches_per_second) * 60;
    }

	public void updateStatus(Robot.OperationMode operationMode) {
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
				SmartDashboard.putBoolean("Hopper Sensor Red", isHopperSensorRedOn());
				SmartDashboard.putBoolean("Hopper Sensor Blue", isHopperSensorBlueOn());
				SmartDashboard.putBoolean("Drive Hold", controlMode == DriveControlMode.HOLD);
				SmartDashboard.putNumber("Yaw Angle Pigeon Deg", getGyroPigeonAngleDeg());
				MotionProfilePoint mpPoint = mpTurnController.getCurrentPoint(); 
				double delta = mpPoint != null ? getGyroAngleDeg() - mpTurnController.getCurrentPoint().position : 0;
				SmartDashboard.putNumber("Gyro Delta", delta);
				SmartDashboard.putBoolean("Gyro Calibrating", isCalibrating);
				SmartDashboard.putNumber("Gyro Offset", gyroOffsetDeg);
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
		else {
			
		}
	}	
	

}