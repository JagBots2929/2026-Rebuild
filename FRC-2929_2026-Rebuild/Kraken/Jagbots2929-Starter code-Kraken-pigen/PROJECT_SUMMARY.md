# JagBots 2929 - 2026 Robot Code Summary

## 📦 What's Included

This is a complete, competition-ready FRC robot code package for Team 2929's 2026 season robot.

### Robot Configuration
- **RoboRIO 2.0** with 2025 WPILib
- **4-Module Swerve Drive** (Level 3 gear ratio) - Kraken X60 motors
- **3 Game Piece Systems**: Intake, Indexer, Shooter - NEO Brushless motors
- **Vision Targeting**: Limelight for distance-based shooting
- **Logitech Controller**: Full button mapping

### Code Architecture

**Subsystems** (6 total):
1. `DriveSubsystem` - Swerve drive with field-oriented control
2. `SwerveModule` - Individual module control
3. `IntakeSubsystem` - Game piece collection
4. `IndexerSubsystem` - Game piece staging
5. `ShooterSubsystem` - Velocity-controlled flywheel
6. `VisionSubsystem` - Limelight integration

**Commands** (5 total):
1. `IntakeCommand` - Run intake and indexer
2. `OuttakeCommand` - Reverse intake/indexer
3. `ShootCommand` - Vision-targeted shooting
4. `VisionAlignCommand` - Auto-align to target
5. `AutoShootCommand` - Autonomous shooting sequence

### Key Features

✅ **Field-Oriented Drive**: Driver-centric controls with NavX gyro
✅ **Dynamic Shooter Control**: RPM adjusts based on vision distance
✅ **Vision Auto-Align**: Automatic target alignment
✅ **Current Limiting**: Brownout protection
✅ **Smart Game Piece Detection**: Current-based sensing
✅ **Extensible Commands**: Easy to add new autonomous routines
✅ **Comprehensive Logging**: SmartDashboard integration

## 🗂️ File Structure

```
JagBots2929-2026/
├── src/main/java/frc/robot/
│   ├── Robot.java                    # Main robot class
│   ├── Main.java                     # Entry point
│   ├── Constants.java                # All configuration constants
│   ├── RobotContainer.java           # Subsystem bindings
│   ├── subsystems/                   # 6 subsystem classes
│   └── commands/                     # 5 command classes
├── vendordeps/                       # Vendor libraries
│   ├── Phoenix6.json                 # CTRE (CANcoders)
│   ├── REVLib.json                   # REV (NEO motors)
│   └── navx_frc.json                 # NavX gyro
├── build.gradle                      # Build configuration
├── settings.gradle                   # Gradle settings
├── bootstrap.py                      # Setup utility script
├── README.md                         # Full documentation
├── QUICKSTART.md                     # 5-minute setup guide
├── TUNING_GUIDE.md                   # Complete tuning reference
└── .gitignore                        # Git ignore rules
```

## 🚀 Quick Start

### 1. Prerequisites
- WPILib 2025.1.1+
- FRC Game Tools
- VS Code with WPILib extension

### 2. Open & Build
```bash
cd JagBots2929-2026
code .
# Press Ctrl+Shift+P -> "WPILib: Build Robot Code"
```

### 3. Deploy
```bash
# Press Ctrl+Shift+P -> "WPILib: Deploy Robot Code"
```

### 4. Calibrate (First Time)
```bash
python3 bootstrap.py calibrate
```

See **QUICKSTART.md** for detailed steps.

## 🎮 Default Controls

| Button | Action |
|--------|--------|
| Left Stick | Forward/Backward & Strafe |
| Right Stick | Rotation |
| A Button | Intake |
| B Button | Outtake |
| X Button | Shoot (Vision) |
| Y Button | Vision Align |

## 🔧 What You Need to Configure

### Critical (Do First):
1. **Swerve Offsets**: Calibrate each module (see QUICKSTART.md)
2. **Motor IDs**: Verify all CAN IDs match your robot
3. **Limelight**: Measure heights and angles

### Important (Do Before Competition):
4. **Shooter Map**: Test and record distance→RPM values
5. **PID Values**: Tune for your specific robot (see TUNING_GUIDE.md)
6. **Current Limits**: Adjust to prevent brownouts

### Optional (Enhance Performance):
7. **Joystick Curves**: Adjust for driver preference
8. **Speed Limits**: Scale for practice vs competition
9. **Auto Routines**: Add custom autonomous commands

## 📚 Documentation

- **README.md**: Comprehensive documentation
- **QUICKSTART.md**: Get running in 5 minutes
- **TUNING_GUIDE.md**: Complete PID and system tuning
- **Code Comments**: Every class and method documented

## 🔌 Hardware Requirements

### Motors:
- **Swerve Drive:** 8x Kraken X60 High Performance Brushless (drive & steer)
- **Game Piece Systems:** 3x NEO Brushless (intake, indexer, shooter)

### Sensors:
- 4x CANcoder (swerve absolute encoders)
- 1x Pigeon 2.0 gyro (CAN connection, ID 20)
- 1x Limelight camera (NetworkTables)

### Controller:
- RoboRIO 2.0
- Power Distribution Hub
- Radio
- Battery

## ⚙️ Technologies Used

- **WPILib 2025**: FRC robotics framework
- **Command-Based**: Modern robot architecture
- **REVLib**: NEO motor control
- **Phoenix 6**: CANcoder support
- **NavX**: Gyro/IMU integration
- **NetworkTables**: Limelight communication

## 🎯 Code Quality Features

- ✅ Null safety
- ✅ Comprehensive error handling
- ✅ Consistent naming conventions
- ✅ Extensive documentation
- ✅ Modular architecture
- ✅ Easy to extend and modify

## 🧪 Testing Recommendations

### Unit Testing:
- Test each subsystem independently
- Verify motor IDs and CAN connections
- Check encoder readings

### Integration Testing:
- Drive in all directions
- Test full intake→shoot cycle
- Verify vision alignment
- Test autonomous routine

### Competition Testing:
- Practice matches
- Battery swap tests
- Driver training
- Backup code validation

## 🆘 Support

### Resources:
1. Check inline code comments
2. Read QUICKSTART.md or TUNING_GUIDE.md
3. Review README.md
4. Use bootstrap.py diagnostics

### Common Issues:
- See QUICKSTART.md "Common Issues" section
- See TUNING_GUIDE.md "Troubleshooting" table

## 📊 Expected Performance

With proper tuning:
- **Drive Speed**: 4.5 m/s max
- **Rotation Rate**: 2π rad/s (full rotation per second)
- **Shooter Spin-Up**: < 1 second
- **Vision Alignment**: < 2 seconds
- **Full Cycle Time**: Intake→Shoot < 3 seconds

## 🏆 Competition Readiness

This code is designed to be:
- ✅ Competition-legal
- ✅ Safe for practice and matches
- ✅ Easy to diagnose and fix
- ✅ Extensible for strategy changes
- ✅ Well-documented for new programmers

## 📝 Version Info

- **Created**: 2026 Season
- **Team**: JagBots 2929
- **WPILib**: 2025.1.1
- **Language**: Java 17
- **Framework**: Command-Based

## 🎓 Learning Resources

New to FRC programming? Check out:
- [WPILib Documentation](https://docs.wpilib.org/)
- [Chief Delphi Forums](https://www.chiefdelphi.com/)
- [REV Robotics Resources](https://docs.revrobotics.com/)
- Inline code comments in this project

---

**Ready to deploy? Follow QUICKSTART.md to get started!**

Good luck at competition! 🤖🏆
