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
	public static final int OPERATOR_JOYSTICK_1_USB_ID = 1;

	// MOTORS 
	public static final int DRIVETRAIN_LEFT_MOTOR1_CAN_ID = 0;
	public static final int DRIVETRAIN_LEFT_MOTOR2_CAN_ID = 1;
	public static final int DRIVETRAIN_LEFT_MOTOR3_CAN_ID = 2;
	public static final int SHOOTER_STAGE_1_LEFT_MOTOR_CAN_ID = 3;
	public static final int SHOOTER_STAGE_2_LEFT_MOTOR_CAN_ID = 4;
	public static final int ZARKER_FEED_ROLLER_RIGHT_MOTOR_CAN_ID = 5;
	public static final int CLIMBER_MOTOR_RIGHT_CAN_ID = 6;
	public static final int ZARKER_FEED_ROLLER_LEFT_MOTOR_CAN_ID = 7;

	public static final int DRIVETRAIN_RIGHT_MOTOR1_CAN_ID = 15;
	public static final int DRIVETRAIN_RIGHT_MOTOR2_CAN_ID = 14;
	public static final int DRIVETRAIN_RIGHT_MOTOR3_CAN_ID = 13;
	public static final int SHOOTER_STAGE_1_RIGHT_MOTOR_CAN_ID = 12;	
	public static final int SHOOTER_STAGE_2_RIGHT_MOTOR_CAN_ID = 11;
	public static final int SHOOTER_FEED_MOTOR_CAN_ID = 10;
	public static final int CLIMBER_MOTOR_lEFT_CAN_ID = 9;
	public static final int GEAR_INTAKE_ROLLER_MOTOR_CAN_ID = 8;
		
	// Pneumatics
	public static final int DRIVETRAIN_SPEEDSHIFT_PCM_ID = 2;
	public static final int SHOOTER_SHOT_POSITION_PCM_ID = 3;
	public static final int GEAR_INNER_POSITION_PCM_ID = 1;
	public static final int GEAR_OUTER_POSITION_PCM_ID = 4;
	public static final int HOPPER_POSITION_PCM_ID = 5;		
	
	// DIO
	public static final int HOPPER_SENSOR_RED_DIO_ID = 4;
	public static final int HOPPER_SENSOR_BLUE_DIO_ID = 5;

	// Analog
	public static final int GEAR_SENSOR_ANALOG_ID = 0;
}
