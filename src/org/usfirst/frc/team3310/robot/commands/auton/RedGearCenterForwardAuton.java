package org.usfirst.frc.team3310.robot.commands.auton;

import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class RedGearCenterForwardAuton extends CommandGroup {
    
    public RedGearCenterForwardAuton() {
    	addSequential(new DriveGyroReset());
    	addSequential(new IntakeSetPosition(IntakePosition.GEAR_PRESENT));
//        addSequential(new DriveStraightMP(68, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true,0));   // This is for 112" wall to C-channel at Greenville
        addSequential(new DriveStraightMP(66, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true,0));   // This is for 110" wall to C-channel at Darwin
        addSequential(new IntakeSetPosition(IntakePosition.GEAR_DEPLOY));
        addSequential(new WaitCommand(0.3));
        addSequential(new DriveStraightMP(-50, Drive.MP_AUTON_MAX_BOILER_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new DriveAbsoluteTurnMP(-60, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
    }
}
