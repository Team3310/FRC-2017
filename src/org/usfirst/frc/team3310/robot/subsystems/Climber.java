
package org.usfirst.frc.team3310.robot.subsystems;

import org.usfirst.frc.team3310.robot.Robot;
import org.usfirst.frc.team3310.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climber extends Subsystem {
    
	public static final double CLIMB_SPEED = 0.8;
	public static enum DoorOpenState { UP, DOWN };

	
	private CANTalon roller;
	
	private DoubleSolenoid doorPosition;

	public Climber() {
		try {
			roller = new CANTalon(RobotMap.CLIMBER_MOTOR_CAN_ID);
			doorPosition = new DoubleSolenoid(RobotMap.CLIMBER_DOOR_POSITION_1_PCM_ID,RobotMap.CLIMBER_DOOR_POSITION_2_PCM_ID);
			
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Climber constructor");
		}
	}
	
	public void setSpeed(double speed) {
		roller.set(speed);
	}

	public double getAmps() {
		return roller.getOutputCurrent();
	}
	
    public void initDefaultCommand() {
    }
    
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Climber Amps", roller.getOutputCurrent());
    }
	public void setDoorPosition(DoorOpenState state) {
		if(state == DoorOpenState.UP) {
			doorPosition.set(Value.kReverse);
		}
		else if(state == DoorOpenState.DOWN) {
			doorPosition.set(Value.kForward);

}
	}
}

