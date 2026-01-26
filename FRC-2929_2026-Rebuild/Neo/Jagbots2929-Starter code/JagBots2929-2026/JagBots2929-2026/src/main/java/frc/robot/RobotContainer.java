package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * Container for robot subsystems, commands, and button mappings
 */
public class RobotContainer {
  // Subsystems
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final IndexerSubsystem indexerSubsystem = new IndexerSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final VisionSubsystem visionSubsystem = new VisionSubsystem();
  
  // Controllers
  private final Joystick driverController = new Joystick(OIConstants.DRIVER_CONTROLLER_PORT);
  
  public RobotContainer() {
    configureDefaultCommands();
    configureButtonBindings();
  }
  
  /**
   * Configures default commands for subsystems
   */
  private void configureDefaultCommands() {
    // Default drive command - field-oriented swerve drive
    driveSubsystem.setDefaultCommand(
        new RunCommand(
            () -> driveSubsystem.drive(
                -applyDeadband(driverController.getRawAxis(1)), // Forward/backward (inverted)
                -applyDeadband(driverController.getRawAxis(0)), // Left/right (inverted)
                -applyDeadband(driverController.getRawAxis(2)), // Rotation
                true), // Field-oriented
            driveSubsystem));
  }
  
  /**
   * Configures button bindings
   */
  private void configureButtonBindings() {
    // Button 1 (A) - Run intake
    new JoystickButton(driverController, OIConstants.INTAKE_BUTTON)
        .whileTrue(new IntakeCommand(intakeSubsystem, indexerSubsystem));
    
    // Button 2 (B) - Outtake/reverse
    new JoystickButton(driverController, OIConstants.OUTTAKE_BUTTON)
        .whileTrue(new OuttakeCommand(intakeSubsystem, indexerSubsystem));
    
    // Button 3 (X) - Shoot with vision targeting
    new JoystickButton(driverController, OIConstants.SHOOT_BUTTON)
        .whileTrue(new ShootCommand(shooterSubsystem, indexerSubsystem, visionSubsystem));
    
    // Button 4 (Y) - Vision align
    new JoystickButton(driverController, OIConstants.VISION_ALIGN_BUTTON)
        .whileTrue(new VisionAlignCommand(driveSubsystem, visionSubsystem));
  }
  
  /**
   * Applies deadband to joystick input
   */
  private double applyDeadband(double value) {
    if (Math.abs(value) < OIConstants.JOYSTICK_DEADBAND) {
      return 0;
    }
    return value;
  }
  
  /**
   * Returns the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Example auto command - you can expand this
    return new AutoShootCommand(
        driveSubsystem,
        shooterSubsystem,
        indexerSubsystem,
        visionSubsystem
    );
  }
  
  /**
   * Returns drive subsystem for testing
   */
  public DriveSubsystem getDriveSubsystem() {
    return driveSubsystem;
  }
}
