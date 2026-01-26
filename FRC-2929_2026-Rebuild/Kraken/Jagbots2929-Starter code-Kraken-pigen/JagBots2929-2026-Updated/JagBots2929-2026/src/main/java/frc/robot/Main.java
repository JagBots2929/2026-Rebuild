package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * Main entry point for the robot program
 * Do NOT add any static initializers or initialization code to this class
 */
public final class Main {
  private Main() {}

  /**
   * Main initialization function. Do not perform any initialization here.
   */
  public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}
