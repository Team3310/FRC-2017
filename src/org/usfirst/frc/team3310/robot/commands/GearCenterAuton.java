package org.usfirst.frc.team3310.robot.commands;

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
public class GearCenterAuton extends CommandGroup {
    
    public GearCenterAuton() {
    	addSequential(new DriveGyroReset());
//        addSequential(new DriveStraightMP(-90, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));
//    	addParallel(new IntakeSetPosition(IntakePosition.BALL_INTAKE));
//        addSequential(new DriveAbsoluteTurnMP(-60, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveStraightMP(-70, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true,0));
        addSequential(new ClimberSetSpeedTimer(0.3, 0.7));
       addSequential(new DriveStraightMP(50, Drive.MP_GEAR_DEPLOY_VELOCITY_INCHES_PER_SEC, true, true, 0));
//        addSequential(new WaitCommand(0.4));
 //       addSequential(new DriveStraightMP(18, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true,-60));
        addSequential(new DriveAbsoluteTurnMP(120, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
//        addParallel(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_CLOSE, Shooter.SHOOTER_STAGE2_RPM_CLOSE));
//        addSequential(new DriveStraightMP(107, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true,-45));
//        addSequential(new ShootOn(ShotState.CLOSE));

    }
}
