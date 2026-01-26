package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IndexerConstants;

/**
 * Indexer Subsystem - controls game piece movement to shooter using NEO motor
 */
public class IndexerSubsystem extends SubsystemBase {
  private final CANSparkMax indexerMotor;
  
  public IndexerSubsystem() {
    indexerMotor = new CANSparkMax(IndexerConstants.INDEXER_MOTOR_ID, MotorType.kBrushless);
    indexerMotor.restoreFactoryDefaults();
    indexerMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    indexerMotor.setSmartCurrentLimit(30);
  }
  
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Indexer Current", indexerMotor.getOutputCurrent());
    SmartDashboard.putNumber("Indexer Speed", indexerMotor.get());
    SmartDashboard.putBoolean("Indexer Has Game Piece", hasGamePiece());
  }
  
  /**
   * Feeds game piece to shooter
   */
  public void feed() {
    indexerMotor.set(IndexerConstants.INDEXER_FEED_SPEED);
  }
  
  /**
   * Reverses indexer
   */
  public void reverse() {
    indexerMotor.set(IndexerConstants.INDEXER_REVERSE_SPEED);
  }
  
  /**
   * Stops the indexer motor
   */
  public void stop() {
    indexerMotor.set(0);
  }
  
  /**
   * Returns true if indexer is drawing high current (game piece detected)
   */
  public boolean hasGamePiece() {
    return indexerMotor.getOutputCurrent() > 20;
  }
}
