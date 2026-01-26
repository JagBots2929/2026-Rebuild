package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

/**
 * Intake Subsystem - controls game piece intake using NEO motor
 */
public class IntakeSubsystem extends SubsystemBase {
  private final CANSparkMax intakeMotor;
  
  public IntakeSubsystem() {
    intakeMotor = new CANSparkMax(IntakeConstants.INTAKE_MOTOR_ID, MotorType.kBrushless);
    intakeMotor.restoreFactoryDefaults();
    intakeMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    intakeMotor.setSmartCurrentLimit(40);
  }
  
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake Current", intakeMotor.getOutputCurrent());
    SmartDashboard.putNumber("Intake Speed", intakeMotor.get());
    SmartDashboard.putBoolean("Intake Has Game Piece", hasGamePiece());
  }
  
  /**
   * Runs intake to collect game pieces
   */
  public void intake() {
    intakeMotor.set(IntakeConstants.INTAKE_SPEED);
  }
  
  /**
   * Runs intake in reverse to eject game pieces
   */
  public void outtake() {
    intakeMotor.set(IntakeConstants.OUTTAKE_SPEED);
  }
  
  /**
   * Stops the intake motor
   */
  public void stop() {
    intakeMotor.set(0);
  }
  
  /**
   * Returns true if intake is drawing high current (game piece detected)
   */
  public boolean hasGamePiece() {
    return intakeMotor.getOutputCurrent() > 30;
  }
  
  /**
   * Gets the current draw of the intake motor
   */
  public double getCurrent() {
    return intakeMotor.getOutputCurrent();
  }
}
