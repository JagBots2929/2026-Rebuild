# Hardware Configuration - JagBots 2929

## 🤖 Complete Motor & Sensor Layout

### Swerve Drive System (Kraken X60)

All drive and steer motors use **Kraken X60 High Performance Brushless Motors** controlled via **CTRE Phoenix 6 API**.

#### Front Left Module
- **Drive Motor:** Kraken X60 (CAN ID 1)
- **Steer Motor:** Kraken X60 (CAN ID 2)
- **Absolute Encoder:** CANcoder (CAN ID 9)

#### Front Right Module
- **Drive Motor:** Kraken X60 (CAN ID 3)
- **Steer Motor:** Kraken X60 (CAN ID 4)
- **Absolute Encoder:** CANcoder (CAN ID 10)

#### Back Left Module
- **Drive Motor:** Kraken X60 (CAN ID 5)
- **Steer Motor:** Kraken X60 (CAN ID 6)
- **Absolute Encoder:** CANcoder (CAN ID 11)

#### Back Right Module
- **Drive Motor:** Kraken X60 (CAN ID 7)
- **Steer Motor:** Kraken X60 (CAN ID 8)
- **Absolute Encoder:** CANcoder (CAN ID 12)

**Total Kraken X60 Motors:** 8 (4 drive + 4 steer)

### Game Piece Manipulation (NEO)

All game piece motors use **NEO Brushless Motors** controlled via **REVLib API**.

- **Intake Motor:** NEO (CAN ID 13)
  - Function: Collect game pieces from field
  - Current Limit: 40A
  
- **Indexer Motor:** NEO (CAN ID 14)
  - Function: Stage game pieces for shooting
  - Current Limit: 30A
  
- **Shooter Motor:** NEO (CAN ID 15)
  - Function: Launch game pieces (flywheel)
  - Current Limit: 60A

**Total NEO Motors:** 3

### Sensors & Navigation

#### Pigeon 2.0 IMU
- **CAN ID:** 20
- **Connection:** CAN bus
- **API:** CTRE Phoenix 6
- **Purpose:** Robot heading, pitch, roll, and angular velocity
- **Features:**
  - 3-axis gyroscope
  - 3-axis accelerometer  
  - 3-axis magnetometer
  - Field-oriented drive support

#### CANcoders (4x)
- **CAN IDs:** 9, 10, 11, 12
- **API:** CTRE Phoenix 6
- **Purpose:** Absolute position feedback for swerve modules
- **Resolution:** 4096 counts per revolution

#### Limelight Camera
- **Connection:** NetworkTables via Ethernet
- **Purpose:** Vision targeting for dynamic shooting
- **Features:**
  - Target detection and tracking
  - Distance calculation
  - Automatic shooter velocity adjustment

## 🔌 CAN Bus Layout

```
CAN ID | Device              | Type
-------|---------------------|-------------------
1      | FL Drive Motor      | Kraken X60
2      | FL Steer Motor      | Kraken X60
3      | FR Drive Motor      | Kraken X60
4      | FR Steer Motor      | Kraken X60
5      | BL Drive Motor      | Kraken X60
6      | BL Steer Motor      | Kraken X60
7      | BR Drive Motor      | Kraken X60
8      | BR Steer Motor      | Kraken X60
9      | FL CANcoder         | Absolute Encoder
10     | FR CANcoder         | Absolute Encoder
11     | BL CANcoder         | Absolute Encoder
12     | BR CANcoder         | Absolute Encoder
13     | Intake Motor        | NEO
14     | Indexer Motor       | NEO
15     | Shooter Motor       | NEO
20     | Pigeon 2.0 Gyro     | IMU
```

## ⚡ Power & Current Management

### Kraken X60 Current Limits
- **Drive Motors:** 60A supply, 80A stator
- **Steer Motors:** 40A supply, 60A stator

### NEO Current Limits
- **Intake:** 40A
- **Indexer:** 30A
- **Shooter:** 60A

