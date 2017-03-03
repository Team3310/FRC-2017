package org.usfirst.frc.team3310.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeSetPosition extends CommandGroup {

	public static enum IntakePosition { RETRACT, BALL_INTAKE, GEAR_INTAKE, GEAR_PRESENT, GEAR_DEPLOY, SHOOT };

    public IntakeSetPosition(IntakePosition intakePosition) {
    	addSequential(new BallIntakeLiftMoveMP(intakePosition));
     	addSequential(new GearIntakeLiftMoveMP(intakePosition));
    }
}
