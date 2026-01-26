package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

/**
 * Shooter Subsystem - controls flywheel for scoring
 */
public class ShooterSubsystem extends SubsystemBase {
  private final CANSparkMax shooterMotor;
  private final RelativeEncoder encoder;
  private final SparkPIDController pidController;
  
  private double targetVelocityRPM = 0;
  
  public ShooterSubsystem() {
    shooterMotor = new CANSparkMax(ShooterConstants.SHOOTER_MOTOR_ID, MotorType.kBrushless);
    shooterMotor.restoreFactoryDefaults();
    shooterMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
    shooterMotor.setSmartCurrentLimit(60);
    
    encoder = shooterMotor.getEncoder();
    pidController = shooterMotor.getPIDController();
    
    // Configure PID
    pidController.setP(ShooterConstants.SHOOTER_P);
    pidController.setI(ShooterConstants.SHOOTER_I);
    pidController.setD(ShooterConstants.SHOOTER_D);
    pidController.setFF(ShooterConstants.SHOOTER_FF);
    pidController.setOutputRange(-1, 1);
  }
  
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter RPM", encoder.getVelocity());
    SmartDashboard.putNumber("Shooter Target RPM", targetVelocityRPM);
    SmartDashboard.putNumber("Shooter Current", shooterMotor.getOutputCurrent());
    SmartDashboard.putBoolean("Shooter At Speed", isAtTargetVelocity());
  }
  
  /**
   * Sets shooter velocity based on distance to target
   * 
   * @param distanceMeters Distance to target in meters
   */
  public void setVelocityFromDistance(double distanceMeters) {
    double rpm = interpolateRPM(distanceMeters);
    setVelocityRPM(rpm);
  }
  
  /**
   * Sets shooter velocity in RPM
   */
  public void setVelocityRPM(double rpm) {
    targetVelocityRPM = Math.min(rpm, ShooterConstants.MAX_SHOOTER_RPM);
    pidController.setReference(targetVelocityRPM, CANSparkMax.ControlType.kVelocity);
  }
  
  /**
   * Interpolates RPM from distance using the shooter distance map
   */
  private double interpolateRPM(double distance) {
    double[][] map = ShooterConstants.SHOOTER_DISTANCE_MAP;
    
    // If distance is less than minimum, use minimum
    if (distance <= map[0][0]) {
      return map[0][1];
    }
    
    // If distance is greater than maximum, use maximum
    if (distance >= map[map.length - 1][0]) {
      return map[map.length - 1][1];
    }
    
    // Find the two points to interpolate between
    for (int i = 0; i < map.length - 1; i++) {
      if (distance >= map[i][0] && distance <= map[i + 1][0]) {
        // Linear interpolation
        double d1 = map[i][0];
        double d2 = map[i + 1][0];
        double rpm1 = map[i][1];
        double rpm2 = map[i + 1][1];
        
        double ratio = (distance - d1) / (d2 - d1);
        return rpm1 + ratio * (rpm2 - rpm1);
      }
    }
    
    return map[0][1]; // Fallback
  }
  
  /**
   * Returns true if shooter is at target velocity
   */
  public boolean isAtTargetVelocity() {
    return Math.abs(encoder.getVelocity() - targetVelocityRPM) 
        < ShooterConstants.SHOOTER_TOLERANCE_RPM;
  }
  
  /**
   * Gets current shooter velocity in RPM
   */
  public double getVelocityRPM() {
    return encoder.getVelocity();
  }
  
  /**
   * Stops the shooter
   */
  public void stop() {
    targetVelocityRPM = 0;
    shooterMotor.set(0);
  }
  
  /**
   * Runs shooter at idle speed for quick response
   */
  public void idle() {
    setVelocityRPM(1000);
  }
}
