package org.usfirst.frc.team3310.robot.commands.auton;

import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DriveSpeed;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.commands.ShootOff;
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
public class RedShootFirstGearBoilerSideForwardAuton extends CommandGroup {
    
    public RedShootFirstGearBoilerSideForwardAuton() {
        addSequential(new DriveGyroReset());
    	addSequential(new ShooterSetVoltageRampRate(Shooter.AUTON_VOLTAGE_RAMP_RATE));
        addParallel(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR, Shooter.SHOOTER_STAGE2_RPM_FAR));
        addSequential(new DriveStraightMP(-12, Drive.MP_GEAR_DEPLOY_FASTER_VELOCITY_INCHES_PER_SEC, true, true, 0));
    	addSequential(new IntakeSetPosition(IntakePosition.GEAR_PRESENT));
        addSequential(new WaitCommand(0.5));
       	addSequential(new ShooterSetVoltageRampRate(Shooter.SHOOT_VOLTAGE_RAMP_RATE));
        addSequential(new ShootOn(ShotState.FAR, ShooterFeed.SHOOTER_FEED_SHOOT_FAR_SPEED, false));      
        addSequential(new WaitCommand(2.0));
        addSequential(new ShootOff());              
        
        addSequential(new DriveAbsoluteTurnMP(-99, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveSpeed(-0.4, 0.5));
        addSequential(new WaitCommand(0.3));        
        addSequential(new DriveGyroReset());
        
        addSequential(new DriveStraightMP(89, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));  // 91" for 112" greenville
        addSequential(new DriveAbsoluteTurnMP(-60, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveStraightMP(21, Drive.MP_GEAR_DEPLOY_FASTER_VELOCITY_INCHES_PER_SEC, true, true, -60));
        addSequential(new IntakeSetPosition(IntakePosition.GEAR_DEPLOY));
        
        addSequential(new WaitCommand(0.3));
        addSequential(new DriveStraightMP(-21, Drive.MP_GEAR_DEPLOY_FASTER_VELOCITY_INCHES_PER_SEC, true, true, -60));
        addSequential(new DriveAbsoluteTurnMP(30, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveStraightMP(117, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 30)); 
        addSequential(new DriveAbsoluteTurnMP(0, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveStraightMP(214, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0)); 
    }
}
