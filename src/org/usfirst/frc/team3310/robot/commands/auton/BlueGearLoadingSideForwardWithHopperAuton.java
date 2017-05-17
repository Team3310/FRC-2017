package org.usfirst.frc.team3310.robot.commands.auton;

import org.usfirst.frc.team3310.robot.commands.DriveAbsoluteTurnMP;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DriveSpeedShift;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.GearIntakeSetOuterPosition;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition;
import org.usfirst.frc.team3310.robot.commands.ShooterSetHopperPosition;
import org.usfirst.frc.team3310.robot.commands.IntakeSetPosition.IntakePosition;
import org.usfirst.frc.team3310.robot.commands.ShooterSetRpm;
import org.usfirst.frc.team3310.robot.commands.ShooterSetVoltageRampRate;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Drive.SpeedShiftState;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake.GearPositionState;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.HopperState;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class BlueGearLoadingSideForwardWithHopperAuton extends CommandGroup {
    
    public BlueGearLoadingSideForwardWithHopperAuton() {
    	addSequential(new DriveGyroReset());
    	addSequential(new IntakeSetPosition(IntakePosition.GEAR_PRESENT));

    	addSequential(new DriveStraightMP(93, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));  // 95 for 112" greenville
        addSequential(new DriveAbsoluteTurnMP(-60, Drive.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveStraightMP(22, Drive.MP_GEAR_DEPLOY_FASTER_VELOCITY_INCHES_PER_SEC, true, true, -60));
        addSequential(new IntakeSetPosition(IntakePosition.GEAR_DEPLOY));

        addSequential(new WaitCommand(0.3));
        addSequential(new DriveStraightMP(-20, Drive.MP_AUTON_MAX_BOILER_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, -60));
        addSequential(new DriveAbsoluteTurnMP(20, Drive.MP_AUTON_MAX_BOILER_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveStraightMP(135, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, -330));
        addSequential(new DriveAbsoluteTurnMP(85, Drive.MP_AUTON_MAX_BOILER_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
     	addSequential(new ShooterSetHopperPosition(HopperState.OPEN));
//    	addSequential(new IntakeSetPosition(IntakePosition.GEAR_PRESENT));
        addSequential(new DriveStraightMP(24, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 85));
        addSequential(new WaitCommand(3.0)); 
        addSequential(new DriveStraightMP(-80, Drive.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 85));        
//        addSequential(new DriveSpeedShift(SpeedShiftState.HI));
    }
}
