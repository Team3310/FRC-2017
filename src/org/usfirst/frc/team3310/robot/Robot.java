
package org.usfirst.frc.team3310.robot;

import org.usfirst.frc.team3310.robot.subsystems.BallIntake;
import org.usfirst.frc.team3310.robot.subsystems.Camera;
import org.usfirst.frc.team3310.robot.subsystems.Drive;
import org.usfirst.frc.team3310.robot.subsystems.GearIntake;
import org.usfirst.frc.team3310.robot.subsystems.MagicCarpet;
import org.usfirst.frc.team3310.robot.subsystems.Shooter;
import org.usfirst.frc.team3310.robot.subsystems.ShooterLift;
import org.usfirst.frc.team3310.utility.ControlLooper;

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
	public static final ShooterLift shooterLift = new ShooterLift();
	public static final BallIntake ballIntake = new BallIntake();
	public static final GearIntake gearIntake = new GearIntake();
	public static final MagicCarpet magicCarpet = new MagicCarpet();
	public static final Camera camera = new Camera();
	
	public static final ControlLooper controlLoop = new ControlLooper("Main control loop", 10);
	public static final PowerDistributionPanel pdp = new PowerDistributionPanel();

	public static double shooterMainSpeed = 2950;
	public static double shooterFeedSpeed = 2950;

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

    	controlLoop.addLoopable(drive);
    	controlLoop.addLoopable(ballIntake);
    	controlLoop.addLoopable(gearIntake);

	    operationModeChooser = new SendableChooser<OperationMode>();
	    operationModeChooser.addDefault("Test", OperationMode.TEST);
	    operationModeChooser.addObject("Competition", OperationMode.COMPETITION);
		SmartDashboard.putData("Operation Mode", operationModeChooser);
		
		autonTaskChooser = new SendableChooser<Command>();
		SmartDashboard.putData("Auton Task", autonTaskChooser);

		SmartDashboard.putNumber("Shooter Main Target RPM", shooterMainSpeed);
		SmartDashboard.putNumber("Shooter Feed Target RPM", shooterFeedSpeed);
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
    	ballIntake.setZeroLiftPosition();
    	ballIntake.setLiftPosition(BallIntake.RETRACTED_POSITION_DEG);
    	gearIntake.setZeroLiftPosition();
    	gearIntake.setLiftPosition(BallIntake.RETRACTED_POSITION_DEG);
    	drive.endGyroCalibration();
    	drive.resetGyro();
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
    	updateChoosers();
        controlLoop.start();
    	ballIntake.setZeroLiftPosition();
    	ballIntake.setLiftPosition(BallIntake.RETRACTED_POSITION_DEG);
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
    
    public void updateStatus() {
    	drive.updateStatus(operationMode);
    	shooter.updateStatus(operationMode);
    	shooterLift.updateStatus(operationMode);
    	magicCarpet.updateStatus(operationMode);
   }

}
