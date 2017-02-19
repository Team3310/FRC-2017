package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.BallIntake;
import org.usfirst.frc.team3310.robot.subsystems.MagicCarpet;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeBallsOff extends CommandGroup {

    public IntakeBallsOff() {
    	addSequential(new BallIntakeRollerSetSpeed(BallIntake.BALL_INTAKE_OFF_SPEED));
    	addSequential(new MagicCarpetSetSpeed(MagicCarpet.MAGIC_CARPET_OFF_SPEED));
    	addSequential(new ShooterFeedSetSpeed(ShooterFeed.SHOOT_FEED_OFF_SPEED));
    }
}
