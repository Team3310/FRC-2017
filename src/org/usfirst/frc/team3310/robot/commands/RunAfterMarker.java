package org.usfirst.frc.team3310.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RunAfterMarker extends CommandGroup {

    public RunAfterMarker(String targetMarker, double timeout, Command command) {
    	addSequential(new DriveWaitForMarker(targetMarker, timeout));
        addSequential(command);
    }
}
