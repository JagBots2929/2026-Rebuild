# Quick Start Guide - JagBots 2929

## 🚀 Fast Setup (5 minutes)

### 1. Install Required Software
- [WPILib 2025](https://github.com/wpilibsuite/allwpilib/releases)
- [FRC Game Tools](https://www.ni.com/en-us/support/downloads/drivers/download.frc-game-tools.html)

### 2. Open Project
```bash
cd JagBots2929-2026
code .
```

### 3. Build Code
- Press `Ctrl+Shift+P`
- Type "WPILib: Build Robot Code"
- Wait for build to complete

### 4. Deploy to Robot
- Connect to robot WiFi or USB
- Press `Ctrl+Shift+P`
- Type "WPILib: Deploy Robot Code"

## 🎮 Controller Layout

**Logitech Controller (Port 0)**:
- **Left Stick**: Forward/Backward & Strafe
- **Right Stick**: Rotation
- **A Button**: Run Intake
- **B Button**: Run Outtake (Reverse)
- **X Button**: Shoot (Vision Targeted)
- **Y Button**: Vision Align

## ⚙️ First-Time Calibration

### Swerve Modules
1. Manually align all wheels pointing forward
2. Run: `python3 bootstrap.py calibrate`
3. Record encoder values from SmartDashboard
4. Update offsets in `Constants.java`:
   ```java
   private final SwerveModule frontLeft = new SwerveModule(
       DriveConstants.FRONT_LEFT_DRIVE_MOTOR_ID,
       DriveConstants.FRONT_LEFT_TURNING_MOTOR_ID,
       DriveConstants.FRONT_LEFT_ENCODER_ID,
       YOUR_MEASURED_OFFSET); // Update this!
   ```

### Limelight
1. Measure camera height from floor: `______` inches
2. Measure camera angle: `______` degrees
3. Measure target height: `______` inches
4. Update `VisionConstants` in `Constants.java`
5. Configure Limelight pipeline for your target

### Shooter
1. Drive to known distances (1m, 2m, 3m, etc.)
2. Test shooting and record optimal RPM
3. Update `SHOOTER_DISTANCE_MAP` in `Constants.java`

## 🔧 Common Issues

### Robot Won't Enable
- Check battery voltage (>12V)
- Verify RoboRIO connection
- Check for code errors in Driver Station

### Swerve Not Working
- Verify all CAN IDs are correct
- Check encoder offsets are calibrated
- Ensure NavX is connected and recognized

### Shooter Not Spinning
- Check motor current limits
- Verify flywheel is free to spin
- Check for CAN errors

### Vision Not Detecting
- Ensure Limelight is connected to network
- Check pipeline is configured correctly
- Verify target is in view and lit properly

## 📊 Dashboard Setup

1. Open Shuffleboard or SmartDashboard
2. Key values to monitor:
   - Robot Heading
   - Shooter RPM vs Target RPM
   - Vision: Target Valid & Distance
   - Motor currents

## 🔌 Motor ID Cheat Sheet

| System | Motor Type | Motor | CAN ID |
|--------|------------|-------|--------|
| Front Left | Kraken X60 | Drive | 1 |
| Front Left | Kraken X60 | Turn | 2 |
| Front Left | CANcoder | Encoder | 9 |
| Front Right | Kraken X60 | Drive | 3 |
| Front Right | Kraken X60 | Turn | 4 |
| Front Right | CANcoder | Encoder | 10 |
| Back Left | Kraken X60 | Drive | 5 |
| Back Left | Kraken X60 | Turn | 6 |
| Back Left | CANcoder | Encoder | 11 |
| Back Right | Kraken X60 | Drive | 7 |
| Back Right | Kraken X60 | Turn | 8 |
| Back Right | CANcoder | Encoder | 12 |
| Intake | NEO | Motor | 13 |
| Indexer | NEO | Motor | 14 |
| Shooter | NEO | Motor | 15 |
| Gyro | Pigeon 2.0 | IMU | 20 |
| Gyro | Pigeon 2.0 | - | 20 |

## 📞 Help

If stuck, check:
1. README.md - Full documentation
2. TUNING_GUIDE.md - PID tuning help
3. Code comments - Inline documentation
4. Ask your mentor or programming lead

## ✅ Pre-Match Checklist

- [ ] Battery charged (>12.5V)
- [ ] All motors respond to commands
- [ ] Swerve modules align correctly
- [ ] Vision system detecting targets
- [ ] Shooter reaches target speeds
- [ ] Controller paired and responsive
- [ ] Code deployed successfully
- [ ] Radio configured correctly
- [ ] Bumpers and robot legal
- [ ] Driver Station connected

Good luck! 🤖🏆
