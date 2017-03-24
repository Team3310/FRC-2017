package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class GearCenterAuton extends CommandGroup {
    
    public GearCenterAuton() {
    	addSequential(new DriveGyroReset());
        addSequential(new DriveStraightMPCached(-70, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true,0));
        addSequential(new ClimberSetSpeedTimer(0.3, 0.7));
        addSequential(new DriveStraightMPCached(50, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new DriveAbsoluteTurnMPCached(0, 120, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
    }
}
