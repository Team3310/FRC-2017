package org.usfirst.frc.team3310.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterAllOn extends CommandGroup {

    public ShooterAllOn() {
        // Add Commands here:
        addSequential(new ShooterMainSetRPMDashboard());
        addSequential(new ShooterFeedSetRPMDashboard());
        addSequential(new ShooterLiftSetSpeed(1.0));
        addSequential(new MagicCarpetSetSpeed(1.0));

    }
}
