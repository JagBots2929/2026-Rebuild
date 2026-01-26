package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants.DriveConstants;

/**
 * Individual Swerve Module - controls one wheel's drive and turning
 * Uses Kraken X60 motors for both drive and steer
 */
public class SwerveModule {
  private final TalonFX driveMotor;
  private final TalonFX turningMotor;
  
  private final CANcoder absoluteEncoder;
  
  private final VelocityVoltage driveVelocityRequest;
  private final PositionVoltage turningPositionRequest;
  
  private double absoluteEncoderOffsetRotations;
  
  /**
   * Constructs a SwerveModule with Kraken X60 motors
   * 
   * @param driveMotorId CAN ID for drive motor (Kraken X60)
   * @param turningMotorId CAN ID for turning motor (Kraken X60)
   * @param absoluteEncoderId CAN ID for absolute encoder (CANcoder)
   * @param absoluteEncoderOffsetRotations Offset for absolute encoder in rotations
   */
  public SwerveModule(int driveMotorId, int turningMotorId, int absoluteEncoderId, 
                      double absoluteEncoderOffsetRotations) {
    this.absoluteEncoderOffsetRotations = absoluteEncoderOffsetRotations;
    
    // Initialize Kraken X60 motors
    driveMotor = new TalonFX(driveMotorId);
    turningMotor = new TalonFX(turningMotorId);
    
    // Configure absolute encoder
    absoluteEncoder = new CANcoder(absoluteEncoderId);
    CANcoderConfiguration cancoderConfig = new CANcoderConfiguration();
    cancoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
    absoluteEncoder.getConfigurator().apply(cancoderConfig);
    
    // Configure drive motor (Kraken X60)
    TalonFXConfiguration driveConfig = new TalonFXConfiguration();
    
    // Drive motor PID
    driveConfig.Slot0.kP = DriveConstants.DRIVE_KP;
    driveConfig.Slot0.kI = DriveConstants.DRIVE_KI;
    driveConfig.Slot0.kD = DriveConstants.DRIVE_KD;
    driveConfig.Slot0.kS = DriveConstants.DRIVE_KS;
    driveConfig.Slot0.kV = DriveConstants.DRIVE_KV;
    driveConfig.Slot0.kA = DriveConstants.DRIVE_KA;
    
    // Drive motor current limits
    driveConfig.CurrentLimits.SupplyCurrentLimit = DriveConstants.DRIVE_CURRENT_LIMIT;
    driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    driveConfig.CurrentLimits.StatorCurrentLimit = 80.0;
    driveConfig.CurrentLimits.StatorCurrentLimitEnable = true;
    
    // Drive motor neutral mode
    driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    
    // Apply configuration
    driveMotor.getConfigurator().apply(driveConfig);
    
    // Configure turning motor (Kraken X60)
    TalonFXConfiguration turningConfig = new TalonFXConfiguration();
    
    // Use CANcoder as remote sensor for turning
    turningConfig.Feedback.FeedbackRemoteSensorID = absoluteEncoderId;
    turningConfig.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
    turningConfig.Feedback.RotorToSensorRatio = DriveConstants.TURNING_GEAR_RATIO;
    
    // Turning motor PID
    turningConfig.Slot0.kP = DriveConstants.TURNING_KP;
    turningConfig.Slot0.kI = DriveConstants.TURNING_KI;
    turningConfig.Slot0.kD = DriveConstants.TURNING_KD;
    turningConfig.Slot0.kS = DriveConstants.TURNING_KS;
    turningConfig.Slot0.kV = DriveConstants.TURNING_KV;
    
    // Turning motor current limits
    turningConfig.CurrentLimits.SupplyCurrentLimit = DriveConstants.TURNING_CURRENT_LIMIT;
    turningConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    turningConfig.CurrentLimits.StatorCurrentLimit = 60.0;
    turningConfig.CurrentLimits.StatorCurrentLimitEnable = true;
    
    // Turning motor neutral mode
    turningConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    
    // Enable continuous wrap for turning (allows shortest path)
    turningConfig.ClosedLoopGeneral.ContinuousWrap = true;
    
    // Apply configuration
    turningMotor.getConfigurator().apply(turningConfig);
    
    // Initialize control requests
    driveVelocityRequest = new VelocityVoltage(0).withSlot(0);
    turningPositionRequest = new PositionVoltage(0).withSlot(0);
    
    // Reset to absolute position
    resetEncoders();
  }
  
