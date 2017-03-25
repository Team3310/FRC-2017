package org.usfirst.frc.team3310.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterAllOn extends CommandGroup {

    public ShooterAllOn() {
        addSequential(new ShooterStage2SetRpmDashboard());
        addSequential(new ShooterStage1SetRpmDashboard());
        addSequential(new ShooterFeedSetSpeed(1.0));
        addSequential(new ZarkerFeedSetSpeed(1.0));

    }
}
