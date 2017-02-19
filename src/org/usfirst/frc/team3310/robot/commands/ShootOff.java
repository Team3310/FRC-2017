package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.MagicCarpet;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootOff extends CommandGroup {

    public ShootOff() {
    	addSequential(new MagicCarpetSetSpeed(MagicCarpet.MAGIC_CARPET_OFF_SPEED));
        addSequential(new ShooterFeedSetSpeed(0.0));
   }
}
