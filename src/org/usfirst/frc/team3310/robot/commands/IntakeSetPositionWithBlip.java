package org.usfirst.frc.team3310.robot.commands;

import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class IntakeSetPositionWithBlip extends CommandGroup {

    public IntakeSetPositionWithBlip(IntakePosition intakePosition) {
    	addParallel(new GearIntakeBlip());
    	addSequential(new WaitCommand(0.4));
    	addParallel(new GearIntakeRollerSetDeploy(0.5, 0.0, intakePosition));
       	addSequential(new GearIntakeSetPosition(intakePosition));
     }
}
