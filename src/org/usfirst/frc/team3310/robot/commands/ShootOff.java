package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.robot.subsystems.ZarkerFeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootOff extends CommandGroup {

    public ShootOff() {
    	addSequential(new ZarkerFeedSetSpeed(ZarkerFeed.ZARKER_FEED_OFF_SPEED));
        addSequential(new ShooterFeedSetSpeed(ShooterFeed.SHOOT_FEED_OFF_SPEED));
    }
}
