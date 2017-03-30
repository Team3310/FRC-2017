package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class ShootOnDelay extends CommandGroup {
    
    public ShootOnDelay(ShotState shotState, double feedSpeed, boolean cameraAlign) {
        addSequential(new WaitCommand(0.6));
        addSequential(new ShootOn(shotState, feedSpeed, cameraAlign));
    }
}
