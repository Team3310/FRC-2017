
package org.usfirst.frc.team3310.robot;

import org.usfirst.frc.team3310.robot.commands.auton.BlueBoilerShooterFromHopperAdaptivePursuit;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearBoilerSideForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearCenterForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearCenterForwardFastAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearCenterShootForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearLoadingSideForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BlueGearLoadingSideForwardWithHopperAuton;
import org.usfirst.frc.team3310.robot.commands.auton.BlueShootFirstGearBoilerSideForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.RedBoilerShooterFromHopperAdaptivePursuit;
import org.usfirst.frc.team3310.robot.commands.auton.RedGearBoilerSideForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.RedGearCenterForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.RedGearCenterForwardFastAuton;
import org.usfirst.frc.team3310.robot.commands.auton.RedGearCenterShootForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.RedGearLoadingSideForwardAuton;
import org.usfirst.frc.team3310.robot.commands.auton.RedGearLoadingSideForwardWithHopperAuton;
import org.usfirst.frc.team3310.robot.commands.auton.RedShootFirstGearBoilerSideForwardAuton;
import org.usfirst.frc.team3310.robot.subsystems.Camera;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;
import org.usfirst.frc.team3310.robot.subsystems.LedLights;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.Shooter.HopperState;
import org.usfirst.frc.team3310.robot.subsystems.ShooterFeed;
import org.usfirst.frc.team3310.robot.subsystems.ZarkerFeed;
import org.usfirst.frc.team3310.utility.ControlLooper;
import org.usfirst.frc.team3310.utility.MotionProfileCache;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


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
  
//		Waypoint[] points = new Waypoint[] {
//                new Waypoint(0, 0, 0),
//                new Waypoint(-95, -9, Pathfinder.d2r(-27))
//        };
//
//        PathGenerator boilerPath = new PathGenerator(points, 0.01, 120, 200.0, 700.0);			

        operationModeChooser = new SendableChooser<OperationMode>();
	    operationModeChooser.addDefault("Test", OperationMode.TEST);
	    operationModeChooser.addObject("Competition", OperationMode.COMPETITION);
		SmartDashboard.putData("Operation Mode", operationModeChooser);
		
		autonTaskChooser = new SendableChooser<Command>();

		autonTaskChooser.addObject("Red Gear Loading Side Plus Travel Forward", new RedGearLoadingSideForwardAuton());
		autonTaskChooser.addObject("Blue Gear Loading Side Plus Travel Forward", new BlueGearLoadingSideForwardAuton());

		autonTaskChooser.addObject("Red Gear Center Forward", new RedGearCenterForwardAuton());
		autonTaskChooser.addObject("Blue Gear Center Forward", new BlueGearCenterForwardAuton());

		autonTaskChooser.addObject("Red Gear Center Fast Forward", new RedGearCenterForwardFastAuton());
		autonTaskChooser.addObject("Blue Gear Center Fast Forward", new BlueGearCenterForwardFastAuton());

		autonTaskChooser.addDefault("Red Boiler Shoot From Hopper Adaptive Pursuit", new RedBoilerShooterFromHopperAdaptivePursuit());
		autonTaskChooser.addDefault("Blue Boiler Shoot From Hopper Adaptive Pursuit", new BlueBoilerShooterFromHopperAdaptivePursuit());

		autonTaskChooser.addObject("Red Gear Plus 10 Boiler Side Forward", new RedGearBoilerSideForwardAuton());
		autonTaskChooser.addObject("Blue Gear Plus 10 Boiler Side Forward", new BlueGearBoilerSideForwardAuton());

		autonTaskChooser.addObject("Red 10 Plus Gear Boiler Side Forward", new RedShootFirstGearBoilerSideForwardAuton());
		autonTaskChooser.addObject("Blue 10 Plus Gear Boiler Side Forward", new BlueShootFirstGearBoilerSideForwardAuton());
		
		autonTaskChooser.addObject("Red Gear Center Plus 10 Forward", new RedGearCenterShootForwardAuton());
		autonTaskChooser.addObject("Blue Gear Center Plus 10 Forward", new BlueGearCenterShootForwardAuton());
		
		autonTaskChooser.addObject("Red Gear Loading Side Plus GET TO THE HOPPA", new RedGearLoadingSideForwardWithHopperAuton());
		autonTaskChooser.addObject("Blue Gear Loading Side Plus GET TO THE HOPPA", new BlueGearLoadingSideForwardWithHopperAuton());

		SmartDashboard.putData("Auton Task", autonTaskChooser);

//		SmartDashboard.putNumber("Shooter Stage 2 Target RPM", shooterStage2RpmDashboard);
//		SmartDashboard.putNumber("Shooter Stage 1 Target RPM", shooterStage1RpmDashboard);
//		SmartDashboard.putNumber("Shooter Target RPM", shooterBothRpmDashboard);
//		SmartDashboard.putNumber("Shooter Target VBus", shooterBothVBusDashboard);
		
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
    	shooter.setStage1Speed(0);
    	shooter.setStage2Speed(0);
    	shooterFeed.setSpeed(0);
    	zarkerFeed.setSpeed(0);
    	shooter.setHopperPosition(HopperState.CLOSE);
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
    	camera.updateStatus(operationMode);
   }

}
