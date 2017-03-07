package org.usfirst.frc.team3310.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class IntakeSetPosition extends CommandGroup {

	public static enum IntakePosition { RETRACT, BALL_INTAKE, GEAR_INTAKE, GEAR_PRESENT, GEAR_DEPLOY, SHOOT };

    public IntakeSetPosition(IntakePosition intakePosition) {
       	addParallel(new BallIntakeRollerSetDeploy(1.0, intakePosition));
       	addSequential(new WaitCommand(0.3));
       	addSequential(new GearIntakeSetPosition(intakePosition));
    	addSequential(new BallIntakeLiftMoveMP(intakePosition));
     }
}
