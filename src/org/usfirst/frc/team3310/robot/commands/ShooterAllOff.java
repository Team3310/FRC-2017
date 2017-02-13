package org.usfirst.frc.team3310.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterAllOff extends CommandGroup {

    public ShooterAllOff() {
        addSequential(new ShooterStage2SetSpeed(0.0));
        addSequential(new ShooterStage1SetSpeed(0.0));
        addSequential(new ShooterFeedSetSpeed(0.0));
        addSequential(new MagicCarpetSetSpeed(0.0));

    }
}
