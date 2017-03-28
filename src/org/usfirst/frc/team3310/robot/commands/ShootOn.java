package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.robot.subsystems.ZarkerFeed;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class ShootOn extends CommandGroup {

    public ShootOn(ShotState shotState, double feedSpeed) {
    	if (shotState == ShotState.CLOSE) {
    		addSequential(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_CLOSE, Shooter.SHOOTER_STAGE2_RPM_CLOSE));
    	}
    	else {
    		addSequential(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR, Shooter.SHOOTER_STAGE2_RPM_FAR));
    	}
    	addSequential(new ShooterSetShotPosition(shotState));
        addSequential(new ShooterFeedSetSpeed(feedSpeed));
//        addSequential(new WaitCommand(0.5));
        addSequential(new ZarkerFeedSetSpeed(ZarkerFeed.ZARKER_FEED_SHOOT_SPEED));
    }
}
