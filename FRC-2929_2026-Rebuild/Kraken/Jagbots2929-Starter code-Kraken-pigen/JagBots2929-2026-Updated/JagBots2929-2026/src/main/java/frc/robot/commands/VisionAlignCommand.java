package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

/**
 * Command to automatically align robot with vision target
 */
public class VisionAlignCommand extends Command {
  private final DriveSubsystem driveSubsystem;
  private final VisionSubsystem visionSubsystem;
  
  private static final double ALIGNMENT_TOLERANCE = 2.0; // degrees
  
  public VisionAlignCommand(DriveSubsystem driveSubsystem, VisionSubsystem visionSubsystem) {
    this.driveSubsystem = driveSubsystem;
    this.visionSubsystem = visionSubsystem;
    
    addRequirements(driveSubsystem);
  }
  
  @Override
  public void initialize() {
    // Turn on Limelight LEDs
    visionSubsystem.setLEDMode(VisionSubsystem.LEDMode.ON);
  }
  
  @Override
  public void execute() {
    if (visionSubsystem.hasValidTarget()) {
      // Get rotation adjustment from vision
      double rotationSpeed = visionSubsystem.getRotationAdjustment();
      
      // Apply rotation while maintaining driver's forward/strafe input
      driveSubsystem.drive(0, 0, rotationSpeed, true);
    } else {
      // No target - stop rotation
      driveSubsystem.drive(0, 0, 0, true);
    }
  }
  
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.drive(0, 0, 0, true);
    visionSubsystem.setLEDMode(VisionSubsystem.LEDMode.OFF);
  }
  
  @Override
  public boolean isFinished() {
    // Continue until button is released
    return false;
  }
  
  /**
   * Returns true if robot is aligned with target
   */
  public boolean isAligned() {
    return visionSubsystem.hasValidTarget() 
        && Math.abs(visionSubsystem.getXOffset()) < ALIGNMENT_TOLERANCE;
  }
}
