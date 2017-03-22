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
public class BoilerShooterFromHopperBarker extends CommandGroup {
    
    public BoilerShooterFromHopperBarker() {
    	addSequential(new DriveGyroReset());
    	addParallel(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR, Shooter.SHOOTER_STAGE2_RPM_FAR));
        addSequential(new DriveStraightMP(-75, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new DriveAbsoluteTurnMP(-15, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.LEFT_SIDE_ONLY));
        addSequential(new DriveStraightMP(-7, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, -15));
    	//addParallel(new IntakeSetPosition(IntakePosition.SHOOT));
      //  addSequential(new DriveAbsoluteTurnMP(10, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveAbsoluteTurnMP(-27, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.RIGHT_SIDE_ONLY));
 //       addSequential(new DriveStraightMP(46, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true,-90));
   //     addSequential(new WaitCommand(0.2));
     //   addParallel(new DriveStraightMP(-6, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, -90));
    	//addParallel(new IntakeSetPosition(IntakePosition.BALL_INTAKE));
      
        //addSequential(new WaitCommand(1.9));
        //addSequential(new DriveStraightMP(-10, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, -90));
        //addParallel(new DriveAbsoluteTurnMP(-18, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new ShootOn(ShotState.FAR));

    }
}
