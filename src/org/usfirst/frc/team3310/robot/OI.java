package org.usfirst.frc.team3310.robot;

import org.usfirst.frc.team3310.buttons.LogitechDPadTriggerButton;
import org.usfirst.frc.team3310.controller.LogitechController;
import org.usfirst.frc.team3310.robot.commands.BallIntakeLiftMoveMP;
import org.usfirst.frc.team3310.robot.commands.BallIntakeLiftResetZero;
import org.usfirst.frc.team3310.robot.commands.BallIntakeLiftSpeed;
import org.usfirst.frc.team3310.robot.commands.BallIntakeRollerSetSpeed;
import org.usfirst.frc.team3310.robot.commands.ClimberDoorPosition;
import org.usfirst.frc.team3310.robot.commands.ClimberSetMaxAmps;
import org.usfirst.frc.team3310.robot.commands.ClimberSetSpeed;
import org.usfirst.frc.team3310.robot.commands.DriveSpeedShift;
import org.usfirst.frc.team3310.robot.commands.GearIntakeLiftMoveMP;
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
import org.usfirst.frc.team3310.robot.subsystems.BallIntake;
import org.usfirst.frc.team3310.robot.subsystems.Climber;
import org.usfirst.frc.team3310.robot.subsystems.Climber.DoorOpenState;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;

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

	private LogitechController m_driverLogitech;
	
	private OI() {
		m_driverLogitech = new LogitechController(RobotMap.DRIVER_JOYSTICK_1_USB_ID);

		//Logitech Controller
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
        
        LogitechDPadTriggerButton climberDoorOpen = new LogitechDPadTriggerButton(m_driverLogitech, LogitechDPadTriggerButton.LEFT);
        climberDoorOpen.whenPressed(new ClimberDoorPosition(DoorOpenState.UP));
        
        LogitechDPadTriggerButton climberDoorClose = new LogitechDPadTriggerButton(m_driverLogitech, LogitechDPadTriggerButton.RIGHT);
        climberDoorClose.whenPressed(new ClimberDoorPosition(DoorOpenState.DOWN));

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

        
		Button magicCarpetOn05 = new InternalButton();
		magicCarpetOn05.whenPressed(new MagicCarpetSetSpeed(0.5));
		SmartDashboard.putData("Magic Carpet 0.5", magicCarpetOn05);

		Button magicCarpetOn02 = new InternalButton();
		magicCarpetOn02.whenPressed(new MagicCarpetSetSpeed(0.2));
		SmartDashboard.putData("Magic Carpet 0.2", magicCarpetOn02);

		Button magicCarpetOn01 = new InternalButton();
		magicCarpetOn01.whenPressed(new MagicCarpetSetSpeed(0.1));
		SmartDashboard.putData("Magic Carpet 0.1", magicCarpetOn01);

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

		Button ballLiftRetractedPosition = new InternalButton();
		ballLiftRetractedPosition.whenPressed(new BallIntakeLiftMoveMP(BallIntake.RETRACTED_POSITION_DEG));
		SmartDashboard.putData("Ball Lift Retracted Position", ballLiftRetractedPosition);

		Button ballLiftBallIntakePosition = new InternalButton();
		ballLiftBallIntakePosition.whenPressed(new BallIntakeLiftMoveMP(BallIntake.BALL_INTAKE_POSITION_DEG));
		SmartDashboard.putData("Ball Lift Ball Position", ballLiftBallIntakePosition);

		Button ballLiftGearPosition = new InternalButton();
		ballLiftGearPosition.whenPressed(new BallIntakeLiftMoveMP(BallIntake.GEAR_INTAKE_POSITION_DEG));
		SmartDashboard.putData("Ball Lift Gear Position", ballLiftGearPosition);

		Button ballPresent = new InternalButton();
		ballPresent.whenPressed(new BallIntakeLiftMoveMP(BallIntake.GEAR_PRESENT_POSITION_DEG));
		SmartDashboard.putData("Ball Present", ballPresent);

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

		Button gearLiftRetractedPosition = new InternalButton();
		gearLiftRetractedPosition.whenPressed(new GearIntakeLiftMoveMP(GearIntake.RETRACTED_POSITION_DEG));
		SmartDashboard.putData("Gear Lift Retracted Position", gearLiftRetractedPosition);

		Button gearLiftGearPosition = new InternalButton();
		gearLiftGearPosition.whenPressed(new GearIntakeLiftMoveMP(GearIntake.GEAR_INTAKE_POSITION_DEG));
		SmartDashboard.putData("Gear Lift Gear Position", gearLiftGearPosition);

		Button gearLiftZero = new InternalButton();
		gearLiftZero.whenPressed(new GearIntakeLiftResetZero());
		SmartDashboard.putData("Gear Lift Reset Zero 1", gearLiftZero);

		Button gearPresent = new InternalButton();
		gearPresent.whenPressed(new GearIntakeLiftMoveMP(GearIntake.GEAR_PRESENT_POSITION_DEG));
		SmartDashboard.putData("Gear Lift Present", gearPresent);

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

		Button climberDoorPositionUp = new InternalButton();
		climberDoorPositionUp.whenPressed(new ClimberDoorPosition (Climber.DoorOpenState.UP));
		SmartDashboard.putData("Climber Door Up", climberDoorPositionUp);
		
		Button climberDoorPositionDown = new InternalButton();
		climberDoorPositionDown.whenPressed(new ClimberDoorPosition (Climber.DoorOpenState.DOWN));
		SmartDashboard.putData("Climber Door Down", climberDoorPositionDown);
	}
	
	public static OI getInstance() {
		if(instance == null) {
			instance = new OI();
		}
		return instance;
	}

	public LogitechController getDriverLogitech() {
		return m_driverLogitech;
	}

}

