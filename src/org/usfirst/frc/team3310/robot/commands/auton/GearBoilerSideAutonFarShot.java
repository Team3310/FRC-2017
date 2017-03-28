package org.usfirst.frc.team3310.robot.commands.auton;

import org.usfirst.frc.team3310.robot.commands.CameraTurnToBestTarget;
import org.usfirst.frc.team3310.robot.commands.ClimberSetSpeedTimer;
import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition;
import org.usfirst.frc.team3310.robot.commands.ShootOn;
import org.usfirst.frc.team3310.robot.commands.ShooterSetRpm;
import org.usfirst.frc.team3310.robot.commands.ShooterSetVoltageRampRate;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class GearBoilerSideAutonFarShot extends CommandGroup {
    
    public GearBoilerSideAutonFarShot() {
    	addSequential(new ShooterSetVoltageRampRate(Shooter.AUTON_VOLTAGE_RAMP_RATE));
        addParallel(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR, Shooter.SHOOTER_STAGE2_RPM_FAR));
    	addSequential(new DriveGyroReset());
        addSequential(new DriveStraightMP(-87, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0)); //-84
    	addParallel(new IntakeSetPosition(IntakePosition.BALL_INTAKE));
        addSequential(new DriveAbsoluteTurnMP(-60, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveStraightMP(-31, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true,-60));
        addSequential(new ClimberSetSpeedTimer(0.3, 0.7));
        addSequential(new DriveStraightMP(14, Drive.MP_GEAR_DEPLOY_FASTER_VELOCITY_INCHES_PER_SEC, true, true,-60));
//        addSequential(new WaitCommand(0.4));
 //       addSequential(new DriveStraightMP(18, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true,-60));
        addSequential(new DriveAbsoluteTurnMP(-44, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addParallel(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR, Shooter.SHOOTER_STAGE2_RPM_FAR));
    	addSequential(new ShooterSetVoltageRampRate(Shooter.SHOOT_VOLTAGE_RAMP_RATE));
        addSequential(new DriveStraightMP(53, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true,-44));
    	addSequential(new CameraTurnToBestTarget());
        addSequential(new ShootOn(ShotState.FAR, ShooterFeed.SHOOTER_FEED_SHOOT_FAR_SPEED));

    }
}
