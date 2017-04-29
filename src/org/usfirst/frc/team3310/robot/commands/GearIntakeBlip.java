package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.subsystems.GearIntake.GearPositionState;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class GearIntakeBlip extends CommandGroup {

	public static enum IntakePosition { RETRACT, BALL_INTAKE, GEAR_INTAKE, GEAR_PRESENT, GEAR_DEPLOY, SHOOT };

    public GearIntakeBlip() {
       	addSequential(new GearIntakeSetOuterPosition(GearPositionState.UP));
       	addSequential(new WaitCommand(0.4));
       	addSequential(new GearIntakeSetOuterPosition(GearPositionState.DOWN));
     }
}