### Total Peak Current Draw
- **Swerve Drive:** ~320A (8 motors × 40A average)
- **Game Pieces:** ~130A (3 motors at peak)
- **Total:** ~450A peak (ensure battery and PDP can handle)

## 📡 Communication Protocols

### CAN Bus Devices
- 8x Kraken X60 motors (Phoenix 6)
- 4x CANcoders (Phoenix 6)
- 1x Pigeon 2.0 (Phoenix 6)
- 3x NEO motors via Spark MAX (REVLib)

### Network Devices
- 1x Limelight camera (NetworkTables)
- 1x RoboRIO 2.0 (main controller)
- 1x Radio (robot-driver station communication)

## 🛠️ Software Libraries Required

### CTRE Phoenix 6
**Purpose:** Control Kraken X60 motors, CANcoders, and Pigeon 2.0
**Devices:**
- TalonFX motor controllers (Kraken X60)
- CANcoder absolute encoders
- Pigeon2 IMU

### REVLib
**Purpose:** Control NEO motors via Spark MAX controllers
**Devices:**
- CANSparkMax motor controllers
- NEO brushless motors

### WPILib
**Purpose:** Core FRC framework
**Features:**
- Robot control framework
- Swerve kinematics
- Command-based architecture
- NetworkTables (Limelight)

## 🔧 Wiring Checklist

### CAN Bus
- [ ] All 8 Kraken X60 motors connected to CAN
- [ ] All 4 CANcoders connected to CAN
- [ ] Pigeon 2.0 connected to CAN
- [ ] All 3 Spark MAX controllers connected to CAN
- [ ] CAN bus terminated at both ends
- [ ] No duplicate CAN IDs

### Power
- [ ] All motors connected to PDH/PDP
- [ ] Battery secured and charged
- [ ] Main breaker accessible
- [ ] Emergency stop functional

### Network
- [ ] Limelight connected to robot radio via Ethernet
- [ ] RoboRIO connected to robot radio
- [ ] Static IP configured for Limelight (if needed)

### Sensors
- [ ] Pigeon 2.0 mounted securely (minimize vibration)
- [ ] CANcoders aligned with modules
- [ ] Limelight has clear view of targets

## 📝 Initial Configuration Steps

1. **Flash Firmware**
   - Update Kraken X60 firmware via Phoenix Tuner
   - Update Spark MAX firmware via REV Hardware Client
   - Update Pigeon 2.0 firmware via Phoenix Tuner
   - Update CANcoder firmware via Phoenix Tuner

2. **Set CAN IDs**
   - Configure each device with correct CAN ID
   - Use Phoenix Tuner for CTRE devices
   - Use REV Hardware Client for Spark MAX controllers
   - Document all IDs

3. **Calibrate Sensors**
   - Zero Pigeon 2.0 gyro
   - Calibrate CANcoder offsets (wheels forward)
   - Configure Limelight pipeline
   - Test sensor readings in Driver Station

4. **Test Motors**
   - Verify each motor spins in correct direction
   - Check for proper current limiting
   - Ensure no mechanical binding
   - Validate encoder feedback

## 🚨 Troubleshooting

### CAN Bus Issues
- **No communication:** Check termination, wiring
- **Intermittent errors:** Check for damaged cables
- **High bus utilization:** Reduce update rates if needed

### Motor Issues
- **Not spinning:** Check power, CAN ID, firmware
- **Wrong direction:** Invert motor in code
- **Overheating:** Check current limits, mechanical binding
- **Encoder errors:** Check connections, calibration

### Sensor Issues
- **Pigeon drift:** Re-zero, check mounting
- **CANcoder errors:** Check magnet alignment, distance
- **Limelight no targets:** Check pipeline, lighting, view

## 📞 Support Resources

- **CTRE Phoenix 6:** https://pro.docs.ctr-electronics.com/
- **REVLib:** https://docs.revrobotics.com/
- **WPILib:** https://docs.wpilib.org/
- **Limelight:** https://docs.limelightvision.io/

---

**Last Updated:** January 2026
**Robot:** JagBots 2929 - 2026 Season
