package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class BallIntakeManualRelease extends CommandGroup {
    
    public BallIntakeManualRelease() {
        addSequential(new BallIntakeLiftResetZero(4.3));
        addSequential(new IntakeSetPosition(IntakePosition.RETRACT));
    }
}
