# JagBots 2929 - 2026 Season Robot Code

FRC Robot code for the 2026 rebuild game featuring:
- **4-module Swerve Drive** with Level 3 gear ratio
- **Game Piece Manipulation**: Intake, Indexer, and Shooter systems
- **Vision-Based Targeting**: Limelight integration for dynamic shooting
- **RoboRIO 2.0** control system

## Robot Configuration

### Drive System
- **4x Swerve Modules** (Level 3 configuration)
  - Drive Motors: NEO (CAN IDs 1, 3, 5, 7)
  - Turning Motors: NEO (CAN IDs 2, 4, 6, 8)
  - Absolute Encoders: CANcoders (CAN IDs 9, 10, 11, 12)
- **Gyro**: NavX connected via SPI

### Game Piece Systems
- **Intake Motor**: NEO (CAN ID 13)
- **Indexer Motor**: NEO (CAN ID 14)
- **Shooter Motor**: NEO (CAN ID 15) with flywheel

### Vision
- **Limelight Camera**: For target detection and distance measurement
- **Dynamic Velocity Control**: Shooter RPM adjusts based on distance

### Controller
- **Logitech Controller** (Port 0)
  - Left Stick: Forward/Strafe
  - Right Stick: Rotation
  - Button A (1): Intake
  - Button B (2): Outtake
  - Button X (3): Shoot with Vision
  - Button Y (4): Vision Align

## Project Structure

```
src/main/java/frc/robot/
├── Robot.java              # Main robot class
├── Main.java              # Entry point
├── Constants.java         # Robot configuration constants
├── RobotContainer.java    # Subsystem and command binding
├── subsystems/
│   ├── DriveSubsystem.java      # Swerve drive control
│   ├── SwerveModule.java        # Individual module control
│   ├── IntakeSubsystem.java     # Game piece intake
│   ├── IndexerSubsystem.java    # Game piece indexing
│   ├── ShooterSubsystem.java    # Flywheel shooter with velocity control
│   └── VisionSubsystem.java     # Limelight integration
└── commands/
    ├── IntakeCommand.java       # Intake operation
    ├── OuttakeCommand.java      # Reverse intake
    ├── ShootCommand.java        # Vision-targeted shooting
    ├── VisionAlignCommand.java  # Auto-align to target
    └── AutoShootCommand.java    # Autonomous shooting sequence
```

## Setup Instructions

### Prerequisites
1. Install WPILib (2025.1.1 or later)
2. Install FRC Game Tools
3. Install vendor dependencies:
   - REVLib (REV Robotics)
   - CTRE Phoenix 6 (for CANcoders)
   - NavX libraries (Kauai Labs)
   - Limelight libraries

### Building and Deploying

1. **Open in VS Code**:
   ```bash
   cd JagBots2929-2026
   code .
   ```

2. **Build the project**:
   - Press `Ctrl+Shift+P`
   - Type "WPILib: Build Robot Code"

3. **Deploy to robot**:
   - Connect to robot via USB or WiFi
   - Press `Ctrl+Shift+P`
   - Type "WPILib: Deploy Robot Code"

### Calibration Steps

1. **Swerve Module Offsets**:
   - Manually align all wheels pointing forward
   - Record absolute encoder values
   - Update offsets in `Constants.java` for each module

2. **Limelight Configuration**:
   - Measure camera height and mounting angle
   - Measure target height
   - Update `VisionConstants` in `Constants.java`
   - Configure Limelight pipeline for target detection

3. **Shooter Tuning**:
   - Test shooting from various distances
   - Record optimal RPM for each distance
   - Update `SHOOTER_DISTANCE_MAP` in `Constants.java`

4. **PID Tuning**:
   - Drive motors: Adjust `DRIVE_P`, `DRIVE_I`, `DRIVE_D`
   - Turning motors: Adjust `TURNING_P`, `TURNING_I`, `TURNING_D`
   - Shooter: Adjust `SHOOTER_P`, `SHOOTER_I`, `SHOOTER_D`, `SHOOTER_FF`

## Features

### Field-Oriented Drive
- Swerve drive with field-oriented control
- Smooth joystick curves with deadband
- NavX gyro integration for heading tracking

### Vision-Based Shooting
- Limelight automatically detects alliance scoring target
- Distance calculated using camera angle and target height
- Shooter velocity dynamically adjusts for optimal scoring
- LED indicators for target lock

### Autonomous Capabilities
- Vision-based alignment
- Automatic shooting sequence
- Extensible command-based architecture

## Troubleshooting

### Swerve Modules Not Aligning
- Check absolute encoder wiring and CAN IDs
- Verify encoder offsets are correctly calibrated
- Ensure turning motor PID values are tuned

### Shooter Not Reaching Speed
- Check motor current limits
- Verify flywheel is not binding
- Tune SHOOTER_FF (feedforward) value
- Check battery voltage

### Vision Not Detecting Target
- Verify Limelight network connection
- Check pipeline configuration
- Ensure adequate lighting conditions
- Verify target height and camera angle constants

### Robot Not Responding to Controller
- Check controller port number (should be 0)
- Verify driver station is connected
- Check for joystick axis mapping

## Dashboard Values

The following values are published to SmartDashboard/Shuffleboard:

**Drive**:
- Robot Heading
- Robot X/Y Position

**Intake**:
- Intake Current
- Intake Speed

**Indexer**:
- Indexer Current
- Indexer Speed

**Shooter**:
- Shooter RPM (current)
- Shooter Target RPM
- Shooter Current
- Shooter At Speed (boolean)

**Vision**:
- Target Valid (boolean)
- Target X Offset
- Target Y Offset
- Target Area
- Distance to Target

## Team Information

- **Team Number**: 2929
- **Team Name**: JagBots
- **Season**: 2026
- **Robot**: Swerve Drive with Vision Targeting

## License

This code is provided for FRC Team 2929 and educational purposes.

## Support

For questions or issues, contact the programming team or mentor.