  /**
   * Returns the current position of the module
   */
  public SwerveModulePosition getPosition() {
    // Convert drive motor rotations to meters
    double driveRotations = driveMotor.getPosition().getValueAsDouble();
    double driveMeters = driveRotations * (Math.PI * DriveConstants.WHEEL_DIAMETER_METERS) 
        / DriveConstants.DRIVE_GEAR_RATIO;
    
    // Get turning angle from CANcoder
    double turningRotations = absoluteEncoder.getAbsolutePosition().getValueAsDouble();
    double adjustedRotations = turningRotations - absoluteEncoderOffsetRotations;
    
    return new SwerveModulePosition(
        driveMeters,
        Rotation2d.fromRotations(adjustedRotations)
    );
  }
  
  /**
   * Returns the current state of the module
   */
  public SwerveModuleState getState() {
    // Convert drive motor velocity to m/s
    double driveRotationsPerSec = driveMotor.getVelocity().getValueAsDouble();
    double driveMetersPerSec = driveRotationsPerSec * (Math.PI * DriveConstants.WHEEL_DIAMETER_METERS) 
        / DriveConstants.DRIVE_GEAR_RATIO;
    
    // Get turning angle from CANcoder
    double turningRotations = absoluteEncoder.getAbsolutePosition().getValueAsDouble();
    double adjustedRotations = turningRotations - absoluteEncoderOffsetRotations;
    
    return new SwerveModuleState(
        driveMetersPerSec,
        Rotation2d.fromRotations(adjustedRotations)
    );
  }
  
  /**
   * Sets the desired state for the module
   */
  public void setDesiredState(SwerveModuleState desiredState) {
    // Get current angle
    double currentRotations = absoluteEncoder.getAbsolutePosition().getValueAsDouble() 
        - absoluteEncoderOffsetRotations;
    Rotation2d currentAngle = Rotation2d.fromRotations(currentRotations);
    
    // Optimize the state to avoid spinning more than 90 degrees
    SwerveModuleState optimizedState = SwerveModuleState.optimize(desiredState, currentAngle);
    
    // Convert desired speed to rotations per second for Kraken
    double desiredDriveRotationsPerSec = optimizedState.speedMetersPerSecond 
        * DriveConstants.DRIVE_GEAR_RATIO 
        / (Math.PI * DriveConstants.WHEEL_DIAMETER_METERS);
    
    // Set drive motor velocity
    driveMotor.setControl(driveVelocityRequest.withVelocity(desiredDriveRotationsPerSec));
    
    // Convert desired angle to rotations and add offset
    double desiredTurningRotations = optimizedState.angle.getRotations() 
        + absoluteEncoderOffsetRotations;
    
    // Set turning motor position
    turningMotor.setControl(turningPositionRequest.withPosition(desiredTurningRotations));
  }
  
  /**
   * Resets encoders to absolute position
   */
  public void resetEncoders() {
    // Reset drive encoder to zero
    driveMotor.setPosition(0);
    
    // Turning uses CANcoder directly, so just sync the Talon position
    double absolutePosition = absoluteEncoder.getAbsolutePosition().getValueAsDouble();
    turningMotor.setPosition(absolutePosition);
  }
  
  /**
   * Stops the module
   */
  public void stop() {
    driveMotor.setControl(driveVelocityRequest.withVelocity(0));
    // Keep turning motor at current position when stopped
  }
  
  /**
   * Gets the absolute encoder position in rotations
   */
  public double getAbsoluteEncoderRotations() {
    return absoluteEncoder.getAbsolutePosition().getValueAsDouble();
  }
  
  /**
   * Gets drive motor current draw in amps
   */
  public double getDriveCurrent() {
    return driveMotor.getSupplyCurrent().getValueAsDouble();
  }
  
  /**
   * Gets turning motor current draw in amps
   */
  public double getTurningCurrent() {
    return turningMotor.getSupplyCurrent().getValueAsDouble();
  }
}
