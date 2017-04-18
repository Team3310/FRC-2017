package org.usfirst.frc.team3310.robot.commands.auton;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team3310.robot.commands.CameraTurnToBestTarget;
import org.usfirst.frc.team3310.robot.commands.DriveGyroReset;
import org.usfirst.frc.team3310.robot.commands.DrivePathAdaptivePursuit;
import org.usfirst.frc.team3310.robot.commands.DriveRelativeTurnPID;
import org.usfirst.frc.team3310.robot.commands.DriveStopOnHopperSensor;
import org.usfirst.frc.team3310.robot.commands.GearIntakeSetOuterPosition;
import org.usfirst.frc.team3310.robot.commands.RunAfterMarker;
import org.usfirst.frc.team3310.robot.commands.ShootOn;
import org.usfirst.frc.team3310.robot.commands.ShooterSetHopperPosition;
import org.usfirst.frc.team3310.robot.commands.ShooterSetHopperShake;
import org.usfirst.frc.team3310.robot.commands.ShooterSetRpm;
import org.usfirst.frc.team3310.robot.commands.ShooterSetShotPosition;
import org.usfirst.frc.team3310.robot.commands.ShooterSetVoltageRampRate;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake.GearPositionState;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.HopperState;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.ShotState;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import org.usfirst.frc.team3310.utility.Path;
import org.usfirst.frc.team3310.utility.Path.Waypoint;
import org.usfirst.frc.team3310.utility.Translation2d;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class RedBoilerShooterFromHopperAdaptivePursuit extends CommandGroup {
    
    public RedBoilerShooterFromHopperAdaptivePursuit() {
    	addSequential(new ShooterSetVoltageRampRate(Shooter.AUTON_VOLTAGE_RAMP_RATE));
    	addSequential(new ShooterSetShotPosition(ShotState.FAR));
    	addSequential(new DriveGyroReset());
        addSequential(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR + 200, Shooter.SHOOTER_STAGE2_RPM_FAR + 200));

    	addParallel(new RunAfterMarker("hopperSensorOn", 2.0, new DriveStopOnHopperSensor()));
    	
        List<Waypoint> waypoints = new ArrayList<>();
        waypoints.add(new Waypoint(new Translation2d(0, 0), 40.0));
        waypoints.add(new Waypoint(new Translation2d(-35, 0), 40.0));
        Path.addCircleArc(waypoints, -30.0, 45.0, 10, "hopperSensorOn");
        waypoints.add(new Waypoint(new Translation2d(-85, -30), 40.0));
        addSequential(new DrivePathAdaptivePursuit(new Path(waypoints), true));
      	   	
      	addSequential(new GearIntakeSetOuterPosition(GearPositionState.DOWN));
   	    addSequential(new ShooterSetVoltageRampRate(Shooter.SHOOT_VOLTAGE_RAMP_RATE));
     	addSequential(new ShooterSetHopperPosition(HopperState.OPEN));
        addSequential(new DriveRelativeTurnPID(-9, MPSoftwareTurnType.RIGHT_SIDE_ONLY));
        addSequential(new ShootOn(ShotState.FAR, ShooterFeed.SHOOTER_FEED_SHOOT_FAR_SPEED, false));
        addSequential(new CameraTurnToBestTarget());
        addSequential(new ShooterSetHopperShake(0, 11, 3));
    }
}
