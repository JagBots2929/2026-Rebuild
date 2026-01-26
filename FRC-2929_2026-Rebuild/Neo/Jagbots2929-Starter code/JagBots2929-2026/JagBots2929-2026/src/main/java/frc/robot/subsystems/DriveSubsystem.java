package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

/**
 * Swerve Drive Subsystem - controls all 4 swerve modules
 */
public class DriveSubsystem extends SubsystemBase {
  // Swerve modules
  private final SwerveModule frontLeft = new SwerveModule(
      DriveConstants.FRONT_LEFT_DRIVE_MOTOR_ID,
      DriveConstants.FRONT_LEFT_TURNING_MOTOR_ID,
      DriveConstants.FRONT_LEFT_ENCODER_ID,
      0.0); // Set actual offset after calibration
  
  private final SwerveModule frontRight = new SwerveModule(
      DriveConstants.FRONT_RIGHT_DRIVE_MOTOR_ID,
      DriveConstants.FRONT_RIGHT_TURNING_MOTOR_ID,
      DriveConstants.FRONT_RIGHT_ENCODER_ID,
      0.0); // Set actual offset after calibration
  
  private final SwerveModule backLeft = new SwerveModule(
      DriveConstants.BACK_LEFT_DRIVE_MOTOR_ID,
      DriveConstants.BACK_LEFT_TURNING_MOTOR_ID,
      DriveConstants.BACK_LEFT_ENCODER_ID,
      0.0); // Set actual offset after calibration
  
  private final SwerveModule backRight = new SwerveModule(
      DriveConstants.BACK_RIGHT_DRIVE_MOTOR_ID,
      DriveConstants.BACK_RIGHT_TURNING_MOTOR_ID,
      DriveConstants.BACK_RIGHT_ENCODER_ID,
      0.0); // Set actual offset after calibration
  
  // Gyro
  private final AHRS gyro = new AHRS(SPI.Port.kMXP);
  
  // Odometry
  private final SwerveDriveOdometry odometry;
  
  public DriveSubsystem() {
    // Zero gyro on startup
    new Thread(() -> {
      try {
        Thread.sleep(1000);
        zeroHeading();
      } catch (Exception e) {
      }
    }).start();
    
    odometry = new SwerveDriveOdometry(
        DriveConstants.DRIVE_KINEMATICS,
        getRotation2d(),
        getModulePositions()
    );
  }
  
  @Override
  public void periodic() {
    // Update odometry
    odometry.update(getRotation2d(), getModulePositions());
    
    // Update dashboard
    SmartDashboard.putNumber("Robot Heading", getHeading());
    SmartDashboard.putNumber("Robot X", odometry.getPoseMeters().getX());
    SmartDashboard.putNumber("Robot Y", odometry.getPoseMeters().getY());
  }
  
  /**
   * Returns the current position of all modules
   */
  public SwerveModulePosition[] getModulePositions() {
    return new SwerveModulePosition[] {
        frontLeft.getPosition(),
        frontRight.getPosition(),
        backLeft.getPosition(),
        backRight.getPosition()
    };
  }
  
  /**
   * Returns the current state of all modules
   */
  public SwerveModuleState[] getModuleStates() {
    return new SwerveModuleState[] {
        frontLeft.getState(),
        frontRight.getState(),
        backLeft.getState(),
        backRight.getState()
    };
  }
  
  /**
   * Returns the currently-estimated pose of the robot
   */
  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }
  
  /**
   * Resets the odometry to the specified pose
   */
  public void resetOdometry(Pose2d pose) {
    odometry.resetPosition(getRotation2d(), getModulePositions(), pose);
  }
  
  /**
   * Method to drive the robot using joystick info
   * 
   * @param xSpeed Speed in the x direction (forward/backward)
   * @param ySpeed Speed in the y direction (side to side)
   * @param rot Angular rate of the robot
   * @param fieldRelative Whether the provided speeds are field relative
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    // Convert speeds to meters per second
    xSpeed *= DriveConstants.MAX_SPEED_METERS_PER_SECOND;
    ySpeed *= DriveConstants.MAX_SPEED_METERS_PER_SECOND;
    rot *= DriveConstants.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND;
    
    // Calculate desired chassis speeds
    ChassisSpeeds chassisSpeeds;
    if (fieldRelative) {
      chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
          xSpeed, ySpeed, rot, getRotation2d());
    } else {
      chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, rot);
    }
    
    // Convert chassis speeds to individual module states
    SwerveModuleState[] moduleStates = 
        DriveConstants.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
    
    setModuleStates(moduleStates);
  }
  
  /**
   * Sets the swerve ModuleStates
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    // Normalize wheel speeds
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, DriveConstants.MAX_SPEED_METERS_PER_SECOND);
    
    frontLeft.setDesiredState(desiredStates[0]);
    frontRight.setDesiredState(desiredStates[1]);
    backLeft.setDesiredState(desiredStates[2]);
    backRight.setDesiredState(desiredStates[3]);
  }
  
  /**
   * Resets the drive encoders to currently read a position of 0
   */
  public void resetEncoders() {
    frontLeft.resetEncoders();
    frontRight.resetEncoders();
    backLeft.resetEncoders();
    backRight.resetEncoders();
  }
  
  /**
   * Zeroes the heading of the robot
   */
  public void zeroHeading() {
    gyro.reset();
  }
  
  /**
   * Returns the heading of the robot in degrees
   */
  public double getHeading() {
    return Math.IEEEremainder(gyro.getAngle(), 360);
  }
  
  /**
   * Returns the turn rate of the robot
   */
  public double getTurnRate() {
    return gyro.getRate() * (gyro.isBoardYawAxis() ? -1.0 : 1.0);
  }
  
  /**
   * Returns the Rotation2d of the robot
   */
  public Rotation2d getRotation2d() {
    return Rotation2d.fromDegrees(getHeading());
  }
  
  /**
   * Stops all modules
   */
  public void stopModules() {
    frontLeft.stop();
    frontRight.stop();
    backLeft.stop();
    backRight.stop();
  }
}
