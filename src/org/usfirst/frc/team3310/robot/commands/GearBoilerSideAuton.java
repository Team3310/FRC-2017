package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearBoilerSideAuton extends CommandGroup {
    
    public GearBoilerSideAuton() {
    	addSequential(new DriveGyroReset());
        addSequential(new DriveStraightMP(-91, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));
    	addParallel(new IntakeSetPosition(IntakePosition.BALL_INTAKE));
        addSequential(new DriveAbsoluteTurnMP(-60, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveStraightMP(-30, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true,-60));
        addSequential(new ClimberSetSpeedTimer(0.2, 0.1));
        addSequential(new DriveStraightMP(10, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true,-60));
        

    }
}
