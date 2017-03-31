package org.usfirst.frc.team3310.robot.commands.auton;

import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.GearIntakeSetPositionNoRoller;
import org.usfirst.frc.team3310.robot.commands.ShootOn;
import org.usfirst.frc.team3310.robot.commands.ShooterSetHopperPosition;
import org.usfirst.frc.team3310.robot.commands.ShooterSetHopperShake;
import org.usfirst.frc.team3310.robot.commands.ShooterSetRpm;
import org.usfirst.frc.team3310.robot.commands.ShooterSetShotPosition;
import org.usfirst.frc.team3310.robot.commands.ShooterSetVoltageRampRate;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.HopperState;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class BlueBoilerShooterFromHopperBarker extends CommandGroup {
    
    public BlueBoilerShooterFromHopperBarker() {
    	addSequential(new ShooterSetVoltageRampRate(Shooter.AUTON_VOLTAGE_RAMP_RATE));
    	addSequential(new ShooterSetShotPosition(ShotState.FAR));
    	addSequential(new DriveGyroReset());
      	addSequential(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR, Shooter.SHOOTER_STAGE2_RPM_FAR));
    	addSequential(new DriveStraightMP(-63, Drive.MP_AUTON_MAX_BOILER_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0)); //greenville -61
        addSequential(new DriveAbsoluteTurnMP(27, Drive.MP_AUTON_MAX_BOILER_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.LEFT_SIDE_ONLY)); //greenville 30
        addSequential(new GearIntakeSetPositionNoRoller(IntakePosition.GEAR_PRESENT));
        addSequential(new DriveStraightMP(-32, Drive.MP_AUTON_MAX_BOILER_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 30)); //greenville -23
    	addSequential(new ShooterSetVoltageRampRate(Shooter.SHOOT_VOLTAGE_RAMP_RATE));
     	addSequential(new ShooterSetHopperPosition(HopperState.OPEN));
     	addSequential(new WaitCommand(0.2));
        addSequential(new ShootOn(ShotState.FAR, ShooterFeed.SHOOTER_FEED_SHOOT_FAR_SPEED, true));
     	addSequential(new WaitCommand(1.2));
        addSequential(new ShooterSetHopperShake(0, 10, 1));
    }
}
