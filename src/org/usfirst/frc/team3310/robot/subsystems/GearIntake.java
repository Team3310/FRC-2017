
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearIntake extends Subsystem {
    
	public static enum GearPositionState { UP, DOWN };
	public static final double GEAR_INTAKE_LOAD_SPEED = 0.8;//0.4;
	public static final double GEAR_INTAKE_EJECT_SPEED = -0.8;//-0.4;
	public static final double GEAR_INTAKE_DEPLOY_SPEED = -0.3;
		
	private IntakePosition position;
	private Solenoid gearInnerPosition;
	private Solenoid gearOuterPosition;
	private DigitalInput gearSensor;

	private CANTalon rollerMotor;

	public GearIntake() {
		try {
			rollerMotor = new CANTalon(RobotMap.GEAR_INTAKE_ROLLER_MOTOR_CAN_ID);
			gearInnerPosition = new Solenoid(RobotMap.GEAR_INNER_POSITION_PCM_ID);			
			gearOuterPosition = new Solenoid(RobotMap.GEAR_OUTER_POSITION_PCM_ID);			
			
			gearSensor = new DigitalInput(RobotMap.GEAR_SENSOR_DIO_ID);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the GearIntake constructor");
		}
	}
	
	public void setRollerSpeed(double speed) {
		rollerMotor.set(-speed);
		Robot.ledLights.setIntakeRollerOn(Math.abs(speed) > 0.01);
	}
	
	public boolean isGearPresent() {
		return gearSensor.get();
	}
	
	public void setLiftPosition(IntakePosition position) {
		GearPositionState gearInnerPosition = GearPositionState.UP;
		GearPositionState gearOuterPosition = GearPositionState.UP;
		this.position = position;
		if (position == IntakePosition.GEAR_INTAKE) {
			gearInnerPosition = GearPositionState.DOWN;
			gearOuterPosition = GearPositionState.DOWN;
		}
		else if (position == IntakePosition.GEAR_PRESENT) {
			gearInnerPosition = GearPositionState.UP;
			gearOuterPosition = GearPositionState.DOWN;
		}
		else if (position == IntakePosition.GEAR_DEPLOY) {
			gearInnerPosition = GearPositionState.DOWN;
			gearOuterPosition = GearPositionState.DOWN;
		}
		else if (position == IntakePosition.RETRACT) {
			gearInnerPosition = GearPositionState.UP;
			gearOuterPosition = GearPositionState.UP;
		}
		else if (position == IntakePosition.SHOOT) {
			gearInnerPosition = GearPositionState.UP;
			gearOuterPosition = GearPositionState.DOWN;
		}
		setGearInnerPosition(gearInnerPosition);
		setGearOuterPosition(gearOuterPosition);
	}
	
	public IntakePosition getIntakePosition() {
		return position;
	}
		
	public void setGearInnerPosition(GearPositionState state) {
		if(state == GearPositionState.UP) {
			gearInnerPosition.set(false);
		}
		else if(state == GearPositionState.DOWN) {
			gearInnerPosition.set(true);
		}
	}

	public void setGearOuterPosition(GearPositionState state) {
		if(state == GearPositionState.UP) {
			gearOuterPosition.set(false);
		}
		else if(state == GearPositionState.DOWN) {
			gearOuterPosition.set(true);
		}
	}

	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putBoolean("Gear Sensor", isGearPresent());
		if (isGearPresent()) {
			Robot.ledLights.setGearLoaded(true);
		}
		else {
			Robot.ledLights.setGearLoaded(false);
		}
	}

	public void initDefaultCommand() {
    }
}

