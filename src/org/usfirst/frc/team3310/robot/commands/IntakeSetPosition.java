package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.BallIntake;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class IntakeSetPosition extends CommandGroup {

	public static enum IntakePosition { RETRACT, BALL_INTAKE, GEAR_INTAKE, GEAR_PRESENT };

    public IntakeSetPosition(IntakePosition intakePosition) {
    	if (intakePosition == IntakePosition.RETRACT) {
    		addSequential(new BallIntakeLiftMoveMP(BallIntake.RETRACTED_POSITION_DEG));
     		addSequential(new GearIntakeLiftMoveMP(GearIntake.RETRACTED_POSITION_DEG));
    	}
    	else if (intakePosition == IntakePosition.BALL_INTAKE) {
    		addSequential(new GearIntakeLiftMoveMP(GearIntake.BALL_INTAKE_POSITION_DEG));
    		addSequential(new BallIntakeLiftMoveMP(BallIntake.BALL_INTAKE_POSITION_DEG));
    	}
    	else if (intakePosition == IntakePosition.GEAR_INTAKE) {
    		addSequential(new GearIntakeLiftMoveMP(GearIntake.GEAR_INTAKE_POSITION_DEG));
    		addSequential(new BallIntakeLiftMoveMP(BallIntake.GEAR_INTAKE_POSITION_DEG));
    	}
    	else if (intakePosition == IntakePosition.GEAR_PRESENT) {
    		addSequential(new BallIntakeLiftMoveMP(BallIntake.GEAR_PRESENT_POSITION_DEG));
    		addSequential(new GearIntakeLiftMoveMP(GearIntake.GEAR_PRESENT_POSITION_DEG));
    	}
   }
}
