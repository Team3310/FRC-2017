package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.BallIntake;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;
import org.usfirst.frc.team3310.robot.subsystems.MagicCarpet;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeBalls extends CommandGroup {

    public IntakeBalls() {
    	//addSequential(new GearIntakeLiftMoveMP(GearIntake.GEAR_INTAKE_POSITION_DEG));
    	//addSequential(new BallIntakeLiftMoveMP(BallIntake.BALL_INTAKE_POSITION_DEG));
    	addSequential(new BallIntakeRollerSetSpeed(BallIntake.BALL_INTAKE_LOAD_SPEED));
    	addSequential(new MagicCarpetSetSpeed(MagicCarpet.MAGIC_CARPET_BALL_MIX_SPEED));
    	addSequential(new ShooterFeedSetSpeed(ShooterFeed.SHOOTER_FEED_BALL_INTAKE_SPEED));
    }
}
