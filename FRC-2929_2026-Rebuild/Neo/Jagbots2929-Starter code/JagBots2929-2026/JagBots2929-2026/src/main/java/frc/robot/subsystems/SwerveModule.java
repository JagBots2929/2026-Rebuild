package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants.DriveConstants;

/**
 * Individual Swerve Module - controls one wheel's drive and turning
 */
public class SwerveModule {
  private final CANSparkMax driveMotor;
  private final CANSparkMax turningMotor;
  
  private final RelativeEncoder driveEncoder;
  private final RelativeEncoder turningEncoder;
  
  private final CANcoder absoluteEncoder;
  
  private final SparkPIDController drivePIDController;
  private final SparkPIDController turningPIDController;
  
  private final SimpleMotorFeedforward driveFeedforward;
  
  private double absoluteEncoderOffsetRadians;
  
  /**
   * Constructs a SwerveModule
   * 
   * @param driveMotorId CAN ID for drive motor
   * @param turningMotorId CAN ID for turning motor
   * @param absoluteEncoderId CAN ID for absolute encoder
   * @param absoluteEncoderOffsetRadians Offset for absolute encoder
   */
  public SwerveModule(int driveMotorId, int turningMotorId, int absoluteEncoderId, 
                      double absoluteEncoderOffsetRadians) {
    this.absoluteEncoderOffsetRadians = absoluteEncoderOffsetRadians;
    
    // Initialize motors
    driveMotor = new CANSparkMax(driveMotorId, MotorType.kBrushless);
    turningMotor = new CANSparkMax(turningMotorId, MotorType.kBrushless);
    
    // Configure motors
    driveMotor.restoreFactoryDefaults();
    turningMotor.restoreFactoryDefaults();
    
    driveMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    turningMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    
    // Get encoders
    driveEncoder = driveMotor.getEncoder();
    turningEncoder = turningMotor.getEncoder();
    
    // Configure absolute encoder
    absoluteEncoder = new CANcoder(absoluteEncoderId);
    CANcoderConfiguration config = new CANcoderConfiguration();
    config.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
    absoluteEncoder.getConfigurator().apply(config);
    
    // Configure encoder conversions
    driveEncoder.setPositionConversionFactor(
        (DriveConstants.WHEEL_DIAMETER_METERS * Math.PI) / DriveConstants.DRIVE_GEAR_RATIO);
    driveEncoder.setVelocityConversionFactor(
        ((DriveConstants.WHEEL_DIAMETER_METERS * Math.PI) / DriveConstants.DRIVE_GEAR_RATIO) / 60.0);
    
    turningEncoder.setPositionConversionFactor(2 * Math.PI / DriveConstants.TURNING_GEAR_RATIO);
    turningEncoder.setVelocityConversionFactor(
        (2 * Math.PI / DriveConstants.TURNING_GEAR_RATIO) / 60.0);
    
    // Get PID controllers
    drivePIDController = driveMotor.getPIDController();
    turningPIDController = turningMotor.getPIDController();
    
    // Configure PID
    drivePIDController.setP(DriveConstants.DRIVE_P);
    drivePIDController.setI(DriveConstants.DRIVE_I);
    drivePIDController.setD(DriveConstants.DRIVE_D);
    
    turningPIDController.setP(DriveConstants.TURNING_P);
    turningPIDController.setI(DriveConstants.TURNING_I);
    turningPIDController.setD(DriveConstants.TURNING_D);
    
    turningPIDController.setPositionPIDWrappingEnabled(true);
    turningPIDController.setPositionPIDWrappingMinInput(0);
    turningPIDController.setPositionPIDWrappingMaxInput(2 * Math.PI);
    
    // Feedforward
    driveFeedforward = new SimpleMotorFeedforward(0.1, 2.5);
    
    // Reset to absolute position
    resetEncoders();
  }
  
  /**
   * Returns the current position of the module
   */
  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
        driveEncoder.getPosition(),
        new Rotation2d(turningEncoder.getPosition())
    );
  }
  
  /**
   * Returns the current state of the module
   */
  public SwerveModuleState getState() {
    return new SwerveModuleState(
        driveEncoder.getVelocity(),
        new Rotation2d(turningEncoder.getPosition())
    );
  }
  
  /**
   * Sets the desired state for the module
   */
  public void setDesiredState(SwerveModuleState desiredState) {
    // Optimize the state to avoid spinning more than 90 degrees
    SwerveModuleState optimizedState = SwerveModuleState.optimize(
        desiredState,
        new Rotation2d(turningEncoder.getPosition())
    );
    
    // Calculate feedforward
    double feedforward = driveFeedforward.calculate(optimizedState.speedMetersPerSecond);
    
    // Set drive motor velocity
    drivePIDController.setReference(
        optimizedState.speedMetersPerSecond,
        CANSparkMax.ControlType.kVelocity,
        0,
        feedforward
    );
    
    // Set turning motor position
    turningPIDController.setReference(
        optimizedState.angle.getRadians(),
        CANSparkMax.ControlType.kPosition
    );
  }
  
  /**
   * Resets encoders to absolute position
   */
  public void resetEncoders() {
    driveEncoder.setPosition(0);
    double absolutePosition = getAbsoluteEncoderRadians();
    turningEncoder.setPosition(absolutePosition);
  }
  
  /**
   * Gets absolute encoder position in radians
   */
  private double getAbsoluteEncoderRadians() {
    double angle = absoluteEncoder.getAbsolutePosition().getValueAsDouble() * 2 * Math.PI;
    angle -= absoluteEncoderOffsetRadians;
    return angle;
  }
  
  /**
   * Stops the module
   */
  public void stop() {
    driveMotor.set(0);
    turningMotor.set(0);
  }
}
