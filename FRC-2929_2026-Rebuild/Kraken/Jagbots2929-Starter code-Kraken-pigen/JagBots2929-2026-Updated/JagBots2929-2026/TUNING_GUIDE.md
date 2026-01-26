# Tuning Guide - JagBots 2929

Complete guide for tuning all robot systems.

## ⚙️ Motor Configuration

**Swerve Drive & Steer:** Kraken X60 High Performance Brushless Motors (Phoenix 6 API)
**Game Piece Systems:** NEO Brushless Motors (REVLib API)
- Intake: NEO
- Indexer: NEO  
- Shooter: NEO

## 📐 Swerve Drive Tuning

### Drive Motor PID

**Current Values** (in `Constants.java`):
```java
public static final double DRIVE_P = 0.1;
public static final double DRIVE_I = 0.0;
public static final double DRIVE_D = 0.0;
```

**Tuning Process**:

1. **Start with defaults**: P=0.1, I=0, D=0
2. **Increase P** until robot oscillates slightly when driving
3. **Reduce P** by 20-30%
4. **Add D** if overshooting (typically 10x P value)
5. **Add I** only if steady-state error exists (typically P/100)

**Testing**:
- Drive forward at constant speed
- Check for oscillation or sluggish response
- Verify accurate distance tracking

### Turning Motor PID

**Current Values**:
```java
public static final double TURNING_P = 0.5;
public static final double TURNING_I = 0.0;
public static final double TURNING_D = 0.0;
```

**Tuning Process**:

1. **Start with P=0.5**
2. Manually rotate wheel and watch response
3. **Increase P** if too slow to respond
4. **Decrease P** if oscillating
5. **Add D** to reduce oscillation (typically 0.1)

**Testing**:
- Command module to face different directions
- Should snap to position without overshoot
- No continuous oscillation

### Module Calibration

**Measure Encoder Offsets**:

1. **Disconnect power** and manually align wheels
2. All wheels should point **straight forward**
3. **Power on** robot
4. Open SmartDashboard/Shuffleboard
5. Read absolute encoder values
6. Record these values as offsets

**Example**:
```java
// If absolute encoder reads 0.234 when aligned forward:
private final SwerveModule frontLeft = new SwerveModule(
    ...,
    0.234  // This is your offset
);
```

## 🎯 Shooter Tuning

### Velocity Control PID

**Current Values**:
```java
public static final double SHOOTER_P = 0.0001;
public static final double SHOOTER_I = 0.0;
public static final double SHOOTER_D = 0.0;
public static final double SHOOTER_FF = 0.0002;
```

**Tuning Process**:

1. **Start with FF (Feedforward)**:
   - Set desired RPM (e.g., 3000)
   - Adjust FF until close to target
   - Formula: `FF ≈ 1.0 / max_RPM`
   
2. **Tune P**:
   - Start at 0.0001
   - Increase until reaching target quickly
   - Should settle within 1 second
   
3. **Add D** if oscillating (typically 10x P)

4. **Add I** for steady-state error (rarely needed)

**Testing**:
```
Target: 3000 RPM
Acceptable range: 2950-3050 RPM (±50)
Settling time: < 1 second
```

### Distance-to-RPM Mapping

**Method 1: Manual Testing**
1. Place robot at known distance (use tape measure)
2. Shoot and observe if game piece reaches target
3. Adjust RPM up/down as needed
4. Record optimal RPM for that distance
5. Repeat for 5-7 different distances

**Method 2: Physics Calculation**
1. Measure shooter angle
2. Calculate trajectory using projectile motion
3. Use as starting point, then test

**Update Map**:
```java
public static final double[][] SHOOTER_DISTANCE_MAP = {
    {1.0, 2500},  // Test and adjust these values
    {2.0, 3000},
    {3.0, 3500},
    {4.0, 4000},
    {5.0, 4500}
};
```

## 📷 Vision System Tuning

### Limelight Configuration

**Physical Setup**:
1. Mount securely to avoid vibration
2. Measure and record:
   - Height from floor: `_____` inches
   - Mounting angle: `_____` degrees
   - Horizontal offset from robot center: `_____` inches

