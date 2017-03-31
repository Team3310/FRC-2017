package org.usfirst.frc.team3310.robot.commands.auton;

import org.usfirst.frc.team3310.robot.commands.ClimberSetSpeedTimer;
import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMPCached;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMPCached;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class BlueGearCenterAuton extends CommandGroup {
    
    public BlueGearCenterAuton() {
    	addSequential(new DriveGyroReset());
        addSequential(new DriveStraightMPCached(-70, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true,0));
        addSequential(new ClimberSetSpeedTimer(0.3, 0.7));
        addSequential(new DriveStraightMPCached(50, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new DriveAbsoluteTurnMPCached(0, 120, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));

    }
}
