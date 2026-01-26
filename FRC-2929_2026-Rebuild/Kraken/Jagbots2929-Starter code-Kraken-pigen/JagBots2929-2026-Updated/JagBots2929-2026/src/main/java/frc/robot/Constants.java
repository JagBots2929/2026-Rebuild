package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

/**
 * Constants for JagBots 2929 - 2026 Season
 */
public final class Constants {
  
  public static final class DriveConstants {
    // CAN IDs for Swerve Modules (Kraken X60 motors)
    public static final int FRONT_LEFT_DRIVE_MOTOR_ID = 1;
    public static final int FRONT_LEFT_TURNING_MOTOR_ID = 2;
    public static final int FRONT_LEFT_ENCODER_ID = 9;
    
    public static final int FRONT_RIGHT_DRIVE_MOTOR_ID = 3;
    public static final int FRONT_RIGHT_TURNING_MOTOR_ID = 4;
    public static final int FRONT_RIGHT_ENCODER_ID = 10;
    
    public static final int BACK_LEFT_DRIVE_MOTOR_ID = 5;
    public static final int BACK_LEFT_TURNING_MOTOR_ID = 6;
    public static final int BACK_LEFT_ENCODER_ID = 11;
    
    public static final int BACK_RIGHT_DRIVE_MOTOR_ID = 7;
    public static final int BACK_RIGHT_TURNING_MOTOR_ID = 8;
    public static final int BACK_RIGHT_ENCODER_ID = 12;
    
    // Pigeon 2.0 Gyro CAN ID
    public static final int PIGEON_ID = 20;
    
    // Drivetrain dimensions
    public static final double TRACK_WIDTH = Units.inchesToMeters(24.0);
    public static final double WHEEL_BASE = Units.inchesToMeters(24.0);
    
    // Swerve kinematics
    public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
        new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2),  // Front Left
        new Translation2d(WHEEL_BASE / 2, -TRACK_WIDTH / 2), // Front Right
        new Translation2d(-WHEEL_BASE / 2, TRACK_WIDTH / 2), // Back Left
        new Translation2d(-WHEEL_BASE / 2, -TRACK_WIDTH / 2) // Back Right
    );
    
    // Drive motor configuration (Kraken X60)
    public static final double MAX_SPEED_METERS_PER_SECOND = 5.5; // Kraken X60 is faster
    public static final double MAX_ANGULAR_SPEED_RADIANS_PER_SECOND = Math.PI * 3;
    
    // Swerve module configuration
    public static final double WHEEL_DIAMETER_METERS = Units.inchesToMeters(4.0);
    public static final double DRIVE_GEAR_RATIO = 6.75; // L3 gear ratio
    public static final double TURNING_GEAR_RATIO = 12.8;
    
    // Kraken X60 specifications
    public static final double KRAKEN_MAX_RPM = 6000;
    
    // PID constants for drive motors (Kraken X60 with Phoenix 6)
    public static final double DRIVE_KP = 0.1;
    public static final double DRIVE_KI = 0.0;
    public static final double DRIVE_KD = 0.0;
    public static final double DRIVE_KS = 0.1;  // Static friction
    public static final double DRIVE_KV = 0.12; // Velocity feedforward
    public static final double DRIVE_KA = 0.01; // Acceleration feedforward
    
    // PID constants for turning motors (Kraken X60 with Phoenix 6)
    public static final double TURNING_KP = 8.0;
    public static final double TURNING_KI = 0.0;
    public static final double TURNING_KD = 0.2;
    public static final double TURNING_KS = 0.2;
    public static final double TURNING_KV = 0.12;
    
    // Current limits for Kraken X60
    public static final double DRIVE_CURRENT_LIMIT = 60.0; // Amps
    public static final double TURNING_CURRENT_LIMIT = 40.0; // Amps
  }
  
  public static final class IntakeConstants {
    public static final int INTAKE_MOTOR_ID = 13;
    public static final double INTAKE_SPEED = 0.8;
    public static final double OUTTAKE_SPEED = -0.5;
  }
  
  public static final class IndexerConstants {
    public static final int INDEXER_MOTOR_ID = 14;
    public static final double INDEXER_FEED_SPEED = 0.6;
    public static final double INDEXER_REVERSE_SPEED = -0.4;
  }
  
  public static final class ShooterConstants {
    public static final int SHOOTER_MOTOR_ID = 15;
    
    // Shooter velocity mapping based on distance (in meters)
    // Distance -> RPM
    public static final double[][] SHOOTER_DISTANCE_MAP = {
        {1.0, 2500},  // 1 meter -> 2500 RPM
        {2.0, 3000},  // 2 meters -> 3000 RPM
        {3.0, 3500},  // 3 meters -> 3500 RPM
        {4.0, 4000},  // 4 meters -> 4000 RPM
        {5.0, 4500}   // 5 meters -> 4500 RPM
    };
    
    public static final double SHOOTER_P = 0.0001;
    public static final double SHOOTER_I = 0.0;
    public static final double SHOOTER_D = 0.0;
    public static final double SHOOTER_FF = 0.0002;
    
    public static final double SHOOTER_TOLERANCE_RPM = 50;
    public static final double MAX_SHOOTER_RPM = 5000;
  }
  
  public static final class VisionConstants {
    public static final String LIMELIGHT_NAME = "limelight";
    public static final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(24.0);
    public static final double TARGET_HEIGHT_METERS = Units.inchesToMeters(104.0);
    public static final double CAMERA_ANGLE_DEGREES = 30.0;
  }
  
  public static final class OIConstants {
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final double JOYSTICK_DEADBAND = 0.1;
    
    // Button mappings
    public static final int INTAKE_BUTTON = 1;  // A button
    public static final int OUTTAKE_BUTTON = 2; // B button
    public static final int SHOOT_BUTTON = 3;   // X button
    public static final int VISION_ALIGN_BUTTON = 4; // Y button
  }
}
