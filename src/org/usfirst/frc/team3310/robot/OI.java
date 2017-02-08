package org.usfirst.frc.team3310.robot;

import org.usfirst.frc.team3310.robot.commands.BallIntakeLiftMoveMP;
import org.usfirst.frc.team3310.robot.commands.BallIntakeLiftResetZero;
import org.usfirst.frc.team3310.robot.commands.BallIntakeLiftSpeed;
import org.usfirst.frc.team3310.robot.commands.BallIntakeRollerSetSpeed;
import org.usfirst.frc.team3310.robot.commands.DriveSpeedShift;
import org.usfirst.frc.team3310.robot.commands.GearIntakeLiftMoveMP;
import org.usfirst.frc.team3310.robot.commands.GearIntakeLiftResetZero;
import org.usfirst.frc.team3310.robot.commands.GearIntakeLiftSpeed;
import org.usfirst.frc.team3310.robot.commands.MagicCarpetSetSpeed;
import org.usfirst.frc.team3310.robot.commands.ShooterFeedSetRPMDashboard;
import org.usfirst.frc.team3310.robot.commands.ShooterFeedSetSpeed;
import org.usfirst.frc.team3310.robot.commands.ShooterLiftSetSpeed;
import org.usfirst.frc.team3310.robot.commands.ShooterMainSetRPMDashboard;
import org.usfirst.frc.team3310.robot.commands.ShooterMainSetSpeed;
import org.usfirst.frc.team3310.robot.subsystems.BallIntake;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;

import edu.wpi.first.wpilibj.Joystick;
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

	private Joystick m_driverJoystickPower;
	private Joystick m_driverJoystickTurn;

	private OI() {
		m_driverJoystickPower = new Joystick(RobotMap.DRIVER_JOYSTICK_1_USB_ID);
		m_driverJoystickTurn = new Joystick(RobotMap.DRIVER_JOYSTICK_2_USB_ID);
		
        JoystickButton shiftDrivetrain = new JoystickButton(m_driverJoystickPower, 1);
        shiftDrivetrain.whenPressed(new DriveSpeedShift(Drive.SpeedShiftState.HI));
        shiftDrivetrain.whenReleased(new DriveSpeedShift(Drive.SpeedShiftState.LO));

        Button magicCarpetOn10 = new InternalButton();
		magicCarpetOn10.whenPressed(new MagicCarpetSetSpeed(1.0));
		SmartDashboard.putData("Magic Carpet 1.0", magicCarpetOn10);

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

		Button shooterLiftOn10 = new InternalButton();
		shooterLiftOn10.whenPressed(new ShooterLiftSetSpeed(1.0));
		SmartDashboard.putData("Shooter Lift 1.0", shooterLiftOn10);

		Button shooterLiftOn05 = new InternalButton();
		shooterLiftOn05.whenPressed(new ShooterLiftSetSpeed(0.5));
		SmartDashboard.putData("Shooter Lift 0.5", shooterLiftOn05);

		Button shooterLiftOff = new InternalButton();
		shooterLiftOff.whenPressed(new ShooterLiftSetSpeed(0.0));
		SmartDashboard.putData("Shooter Lift Off", shooterLiftOff);

		Button shooterRPMOnDash = new InternalButton();
		shooterRPMOnDash.whenPressed(new ShooterMainSetRPMDashboard());
		SmartDashboard.putData("Shooter Dash RPM", shooterRPMOnDash);

		Button shooter2RPMOnDash = new InternalButton();
		shooter2RPMOnDash.whenPressed(new ShooterFeedSetRPMDashboard());
		SmartDashboard.putData("Shooter 2 Dash RPM", shooter2RPMOnDash);

		Button shooterOn10 = new InternalButton();
		shooterOn10.whenPressed(new ShooterMainSetSpeed(1.0));
		SmartDashboard.putData("Shooter 1.0", shooterOn10);

		Button shooterOn09 = new InternalButton();
		shooterOn09.whenPressed(new ShooterMainSetSpeed(0.9));
		SmartDashboard.putData("Shooter 0.9", shooterOn09);

		Button shooterOn08 = new InternalButton();
		shooterOn08.whenPressed(new ShooterMainSetSpeed(0.8));
		SmartDashboard.putData("Shooter 0.8", shooterOn08);

		Button shooterOn07 = new InternalButton();
		shooterOn07.whenPressed(new ShooterMainSetSpeed(0.7));
		SmartDashboard.putData("Shooter 0.7", shooterOn07);

		Button shooterOn06 = new InternalButton();
		shooterOn06.whenPressed(new ShooterMainSetSpeed(0.6));
		SmartDashboard.putData("Shooter 0.6", shooterOn06);

		Button shooterOn05 = new InternalButton();
		shooterOn05.whenPressed(new ShooterMainSetSpeed(0.5));
		SmartDashboard.putData("Shooter 0.5", shooterOn05);

		Button shooterOff = new InternalButton();
		shooterOff.whenPressed(new ShooterMainSetSpeed(0.0));
		SmartDashboard.putData("Shooter Off", shooterOff);
		
		Button shooter2Off = new InternalButton();
		shooter2Off.whenPressed(new ShooterFeedSetSpeed(0.0));
		SmartDashboard.putData("Shooter 2 Off", shooter2Off);

		Button intakeOn10 = new InternalButton();
		intakeOn10.whenPressed(new BallIntakeRollerSetSpeed(1.0));
		SmartDashboard.putData("Intake 1.0", intakeOn10);

		Button intakeOn05 = new InternalButton();
		intakeOn05.whenPressed(new BallIntakeRollerSetSpeed(0.5));
		SmartDashboard.putData("Intake 0.5", intakeOn05);

		Button intakeOn08 = new InternalButton();
		intakeOn08.whenPressed(new BallIntakeRollerSetSpeed(0.8));
		SmartDashboard.putData("Intake 0.8", intakeOn08);

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

		Button ballLiftZero = new InternalButton();
		ballLiftZero.whenPressed(new BallIntakeLiftResetZero());
		SmartDashboard.putData("Ball Lift Reset Zero", ballLiftZero);

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
		SmartDashboard.putData("Gear Lift Reset Zero", gearLiftZero);

	}
	
	public Joystick getDriverJoystickPower() {
		return m_driverJoystickPower;
	}
	
	public Joystick getDriverJoystickTurn() {
		return m_driverJoystickTurn;
	}

	public static OI getInstance() {
		if(instance == null) {
			instance = new OI();
		}
		return instance;
	}

}

