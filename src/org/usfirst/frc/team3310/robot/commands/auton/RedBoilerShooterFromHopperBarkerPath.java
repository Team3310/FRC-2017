package org.usfirst.frc.team3310.robot.commands.auton;

import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DrivePathMP;
import org.usfirst.frc.team3310.robot.commands.ShootOn;
import org.usfirst.frc.team3310.robot.commands.ShooterSetHopperPosition;
import org.usfirst.frc.team3310.robot.commands.ShooterSetHopperShake;
import org.usfirst.frc.team3310.robot.commands.ShooterSetRpm;
import org.usfirst.frc.team3310.robot.commands.ShooterSetShotPosition;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.HopperState;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.utility.PathGenerator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class RedBoilerShooterFromHopperBarkerPath extends CommandGroup {
    
    public RedBoilerShooterFromHopperBarkerPath(PathGenerator path) {
    	addSequential(new ShooterSetShotPosition(ShotState.FAR));
    	addSequential(new DriveGyroReset());
        addSequential(new DrivePathMP(path));
        addSequential(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR, Shooter.SHOOTER_STAGE2_RPM_FAR));
     	addSequential(new WaitCommand(0.2));
    	addSequential(new ShooterSetHopperPosition(HopperState.OPEN));
     	addSequential(new WaitCommand(0.2));
        addSequential(new ShootOn(ShotState.FAR, ShooterFeed.SHOOTER_FEED_SHOOT_FAR_SPEED, true));
     	addSequential(new WaitCommand(1.2));
        addSequential(new ShooterSetHopperShake(0, 10, 1));
    }
}
