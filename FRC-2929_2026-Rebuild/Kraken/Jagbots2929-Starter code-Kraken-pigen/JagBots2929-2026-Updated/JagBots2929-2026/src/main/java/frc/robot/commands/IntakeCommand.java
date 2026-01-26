package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IndexerSubsystem;

/**
 * Command to run intake and indexer to collect game pieces
 */
public class IntakeCommand extends Command {
  private final IntakeSubsystem intakeSubsystem;
  private final IndexerSubsystem indexerSubsystem;
  
  public IntakeCommand(IntakeSubsystem intakeSubsystem, IndexerSubsystem indexerSubsystem) {
    this.intakeSubsystem = intakeSubsystem;
    this.indexerSubsystem = indexerSubsystem;
    
    addRequirements(intakeSubsystem, indexerSubsystem);
  }
  
  @Override
  public void initialize() {
    intakeSubsystem.intake();
    indexerSubsystem.feed();
  }
  
  @Override
  public void execute() {
    // Continue running intake and indexer
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
