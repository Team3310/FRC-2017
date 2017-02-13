package org.usfirst.frc.team3310.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterAllOff extends CommandGroup {

    public ShooterAllOff() {
        // Add Commands here:
        addSequential(new ShooterMainSetSpeed(0.0));
        addSequential(new ShooterFeedSetSpeed(0.0));
        addSequential(new ShooterLiftSetSpeed(0.0));
        addSequential(new MagicCarpetSetSpeed(0.0));

    }
}
