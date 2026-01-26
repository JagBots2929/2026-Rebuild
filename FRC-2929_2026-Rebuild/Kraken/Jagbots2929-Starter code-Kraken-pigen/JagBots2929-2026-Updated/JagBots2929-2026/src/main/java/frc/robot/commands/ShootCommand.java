package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

/**
 * Command to shoot with vision-based velocity control
 */
public class ShootCommand extends Command {
  private final ShooterSubsystem shooterSubsystem;
  private final IndexerSubsystem indexerSubsystem;
  private final VisionSubsystem visionSubsystem;
  
  private boolean shooterReady = false;
  
  public ShootCommand(ShooterSubsystem shooterSubsystem, 
                     IndexerSubsystem indexerSubsystem,
                     VisionSubsystem visionSubsystem) {
    this.shooterSubsystem = shooterSubsystem;
    this.indexerSubsystem = indexerSubsystem;
    this.visionSubsystem = visionSubsystem;
    
    addRequirements(shooterSubsystem, indexerSubsystem);
  }
  
  @Override
  public void initialize() {
    shooterReady = false;
    
    // Turn on Limelight LEDs
    visionSubsystem.setLEDMode(VisionSubsystem.LEDMode.ON);
    
    // Set shooter velocity based on distance
    if (visionSubsystem.hasValidTarget()) {
      double distance = visionSubsystem.getDistanceToTarget();
      shooterSubsystem.setVelocityFromDistance(distance);
    } else {
      // Default velocity if no target
      shooterSubsystem.setVelocityRPM(3000);
    }
  }
  
  @Override
  public void execute() {
    // Update shooter velocity based on current distance
    if (visionSubsystem.hasValidTarget()) {
      double distance = visionSubsystem.getDistanceToTarget();
      shooterSubsystem.setVelocityFromDistance(distance);
    }
    
    // Check if shooter is at speed
    if (shooterSubsystem.isAtTargetVelocity()) {
      shooterReady = true;
      // Feed game piece to shooter
      indexerSubsystem.feed();
    } else {
      // Don't feed until shooter is ready
      indexerSubsystem.stop();
    }
  }
  
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.stop();
    indexerSubsystem.stop();
    visionSubsystem.setLEDMode(VisionSubsystem.LEDMode.OFF);
  }
  
  @Override
  public boolean isFinished() {
    return false; // Runs until button is released
  }
}
