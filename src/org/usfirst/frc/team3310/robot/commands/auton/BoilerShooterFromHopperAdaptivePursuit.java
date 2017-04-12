package org.usfirst.frc.team3310.robot.commands.auton;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team3310.robot.commands.DrivePathAdaptivePursuit;
import org.usfirst.frc.team3310.robot.commands.DriveRelativeTurnPID;
import org.usfirst.frc.team3310.robot.commands.DriveStopOnHopperSensor;
import org.usfirst.frc.team3310.robot.commands.DriveStraightMP;
import org.usfirst.frc.team3310.robot.commands.RunAfterMarker;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import org.usfirst.frc.team3310.utility.Path;
import org.usfirst.frc.team3310.utility.Path.Waypoint;
import org.usfirst.frc.team3310.utility.Translation2d;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BoilerShooterFromHopperAdaptivePursuit extends CommandGroup {
    
    public BoilerShooterFromHopperAdaptivePursuit() {
//    	addSequential(new ShooterSetVoltageRampRate(Shooter.AUTON_VOLTAGE_RAMP_RATE));
//    	addSequential(new ShooterSetShotPosition(ShotState.FAR));
//   	addSequential(new DriveGyroReset());
//      addSequential(new ShooterSetRpm(Shooter.SHOOTER_STAGE1_RPM_FAR + 200, Shooter.SHOOTER_STAGE2_RPM_FAR + 200));

    	addParallel(new RunAfterMarker("hopperSensorOn", 2.0, new DriveStopOnHopperSensor()));
    	
        List<Waypoint> waypoints = new ArrayList<>();
        waypoints.add(new Waypoint(new Translation2d(0, 0), 35.0));
        waypoints.add(new Waypoint(new Translation2d(-29, 0), 40.0));
        Path.addCircleArc(waypoints, -30.0, 45.0, 10, "hopperSensorOn");
        waypoints.add(new Waypoint(new Translation2d(-65, -23), 40.0));
        waypoints.add(new Waypoint(new Translation2d(-75, -43), 40.0));

        addSequential(new DrivePathAdaptivePursuit(new Path(waypoints), true));
        addSequential(new DriveRelativeTurnPID(-6, MPSoftwareTurnType.LEFT_SIDE_ONLY));
    	addSequential(new DriveStraightMP(-3, Drive.MP_AUTON_MAX_BOILER_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, false, 0));//greenville -61
      	   	
/*      	addSequential(new GearIntakeSetOuterPosition(GearPositionState.DOWN));
   	    addSequential(new ShooterSetVoltageRampRate(Shooter.SHOOT_VOLTAGE_RAMP_RATE));
     	addSequential(new ShooterSetHopperPosition(HopperState.OPEN));
     	addSequential(new WaitCommand(0.2));
        addSequential(new DriveRelativeTurnPID(-7, MPSoftwareTurnType.TANK));
        addSequential(new ShootOn(ShotState.FAR, ShooterFeed.SHOOTER_FEED_SHOOT_FAR_SPEED, true));
     	addSequential(new WaitCommand(1.2));
        addSequential(new ShooterSetHopperShake(0, 11, 5));
        */
    }
}
