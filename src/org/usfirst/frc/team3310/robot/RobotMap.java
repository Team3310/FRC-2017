package org.usfirst.frc.team3310.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// USB Port IDs
	public static final int DRIVER_JOYSTICK_1_USB_ID = 0;
	public static final int DRIVER_JOYSTICK_2_USB_ID = 1;

	// MOTORS 
	public static final int DRIVETRAIN_LEFT_MOTOR1_CAN_ID = 0;
	public static final int DRIVETRAIN_LEFT_MOTOR2_CAN_ID = 1;
	public static final int DRIVETRAIN_LEFT_MOTOR3_CAN_ID = 2;
	public static final int SHOOTER_FEED_LEFT_MOTOR_CAN_ID = 3;
	public static final int SHOOTER_MAIN_LEFT_MOTOR_CAN_ID = 4;
	public static final int BALL_INTAKE_LIFT_MOTOR_CAN_ID = 5;
	public static final int GEAR_INTAKE_LIFT_MOTOR_CAN_ID = 6;
	public static final int MAGIC_CARPET_ROLLER_MOTOR_CAN_ID = 7;

	public static final int DRIVETRAIN_RIGHT_MOTOR1_CAN_ID = 15;
	public static final int DRIVETRAIN_RIGHT_MOTOR2_CAN_ID = 14;
	public static final int DRIVETRAIN_RIGHT_MOTOR3_CAN_ID = 13;
	public static final int SHOOTER_FEED_RIGHT_MOTOR_CAN_ID = 12;	
	public static final int SHOOTER_MAIN_RIGHT_MOTOR_CAN_ID = 11;
	public static final int SHOOTER_LIFT_MOTOR_CAN_ID = 10;
	public static final int CLIMBER_MOTOR_CAN_ID = 9;
	public static final int BALL_INTAKE_ROLLER_MOTOR_CAN_ID = 8;
	
	// Pneumatics
	public static final int DRIVETRAIN_SPEEDSHIFT_PCM_ID = 0;
	public static final int SHOOTER_SHOT_POSITION_PCM_ID = 1;
	public static final int CLIMBER_DOOR_POSITION_1_PCM_ID = 5;
	public static final int CLIMBER_DOOR_POSITION_2_PCM_ID = 6;
}
