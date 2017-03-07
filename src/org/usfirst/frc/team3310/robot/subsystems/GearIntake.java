
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GearIntake extends Subsystem {
    
	public static enum GearPositionState { UP, DOWN };
		
	private IntakePosition position;
	private DoubleSolenoid gearPosition;
	private DigitalInput gearSensor;

	public GearIntake() {
		try {
			gearPosition = new DoubleSolenoid(RobotMap.GEAR_POSITION_1_PCM_ID,RobotMap.GEAR_POSITION_2_PCM_ID);			
			
			gearSensor = new DigitalInput(RobotMap.GEAR_SENSOR_DIO_ID);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the GearIntake constructor");
		}
	}
	
	public boolean isGearPresent() {
		return gearSensor.get();
	}
	
	public void setLiftPosition(IntakePosition position) {
		GearPositionState gearPosition = GearPositionState.UP;
		this.position = position;
		if (position == IntakePosition.BALL_INTAKE) {
			gearPosition = GearPositionState.DOWN;
		}
		else if (position == IntakePosition.GEAR_INTAKE) {
			gearPosition = GearPositionState.DOWN;
		}
		else if (position == IntakePosition.GEAR_PRESENT) {
			gearPosition = GearPositionState.UP;
		}
		else if (position == IntakePosition.GEAR_DEPLOY) {
			gearPosition = GearPositionState.DOWN;
		}
		else if (position == IntakePosition.RETRACT) {
			gearPosition = GearPositionState.UP;
		}
		else if (position == IntakePosition.SHOOT) {
			gearPosition = GearPositionState.UP;
		}
		setGearPosition(gearPosition);
	}
	
	public IntakePosition getIntakePosition() {
		return position;
	}
		
	public void setGearPosition(GearPositionState state) {
		if(state == GearPositionState.UP) {
			gearPosition.set(Value.kReverse);
		}
		else if(state == GearPositionState.DOWN) {
			gearPosition.set(Value.kForward);
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

