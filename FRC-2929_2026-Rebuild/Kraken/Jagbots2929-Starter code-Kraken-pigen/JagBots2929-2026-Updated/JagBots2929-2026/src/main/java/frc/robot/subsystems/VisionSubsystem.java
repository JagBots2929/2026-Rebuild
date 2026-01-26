package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.VisionConstants;

/**
 * Vision Subsystem - interfaces with Limelight for target tracking
 */
public class VisionSubsystem extends SubsystemBase {
  private final NetworkTable limelightTable;
  private final NetworkTableEntry tx;
  private final NetworkTableEntry ty;
  private final NetworkTableEntry ta;
  private final NetworkTableEntry tv;
  
  public VisionSubsystem() {
    limelightTable = NetworkTableInstance.getDefault().getTable(VisionConstants.LIMELIGHT_NAME);
    tx = limelightTable.getEntry("tx");
    ty = limelightTable.getEntry("ty");
    ta = limelightTable.getEntry("ta");
    tv = limelightTable.getEntry("tv");
    
    // Set LED mode to off by default
    setLEDMode(LEDMode.OFF);
  }
  
  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Target Valid", hasValidTarget());
    SmartDashboard.putNumber("Target X Offset", getXOffset());
    SmartDashboard.putNumber("Target Y Offset", getYOffset());
    SmartDashboard.putNumber("Target Area", getArea());
    SmartDashboard.putNumber("Distance to Target", getDistanceToTarget());
  }
  
  /**
   * Returns true if limelight has a valid target
   */
  public boolean hasValidTarget() {
    return tv.getDouble(0) == 1;
  }
  
  /**
   * Returns horizontal offset from crosshair to target (-27 to 27 degrees)
   */
  public double getXOffset() {
    return tx.getDouble(0.0);
  }
  
  /**
   * Returns vertical offset from crosshair to target (-20.5 to 20.5 degrees)
   */
  public double getYOffset() {
    return ty.getDouble(0.0);
  }
  
  /**
   * Returns target area (0% to 100% of image)
   */
  public double getArea() {
    return ta.getDouble(0.0);
  }
  
  /**
   * Calculates distance to target in meters using limelight angle
   */
  public double getDistanceToTarget() {
    if (!hasValidTarget()) {
      return 0.0;
    }
    
    double angleToGoalDegrees = VisionConstants.CAMERA_ANGLE_DEGREES + getYOffset();
    double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);
    
    double distance = (VisionConstants.TARGET_HEIGHT_METERS - VisionConstants.CAMERA_HEIGHT_METERS) 
        / Math.tan(angleToGoalRadians);
    
    return distance;
  }
  
  /**
   * Returns rotational adjustment needed to align with target
   * Positive = turn right, Negative = turn left
   */
  public double getRotationAdjustment() {
    if (!hasValidTarget()) {
      return 0.0;
    }
    
    // Simple proportional control
    double kP = 0.03;
    return -getXOffset() * kP;
  }
  
  /**
   * Sets LED mode
   */
  public void setLEDMode(LEDMode mode) {
    limelightTable.getEntry("ledMode").setNumber(mode.getValue());
  }
  
  /**
   * Sets pipeline
   */
  public void setPipeline(int pipeline) {
    limelightTable.getEntry("pipeline").setNumber(pipeline);
  }
  
  /**
   * LED Mode enum
   */
  public enum LEDMode {
    PIPELINE(0),
    OFF(1),
    BLINK(2),
    ON(3);
    
    private final int value;
    
    LEDMode(int value) {
      this.value = value;
    }
    
    public int getValue() {
      return value;
    }
  }
}