**Pipeline Setup**:
1. Access Limelight web interface: `http://limelight.local:5801`
2. Create pipeline for your target type
3. Adjust:
   - **Hue**: Color of target (typically green/yellow)
   - **Saturation**: 150-255 recommended
   - **Value**: 150-255 recommended
   - **Area**: Minimum target size (adjust based on distance)
   - **Aspect Ratio**: Target shape constraints

**Distance Calculation Tuning**:

Update in `Constants.java`:
```java
public static final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(24.0);
public static final double TARGET_HEIGHT_METERS = Units.inchesToMeters(104.0);
public static final double CAMERA_ANGLE_DEGREES = 30.0;
```

**Verification**:
1. Place robot at known distance
2. Read calculated distance from dashboard
3. Compare to actual measured distance
4. Adjust camera angle constant if needed

### Vision Alignment PID

**In VisionSubsystem.java**:
```java
public double getRotationAdjustment() {
    double kP = 0.03;  // Tune this value
    return -getXOffset() * kP;
}
```

**Tuning**:
- Increase kP if robot aligns too slowly
- Decrease kP if robot oscillates
- Target: Smooth alignment in 1-2 seconds

## ⚡ Current Limits

Adjust in subsystem constructors to prevent brownouts:

```java
// Example in IntakeSubsystem
intakeMotor.setSmartCurrentLimit(40);  // Amps
```

**Recommended Limits**:
- Drive motors: 40A
- Intake: 30-40A
- Indexer: 30A
- Shooter: 60A

**Signs of brownout**:
- RoboRIO reboots during operation
- Motors suddenly stop
- Lights dim

**Solutions**:
- Reduce current limits
- Check battery condition
- Avoid running all motors at once
- Use ramp rates

## 🔄 Control Response Tuning

### Joystick Deadband

In `RobotContainer.java`:
```java
private double applyDeadband(double value) {
    if (Math.abs(value) < OIConstants.JOYSTICK_DEADBAND) {
        return 0;
    }
    return value;
}
```

Adjust `JOYSTICK_DEADBAND` in Constants:
- Too low: Robot drifts
- Too high: Less responsive
- Typical: 0.05-0.15

### Drive Speed Scaling

In `Constants.java`:
```java
public static final double MAX_SPEED_METERS_PER_SECOND = 4.5;
public static final double MAX_ANGULAR_SPEED = Math.PI * 2;
```

Adjust for:
- Competition: Use full speed
- Practice: Reduce for safety
- New drivers: Start at 50%

## 📊 Dashboard Tuning Widgets

**Add to SmartDashboard for live tuning**:

```java
// In subsystem periodic() method:
SmartDashboard.putNumber("Shooter P", SHOOTER_P);
SmartDashboard.putNumber("Shooter FF", SHOOTER_FF);

// Read back:
double p = SmartDashboard.getNumber("Shooter P", SHOOTER_P);
```

⚠️ **Remember**: Changes via dashboard are temporary! Update Constants.java with final values.

## ✅ Tuning Checklist

### Before Competition:
- [ ] Swerve modules calibrated and aligned
- [ ] Drive PID smooth and responsive
- [ ] Shooter reaches target speeds reliably
- [ ] Vision calculates accurate distances
- [ ] Auto-align is smooth and fast
- [ ] Current limits prevent brownouts
- [ ] All tuned values saved to Constants.java

### Test Routine:
1. **Swerve**: Drive in all directions, rotate
2. **Shooter**: Test at 3 distances minimum
3. **Vision**: Verify alignment and distance
4. **Auto**: Run autonomous routine
5. **Full Cycle**: Intake → Index → Shoot

## 🆘 Troubleshooting

| Problem | Likely Cause | Solution |
|---------|-------------|----------|
| Swerve oscillates | P too high | Reduce TURNING_P by 20% |
| Swerve sluggish | P too low | Increase TURNING_P by 20% |
| Shooter overshoots | FF too high | Reduce SHOOTER_FF |
| Shooter won't reach speed | FF too low | Increase SHOOTER_FF |
| Vision bounces | kP too high | Reduce rotation kP |
| Vision too slow | kP too low | Increase rotation kP |
| Random brownouts | Current too high | Reduce motor limits |

## 📝 Log Your Values

Keep a tuning log:
```
Date: ____
Tested: ____
Changed: ____
Result: ____
Final Values: ____
```

Good luck tuning! 🎯
