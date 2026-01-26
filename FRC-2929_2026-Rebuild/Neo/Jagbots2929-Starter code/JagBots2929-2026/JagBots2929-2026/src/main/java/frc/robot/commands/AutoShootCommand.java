package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

/**
 * Autonomous command to align and shoot
 */
public class AutoShootCommand extends SequentialCommandGroup {
  
  public AutoShootCommand(DriveSubsystem driveSubsystem,
                         ShooterSubsystem shooterSubsystem,
                         IndexerSubsystem indexerSubsystem,
                         VisionSubsystem visionSubsystem) {
    
    addCommands(
        // Step 1: Turn on vision LEDs and start aligning
        new VisionAlignCommand(driveSubsystem, visionSubsystem)
            .withTimeout(2.0), // Align for up to 2 seconds
        
        // Step 2: Spin up shooter based on vision distance
        new ShootCommand(shooterSubsystem, indexerSubsystem, visionSubsystem)
            .withTimeout(3.0)  // Shoot for 3 seconds
    );
  }
}
