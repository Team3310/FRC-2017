package org.usfirst.frc.team3310.robot.commands.auton;

import org.usfirst.frc.team3310.robot.commands.CameraTurnToBestTarget;
import org.usfirst.frc.team3310.robot.commands.ClimberSetSpeedTimer;
import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.commands.ShootOn;
import org.usfirst.frc.team3310.robot.commands.ShooterSetRpm;
import org.usfirst.frc.team3310.robot.commands.ShooterSetVoltageRampRate;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class BlueGearCenterShootForwardAuton extends CommandGroup {
    
    public BlueGearCenterShootForwardAuton() {
    	addSequential(new ShooterSetVoltageRampRate(Shooter.AUTON_VOLTAGE_RAMP_RATE));
        addParallel(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_CLOSE, Shooter.SHOOTER_STAGE2_RPM_CLOSE));
    	addSequential(new DriveGyroReset());
    	addSequential(new IntakeSetPosition(IntakePosition.GEAR_PRESENT));
    	addSequential(new DriveStraightMP(60, Drive.MP_AUTON_MAX_BOILER_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0)); // 62" for 112" greenville
        addSequential(new DriveStraightMP(12, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true,0));   
        addSequential(new IntakeSetPosition(IntakePosition.GEAR_DEPLOY));
        addSequential(new WaitCommand(0.3));
        addSequential(new DriveStraightMP(-14, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new DriveAbsoluteTurnMP(-110, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
    	addSequential(new DriveStraightMP(86, Drive.MP_AUTON_MAX_BOILER_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 115)); 
        addSequential(new WaitCommand(0.3));
    	addSequential(new CameraTurnToBestTarget());
       	addSequential(new ShooterSetVoltageRampRate(Shooter.SHOOT_VOLTAGE_RAMP_RATE));
        addSequential(new ShootOn(ShotState.FAR, ShooterFeed.SHOOTER_FEED_SHOOT_FAR_SPEED, false));
    }
}
