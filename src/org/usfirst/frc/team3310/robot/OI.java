package org.usfirst.frc.team3310.robot;

import org.usfirst.frc.team3310.buttons.LogitechDPadTriggerButton;
import org.usfirst.frc.team3310.buttons.XBoxDPadTriggerButton;
import org.usfirst.frc.team3310.buttons.XBoxTriggerButton;
import org.usfirst.frc.team3310.controller.IHandController;
import org.usfirst.frc.team3310.controller.LogitechController;
import org.usfirst.frc.team3310.controller.XboxController;
import org.usfirst.frc.team3310.robot.commands.BallIntakeLiftResetZero;
import org.usfirst.frc.team3310.robot.commands.BallIntakeLiftSpeed;
import org.usfirst.frc.team3310.robot.commands.BallIntakeRollerSetSpeed;
import org.usfirst.frc.team3310.robot.commands.CameraOffset;
import org.usfirst.frc.team3310.robot.commands.CameraReadAndProcessImage;
import org.usfirst.frc.team3310.robot.commands.CameraReadImageTurnToBestTarget;
import org.usfirst.frc.team3310.robot.commands.CameraSaveImage;
import org.usfirst.frc.team3310.robot.commands.CameraTurnToBestTarget;
import org.usfirst.frc.team3310.robot.commands.CameraUpdateBestTarget;
import org.usfirst.frc.team3310.robot.commands.CameraUpdateDashboard;
import org.usfirst.frc.team3310.robot.commands.ClimberSetMaxAmps;
import org.usfirst.frc.team3310.robot.commands.ClimberSetSpeed;
import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DriveRelativeTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveSpeedShift;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.GearIntakeLiftResetZero;
import org.usfirst.frc.team3310.robot.commands.GearIntakeLiftSpeed;
import org.usfirst.frc.team3310.robot.commands.IntakeBalls;
import org.usfirst.frc.team3310.robot.commands.IntakeBallsOff;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.commands.MagicCarpetSetSpeed;
import org.usfirst.frc.team3310.robot.commands.ShootOff;
import org.usfirst.frc.team3310.robot.commands.ShootOn;
import org.usfirst.frc.team3310.robot.commands.ShooterAllOff;
import org.usfirst.frc.team3310.robot.commands.ShooterAllOn;
import org.usfirst.frc.team3310.robot.commands.ShooterFeedSetSpeed;
import org.usfirst.frc.team3310.robot.commands.ShooterSetRpmDashboard;
import org.usfirst.frc.team3310.robot.commands.ShooterSetShotPosition;
import org.usfirst.frc.team3310.robot.commands.ShooterSetSpeedDashboard;
import org.usfirst.frc.team3310.robot.commands.ShooterSetToggle;
import org.usfirst.frc.team3310.robot.commands.ShooterStage1SetRpmDashboard;
import org.usfirst.frc.team3310.robot.commands.ShooterStage1SetSpeed;
import org.usfirst.frc.team3310.robot.commands.ShooterStage2SetRpmDashboard;
import org.usfirst.frc.team3310.robot.commands.ShooterStage2SetSpeed;
import org.usfirst.frc.team3310.robot.subsystems.Climber;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	private static OI instance;

	private enum ControllerType {XBOX, LOGITECH, TEST};
	private ControllerType controllerType = ControllerType.XBOX;
	
	private LogitechController m_driverLogitech;
	private XboxController m_driverXbox;
	private XboxController m_operatorXbox;
	private IHandController m_controller;
	
	private OI() {
		
		//Logitech Controller
		if (controllerType == ControllerType.LOGITECH) {
			m_driverLogitech = new LogitechController(RobotMap.DRIVER_JOYSTICK_1_USB_ID);
			m_controller = m_driverLogitech;
	
	        JoystickButton shiftDrivetrain = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.LB_BUTTON);
	        shiftDrivetrain.whenPressed(new DriveSpeedShift(Drive.SpeedShiftState.HI));
	        shiftDrivetrain.whenReleased(new DriveSpeedShift(Drive.SpeedShiftState.LO));
	        
	        JoystickButton longShot = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.RB_BUTTON);
	        longShot.whenPressed(new ShootOn(ShotState.FAR));
	        longShot.whenReleased(new ShootOff());
	        
	        JoystickButton shortShot = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.RT_BUTTON);
	        shortShot.whenPressed(new ShootOn(ShotState.CLOSE));
	        shortShot.whenReleased(new ShootOff());
	        
	        JoystickButton ballIntake = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.LT_BUTTON);
	        ballIntake.whenPressed(new IntakeBalls());
	        ballIntake.whenReleased(new IntakeBallsOff());
	        
	        JoystickButton ballIntakeDeploy = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.Y_BUTTON);
	        ballIntakeDeploy.whenPressed(new IntakeSetPosition(IntakePosition.BALL_INTAKE));
	        
	        JoystickButton ballIntakeRetract = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.A_BUTTON);
	        ballIntakeRetract.whenPressed(new IntakeSetPosition(IntakePosition.RETRACT));
	        
	        JoystickButton gearIntakeDeploy = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.X_BUTTON);
	        gearIntakeDeploy.whenPressed(new IntakeSetPosition(IntakePosition.GEAR_INTAKE));
	        
	        JoystickButton gearIntakePresent = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.B_BUTTON);
	        gearIntakePresent.whenPressed(new IntakeSetPosition(IntakePosition.GEAR_PRESENT));
	        
	        JoystickButton climberForward = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.LEFT_JOYSTICK_BUTTON);
	        climberForward.whenPressed(new ClimberSetSpeed(Climber.CLIMB_SPEED));
	        climberForward.whenReleased(new ClimberSetSpeed(0.0));
	        
	        LogitechDPadTriggerButton climberForward2 = new LogitechDPadTriggerButton(m_driverLogitech, LogitechDPadTriggerButton.UP);
	        climberForward2.whenPressed(new ClimberSetSpeed(Climber.CLIMB_SPEED));
	        climberForward2.whenReleased(new ClimberSetSpeed(0.0));
	        
	        LogitechDPadTriggerButton climberReverse = new LogitechDPadTriggerButton(m_driverLogitech, LogitechDPadTriggerButton.DOWN);
	        climberReverse.whenPressed(new ClimberSetSpeed(-0.5));
	        climberReverse.whenReleased(new ClimberSetSpeed(0.0));
	        
	        JoystickButton toggleShooter = new JoystickButton(m_driverLogitech.getJoyStick(), LogitechController.START_BUTTON);
	        toggleShooter.whenPressed(new ShooterSetToggle(Shooter.SHOOTER_STAGE1_RPM_CLOSE, Shooter.SHOOTER_STAGE2_RPM_CLOSE)); 
		}
		
		else if (controllerType == ControllerType.XBOX) {
			m_driverXbox = new XboxController(RobotMap.DRIVER_JOYSTICK_1_USB_ID);
			m_controller = m_driverXbox;
	
	        JoystickButton shiftDrivetrain = new JoystickButton(m_driverXbox.getJoyStick(), XboxController.LEFT_BUMPER_BUTTON);
	        shiftDrivetrain.whenPressed(new DriveSpeedShift(Drive.SpeedShiftState.HI));
	        shiftDrivetrain.whenReleased(new DriveSpeedShift(Drive.SpeedShiftState.LO));
	        
	        JoystickButton longShot = new JoystickButton(m_driverXbox.getJoyStick(), XboxController.RIGHT_BUMPER_BUTTON);
	        longShot.whenPressed(new ShootOn(ShotState.FAR));
	        longShot.whenReleased(new ShootOff());
	        
	        XBoxTriggerButton shortShot = new XBoxTriggerButton(m_driverXbox, XBoxTriggerButton.RIGHT_TRIGGER);
	        shortShot.whenPressed(new ShootOn(ShotState.CLOSE));
	        shortShot.whenReleased(new ShootOff());
	        
	        XBoxTriggerButton ballIntake = new XBoxTriggerButton(m_driverXbox, XBoxTriggerButton.LEFT_TRIGGER);
	        ballIntake.whenPressed(new IntakeBalls());
	        ballIntake.whenReleased(new IntakeBallsOff());
	        
	        JoystickButton ballIntakeDeploy = new JoystickButton(m_driverXbox.getJoyStick(), XboxController.Y_BUTTON);
	        ballIntakeDeploy.whenPressed(new IntakeSetPosition(IntakePosition.BALL_INTAKE));
	        
	        JoystickButton ballIntakeRetract = new JoystickButton(m_driverXbox.getJoyStick(), XboxController.A_BUTTON);
	        ballIntakeRetract.whenPressed(new IntakeSetPosition(IntakePosition.GEAR_DEPLOY));
	        
	        JoystickButton gearIntakeDeploy = new JoystickButton(m_driverXbox.getJoyStick(), XboxController.X_BUTTON);
	        gearIntakeDeploy.whenPressed(new IntakeSetPosition(IntakePosition.GEAR_INTAKE));
	        
	        JoystickButton gearIntakePresent = new JoystickButton(m_driverXbox.getJoyStick(), XboxController.B_BUTTON);
	        gearIntakePresent.whenPressed(new IntakeSetPosition(IntakePosition.GEAR_PRESENT));
	        
	        JoystickButton climberForward = new JoystickButton(m_driverXbox.getJoyStick(), XboxController.LEFT_JOYSTICK_BUTTON);
	        climberForward.whenPressed(new ClimberSetSpeed(Climber.CLIMB_SPEED));
	        climberForward.whenReleased(new ClimberSetSpeed(0.0));
	        
	        XBoxDPadTriggerButton climberForward2 = new XBoxDPadTriggerButton(m_driverXbox, XBoxDPadTriggerButton.UP);
	        climberForward2.whenPressed(new ClimberSetSpeed(Climber.CLIMB_SPEED));
	        climberForward2.whenReleased(new ClimberSetSpeed(0.0));
	        
	        XBoxDPadTriggerButton climberReverse = new XBoxDPadTriggerButton(m_driverXbox, XBoxDPadTriggerButton.DOWN);
	        climberReverse.whenPressed(new ClimberSetSpeed(-0.5));
	        climberReverse.whenReleased(new ClimberSetSpeed(0.0));
	        
	        JoystickButton toggleShooter = new JoystickButton(m_driverXbox.getJoyStick(), XboxController.START_BUTTON);
	        toggleShooter.whenPressed(new ShooterSetToggle(Shooter.SHOOTER_STAGE1_RPM_CLOSE, Shooter.SHOOTER_STAGE2_RPM_CLOSE)); 
		}
		
		// Operator 
		m_operatorXbox = new XboxController(RobotMap.OPERATOR_JOYSTICK_1_USB_ID);

        JoystickButton longShotOperator = new JoystickButton(m_operatorXbox.getJoyStick(), XboxController.RIGHT_BUMPER_BUTTON);
        longShotOperator.whenPressed(new ShootOn(ShotState.FAR));
        longShotOperator.whenReleased(new ShootOff());
        
        XBoxTriggerButton shortShotOperator = new XBoxTriggerButton(m_operatorXbox, XBoxTriggerButton.RIGHT_TRIGGER);
        shortShotOperator.whenPressed(new ShootOn(ShotState.CLOSE));
        shortShotOperator.whenReleased(new ShootOff());
        
        XBoxTriggerButton ballIntakeOperator = new XBoxTriggerButton(m_operatorXbox, XBoxTriggerButton.LEFT_TRIGGER);
        ballIntakeOperator.whenPressed(new IntakeBalls());
        ballIntakeOperator.whenReleased(new IntakeBallsOff());
        
        JoystickButton ballIntakeDeployOperator = new JoystickButton(m_operatorXbox.getJoyStick(), XboxController.Y_BUTTON);
        ballIntakeDeployOperator.whenPressed(new IntakeSetPosition(IntakePosition.BALL_INTAKE));
        
        JoystickButton ballIntakeRetractOperator = new JoystickButton(m_operatorXbox.getJoyStick(), XboxController.A_BUTTON);
        ballIntakeRetractOperator.whenPressed(new IntakeSetPosition(IntakePosition.GEAR_DEPLOY));
        
        JoystickButton gearIntakeDeployOperator = new JoystickButton(m_operatorXbox.getJoyStick(), XboxController.X_BUTTON);
        gearIntakeDeployOperator.whenPressed(new IntakeSetPosition(IntakePosition.GEAR_INTAKE));
        
        JoystickButton gearIntakePresentOperator = new JoystickButton(m_operatorXbox.getJoyStick(), XboxController.B_BUTTON);
        gearIntakePresentOperator.whenPressed(new IntakeSetPosition(IntakePosition.GEAR_PRESENT));
        
        JoystickButton climberForwardOperator = new JoystickButton(m_operatorXbox.getJoyStick(), XboxController.LEFT_JOYSTICK_BUTTON);
        climberForwardOperator.whenPressed(new ClimberSetSpeed(Climber.CLIMB_SPEED));
        climberForwardOperator.whenReleased(new ClimberSetSpeed(0.0));
        
        XBoxDPadTriggerButton climberForward2Operator = new XBoxDPadTriggerButton(m_operatorXbox, XBoxDPadTriggerButton.UP);
        climberForward2Operator.whenPressed(new ClimberSetSpeed(Climber.CLIMB_SPEED));
        climberForward2Operator.whenReleased(new ClimberSetSpeed(0.0));
        
        XBoxDPadTriggerButton climberReverseOperator = new XBoxDPadTriggerButton(m_operatorXbox, XBoxDPadTriggerButton.DOWN);
        climberReverseOperator.whenPressed(new ClimberSetSpeed(-0.5));
        climberReverseOperator.whenReleased(new ClimberSetSpeed(0.0));
        
        JoystickButton toggleShooterOperator = new JoystickButton(m_operatorXbox.getJoyStick(), XboxController.START_BUTTON);
        toggleShooterOperator.whenPressed(new ShooterSetToggle(Shooter.SHOOTER_STAGE1_RPM_CLOSE, Shooter.SHOOTER_STAGE2_RPM_CLOSE)); 
		
        // SmartDashboard
		Button driveMP = new InternalButton();
		driveMP.whenPressed(new DriveStraightMP(96, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, false, 0));
		SmartDashboard.putData("Drive Straight", driveMP);
		
		Button turnRelativeMP = new InternalButton();
		turnRelativeMP.whenPressed(new DriveRelativeTurnMP(90, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
		SmartDashboard.putData("Turn Relative 90", turnRelativeMP);
		
		Button turnAbsoluteMP = new InternalButton();
		turnAbsoluteMP.whenPressed(new DriveAbsoluteTurnMP(90, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
		SmartDashboard.putData("Turn Absolute 90", turnAbsoluteMP);

		Button magicCarpetOn05 = new InternalButton();
		magicCarpetOn05.whenPressed(new MagicCarpetSetSpeed(0.5));
		SmartDashboard.putData("Magic Carpet 0.5", magicCarpetOn05);

		Button magicCarpetOff = new InternalButton();
		magicCarpetOff.whenPressed(new MagicCarpetSetSpeed(0.0));
		SmartDashboard.putData("Magic Carpet Off", magicCarpetOff);

		Button shooterFeedOn10 = new InternalButton();
		shooterFeedOn10.whenPressed(new ShooterFeedSetSpeed(1.0));
		SmartDashboard.putData("Shooter Feed 1.0", shooterFeedOn10);

		Button shooterFeedOn05 = new InternalButton();
		shooterFeedOn05.whenPressed(new ShooterFeedSetSpeed(0.5));
		SmartDashboard.putData("Shooter Feed 0.5", shooterFeedOn05);

		Button shooterFeedOff = new InternalButton();
		shooterFeedOff.whenPressed(new ShooterFeedSetSpeed(0.0));
		SmartDashboard.putData("Shooter Feed Off", shooterFeedOff);

		Button shooterStage2RPMOnDash = new InternalButton();
		shooterStage2RPMOnDash.whenPressed(new ShooterStage2SetRpmDashboard());
		SmartDashboard.putData("Shooter Stage 2 Dash RPM", shooterStage2RPMOnDash);

		Button shooterStage2On10 = new InternalButton();
		shooterStage2On10.whenPressed(new ShooterStage2SetSpeed(1.0));
		SmartDashboard.putData("Shooter Stage 2 - 1.0", shooterStage2On10);

		Button shooterStage2On09 = new InternalButton();
		shooterStage2On09.whenPressed(new ShooterStage2SetSpeed(0.9));
		SmartDashboard.putData("Shooter Stage 2 - 0.9", shooterStage2On09);

		Button shooterStage2On08 = new InternalButton();
		shooterStage2On08.whenPressed(new ShooterStage2SetSpeed(0.8));
		SmartDashboard.putData("Shooter Stage 2 - 0.8", shooterStage2On08);

		Button shooterStage2On07 = new InternalButton();
		shooterStage2On07.whenPressed(new ShooterStage2SetSpeed(0.7));
		SmartDashboard.putData("Shooter Stage 2 - 0.7", shooterStage2On07);

		Button shooterStage2On06 = new InternalButton();
		shooterStage2On06.whenPressed(new ShooterStage2SetSpeed(0.6));
		SmartDashboard.putData("Shooter Stage 2 - 0.6", shooterStage2On06);

		Button shooterStage2On05 = new InternalButton();
		shooterStage2On05.whenPressed(new ShooterStage2SetSpeed(0.5));
		SmartDashboard.putData("Shooter Stage 2 - 0.5", shooterStage2On05);

		Button shooterStage2Off = new InternalButton();
		shooterStage2Off.whenPressed(new ShooterStage2SetSpeed(0.0));
		SmartDashboard.putData("Shooter Stage 2 - Off", shooterStage2Off);
		
		Button shooterStage1RPMOnDash = new InternalButton();
		shooterStage1RPMOnDash.whenPressed(new ShooterStage1SetRpmDashboard());
		SmartDashboard.putData("Shooter Stage 1 Dash RPM", shooterStage1RPMOnDash);

		Button shooterStage1On05 = new InternalButton();
		shooterStage1On05.whenPressed(new ShooterStage1SetSpeed(0.5));
		SmartDashboard.putData("Shooter Stage 1 - 0.5", shooterStage1On05);
		
		Button shooterStage1Off = new InternalButton();
		shooterStage1Off.whenPressed(new ShooterStage1SetSpeed(0.0));
		SmartDashboard.putData("Shooter Stage 1 - Off", shooterStage1Off);
		
		Button shooterBothRPMOnDash = new InternalButton();
		shooterBothRPMOnDash.whenPressed(new ShooterSetRpmDashboard());
		SmartDashboard.putData("Shooter Both Dash RPM", shooterBothRPMOnDash);

		Button shooterBothVBusOnDash = new InternalButton();
		shooterBothVBusOnDash.whenPressed(new ShooterSetSpeedDashboard());
		SmartDashboard.putData("Shooter Both Dash VBus", shooterBothVBusOnDash);

		Button allButLiftOff = new InternalButton();
		allButLiftOff.whenPressed(new ShooterAllOff());
		SmartDashboard.putData("Shooter All Off", allButLiftOff);
		
		Button allButLiftOn = new InternalButton();
		allButLiftOn.whenPressed(new ShooterAllOn());
		SmartDashboard.putData("Shooter All On", allButLiftOn);

		Button intakeOn10 = new InternalButton();
		intakeOn10.whenPressed(new BallIntakeRollerSetSpeed(1.0));
		SmartDashboard.putData("Intake 1.0", intakeOn10);

		Button intakeOn08 = new InternalButton();
		intakeOn08.whenPressed(new BallIntakeRollerSetSpeed(0.8));
		SmartDashboard.putData("Intake 0.8", intakeOn08);

		Button intakeOn05 = new InternalButton();
		intakeOn05.whenPressed(new BallIntakeRollerSetSpeed(0.5));
		SmartDashboard.putData("Intake 0.5", intakeOn05);

		Button intakeOn04 = new InternalButton();
		intakeOn04.whenPressed(new BallIntakeRollerSetSpeed(0.4));
		SmartDashboard.putData("Intake 0.4", intakeOn04);

		Button intakeOff = new InternalButton();
		intakeOff.whenPressed(new BallIntakeRollerSetSpeed(0.0));
		SmartDashboard.putData("Intake Off", intakeOff);

		Button ballLiftPositive = new InternalButton();
		ballLiftPositive.whenPressed(new BallIntakeLiftSpeed(0.2));
		ballLiftPositive.whenReleased(new BallIntakeLiftSpeed(0.0));
		SmartDashboard.putData("Ball Lift Positive", ballLiftPositive);

		Button ballLiftNegative = new InternalButton();
		ballLiftNegative.whenPressed(new BallIntakeLiftSpeed(-0.2));
		ballLiftNegative.whenReleased(new BallIntakeLiftSpeed(0.0));
		SmartDashboard.putData("Ball Lift Negative", ballLiftNegative);

		Button ballLiftZero = new InternalButton();
		ballLiftZero.whenPressed(new BallIntakeLiftResetZero());
		SmartDashboard.putData("Ball Lift Reset Zero 1", ballLiftZero);

		Button gearLiftPositive = new InternalButton();
		gearLiftPositive.whenPressed(new GearIntakeLiftSpeed(0.2));
		gearLiftPositive.whenReleased(new GearIntakeLiftSpeed(0.0));
		SmartDashboard.putData("Gear Lift Positive", gearLiftPositive);

		Button gearLiftNegative = new InternalButton();
		gearLiftNegative.whenPressed(new GearIntakeLiftSpeed(-0.2));
		gearLiftNegative.whenReleased(new GearIntakeLiftSpeed(0.0));
		SmartDashboard.putData("Gear Lift Negative", gearLiftNegative);

		Button gearLiftZero = new InternalButton();
		gearLiftZero.whenPressed(new GearIntakeLiftResetZero());
		SmartDashboard.putData("Gear Lift Reset Zero 1", gearLiftZero);

		Button shooterShotPositionClose = new InternalButton();
		shooterShotPositionClose.whenPressed(new ShooterSetShotPosition(Shooter.ShotState.CLOSE));
		SmartDashboard.putData("Shooter Close Shot", shooterShotPositionClose);
		
		Button shooterShotPositionFar = new InternalButton();
		shooterShotPositionFar.whenPressed(new ShooterSetShotPosition(Shooter.ShotState.FAR));
		SmartDashboard.putData("Shooter Far Shot", shooterShotPositionFar);
		
		Button climberOn08 = new InternalButton();
		climberOn08.whenPressed(new ClimberSetSpeed(0.8));
		climberOn08.whenReleased(new ClimberSetSpeed(0.0));
		SmartDashboard.putData("Climber 0.8", climberOn08);

		Button climberSetMaxAmps = new InternalButton();
		climberSetMaxAmps.whenPressed(new ClimberSetMaxAmps(0.8, 20));
		SmartDashboard.putData("Climber Max Amps", climberSetMaxAmps);

		Button gyroReset = new InternalButton();
		gyroReset.whenPressed(new DriveGyroReset());
		SmartDashboard.putData("Gyro Reset", gyroReset);
		
		// Camera
		Button cameraUpdateDashboard = new InternalButton();
		cameraUpdateDashboard.whenPressed(new CameraUpdateDashboard());
		SmartDashboard.putData("Camera Update", cameraUpdateDashboard);
		
		Button cameraSaveImage = new InternalButton();
		cameraSaveImage.whenPressed(new CameraSaveImage());
		SmartDashboard.putData("Camera Save", cameraSaveImage);
		
		Button cameraReadImage = new InternalButton();
		cameraReadImage.whenPressed(new CameraReadAndProcessImage());
		SmartDashboard.putData("Camera Read", cameraReadImage);
		
		Button cameraReadImageTurnToBestTarget = new InternalButton();
		cameraReadImageTurnToBestTarget.whenPressed(new CameraReadImageTurnToBestTarget());
		SmartDashboard.putData("Camera Read Turn", cameraReadImageTurnToBestTarget);
		
		Button cameraTurnToBestTarget = new InternalButton();
		cameraTurnToBestTarget.whenPressed(new CameraTurnToBestTarget());
		SmartDashboard.putData("Camera Turn To Best", cameraTurnToBestTarget);
		
		Button cameraUpdateBestTarget = new InternalButton();
		cameraUpdateBestTarget.whenPressed(new CameraUpdateBestTarget());
		SmartDashboard.putData("Camera Update Best", cameraUpdateBestTarget);
		
		Button incrementCameraOffsetPos = new InternalButton();
		incrementCameraOffsetPos.whenPressed(new CameraOffset(0.5));
		SmartDashboard.putData("Camera Offset Pos", incrementCameraOffsetPos);
		
		Button incrementCameraOffsetNeg = new InternalButton();
		incrementCameraOffsetNeg.whenPressed(new CameraOffset(-0.5));
		SmartDashboard.putData("Camera Offset Neg", incrementCameraOffsetNeg);
	}
	
	public static OI getInstance() {
		if(instance == null) {
			instance = new OI();
		}
		return instance;
	}

	public IHandController getDriverController() {
		return m_controller;
	}

}

