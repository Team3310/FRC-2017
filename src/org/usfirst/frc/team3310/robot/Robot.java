
package org.usfirst.frc.team3310.robot;

import org.usfirst.frc.team3310.robot.commands.auton.BlueBoilerShooterFromHopper;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearBoilerSideAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearCenterAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearLoadingSideAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BoilerShooterFromHopper;
import org.usfirst.frc.team3310.robot.commands.auton.BoilerShooterFromHopperBarker;
import org.usfirst.frc.team3310.robot.commands.auton.BoilerShooterFromHopperBarkerPath;
import org.usfirst.frc.team3310.robot.commands.auton.GearBoilerSideAuton;
import org.usfirst.frc.team3310.robot.commands.auton.GearBoilerSideAutonFarShot;
import org.usfirst.frc.team3310.robot.commands.auton.GearCenterAuton;
import org.usfirst.frc.team3310.robot.commands.auton.GearLoadingSideAuton;
import org.usfirst.frc.team3310.robot.subsystems.Camera;
import org.usfirst.frc.team3310.robot.subsystems.Climber;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;
import org.usfirst.frc.team3310.robot.subsystems.LedLights;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.robot.subsystems.ZarkerFeed;
import org.usfirst.frc.team3310.utility.ControlLooper;
import org.usfirst.frc.team3310.utility.MotionProfileCache;
import org.usfirst.frc.team3310.utility.PathGenerator;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static OI oi;
	
	public static final Drive drive = new Drive();
	public static final Shooter shooter = new Shooter();
	public static final ShooterFeed shooterFeed = new ShooterFeed();
	public static final GearIntake gearIntake = new GearIntake();
	public static final ZarkerFeed zarkerFeed = new ZarkerFeed();
	public static final Climber climber = new Climber();
	public static final LedLights ledLights = new LedLights();
	public static final Camera camera = new Camera();
	
	public static final long periodMS = 10;
	public static final ControlLooper controlLoop = new ControlLooper("Main control loop", periodMS);
	public static final PowerDistributionPanel pdp = new PowerDistributionPanel();

	public static double shooterStage2RpmDashboard = 2950;
	public static double shooterStage1RpmDashboard = 2950;
	public static double shooterBothRpmDashboard = 2700;
	public static double shooterBothVBusDashboard = 0.7;

	public static enum OperationMode { TEST, COMPETITION };
	public static OperationMode operationMode = OperationMode.TEST;

	private SendableChooser<OperationMode> operationModeChooser;
	private SendableChooser<Command> autonTaskChooser;

    private Command autonomousCommand;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = OI.getInstance();
		camera.initialize();
		
    	controlLoop.addLoopable(drive);
  
	    operationModeChooser = new SendableChooser<OperationMode>();
	    operationModeChooser.addDefault("Test", OperationMode.TEST);
	    operationModeChooser.addObject("Competition", OperationMode.COMPETITION);
		SmartDashboard.putData("Operation Mode", operationModeChooser);
		
        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(-95, -9, Pathfinder.d2r(-27))
        };

        PathGenerator boilerPath = new PathGenerator(points, 0.01, 120, 200.0, 700.0);		
		
		autonTaskChooser = new SendableChooser<Command>();
		autonTaskChooser.addObject("Red Gear Loading Side", new GearLoadingSideAuton());
		autonTaskChooser.addObject("Blue Gear Loading Side", new BlueGearLoadingSideAuton());
		autonTaskChooser.addObject("Red Gear Center", new GearCenterAuton());
		autonTaskChooser.addObject("Blue Gear Center", new BlueGearCenterAuton());
		autonTaskChooser.addObject("Red Boiler Shooter From Hopper", new BoilerShooterFromHopper());
		autonTaskChooser.addObject("Blue Boiler Shooter From Hopper", new BlueBoilerShooterFromHopper());
		autonTaskChooser.addObject("Red Boiler Shooter From Hopper Barker", new BoilerShooterFromHopperBarker());
		autonTaskChooser.addObject("Red Boiler Shooter From Hopper Barker Path", new BoilerShooterFromHopperBarkerPath(boilerPath));
		autonTaskChooser.addDefault("Red Gear Boiler Side", new GearBoilerSideAuton());
		autonTaskChooser.addDefault("Red Gear Boiler Side Far Shot", new GearBoilerSideAutonFarShot());
		autonTaskChooser.addObject("Blue Gear Boiler Side", new BlueGearBoilerSideAuton());
		SmartDashboard.putData("Auton Task", autonTaskChooser);

		SmartDashboard.putNumber("Shooter Stage 2 Target RPM", shooterStage2RpmDashboard);
		SmartDashboard.putNumber("Shooter Stage 1 Target RPM", shooterStage1RpmDashboard);
		SmartDashboard.putNumber("Shooter Target RPM", shooterBothRpmDashboard);
		SmartDashboard.putNumber("Shooter Target VBus", shooterBothVBusDashboard);
		
		ledLights.setAllLightsOn(false);
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		updateStatus();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {    	
    	updateChoosers();
    	
        // Schedule the autonomous command (example)
    	controlLoop.start();
    	drive.endGyroCalibration();
    	drive.resetEncoders();
    	drive.resetGyro();
    	drive.setIsRed(getAlliance().equals(Alliance.Red));
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
		updateStatus();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        MotionProfileCache.getInstance().release();
    	updateChoosers();
        controlLoop.start();
    	drive.resetEncoders();
    	drive.endGyroCalibration();
        updateStatus();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
		updateStatus();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
		updateStatus();
   }
    
    private void updateChoosers() {
		operationMode = (OperationMode)operationModeChooser.getSelected();
		autonomousCommand = (Command)autonTaskChooser.getSelected();
    }
    
    public Alliance getAlliance() {
    	return m_ds.getAlliance();
    }
    
    public void updateStatus() {
    	drive.updateStatus(operationMode);
    	gearIntake.updateStatus(operationMode);
    	shooter.updateStatus(operationMode);
    	shooterFeed.updateStatus(operationMode);
    	zarkerFeed.updateStatus(operationMode);
    	climber.updateStatus(operationMode);
    	camera.updateStatus(operationMode);
   }

}
