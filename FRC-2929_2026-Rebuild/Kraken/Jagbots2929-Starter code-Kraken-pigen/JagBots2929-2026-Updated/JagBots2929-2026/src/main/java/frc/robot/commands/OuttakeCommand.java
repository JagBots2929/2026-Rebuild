package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IndexerSubsystem;

/**
 * Command to reverse intake and indexer to eject game pieces
 */
public class OuttakeCommand extends Command {
  private final IntakeSubsystem intakeSubsystem;
  private final IndexerSubsystem indexerSubsystem;
  
  public OuttakeCommand(IntakeSubsystem intakeSubsystem, IndexerSubsystem indexerSubsystem) {
    this.intakeSubsystem = intakeSubsystem;
    this.indexerSubsystem = indexerSubsystem;
    
    addRequirements(intakeSubsystem, indexerSubsystem);
  }
  
  @Override
  public void initialize() {
    intakeSubsystem.outtake();
    indexerSubsystem.reverse();
  }
  
  @Override
  public void execute() {
    // Continue running in reverse
  }
  
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stop();
    indexerSubsystem.stop();
  }
  
  @Override
  public boolean isFinished() {
    return false; // Runs until button is released
  }
}
