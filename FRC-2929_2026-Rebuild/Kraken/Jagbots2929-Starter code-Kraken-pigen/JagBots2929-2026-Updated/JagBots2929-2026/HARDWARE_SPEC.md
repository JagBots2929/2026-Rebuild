# Hardware Specifications - JagBots 2929

## Motor Configuration

### Swerve Drive System
All swerve drive and steering motors use **Kraken X60 High Performance Brushless Motors** with Phoenix 6 control.

| Module | Function | Motor Type | CAN ID | Notes |
|--------|----------|------------|--------|-------|
| Front Left | Drive | Kraken X60 | 1 | Level 3 gear ratio (6.75:1) |
| Front Left | Steer | Kraken X60 | 2 | 12.8:1 gear ratio |
| Front Right | Drive | Kraken X60 | 3 | Level 3 gear ratio (6.75:1) |
| Front Right | Steer | Kraken X60 | 4 | 12.8:1 gear ratio |
| Back Left | Drive | Kraken X60 | 5 | Level 3 gear ratio (6.75:1) |
| Back Left | Steer | Kraken X60 | 6 | 12.8:1 gear ratio |
| Back Right | Drive | Kraken X60 | 7 | Level 3 gear ratio (6.75:1) |
| Back Right | Steer | Kraken X60 | 8 | 12.8:1 gear ratio |

**Kraken X60 Specifications:**
- Max RPM: 6000
- Continuous Current: 40A
- Peak Current: 80A  
- Control: Phoenix 6 API (TalonFX)
- Integrated encoder resolution: Built-in

### Game Piece Manipulation
All game piece systems use **NEO Brushless Motors** with REVLib control.

| System | Motor Type | CAN ID | Current Limit | Function |
|--------|------------|--------|---------------|----------|
| Intake | NEO | 13 | 40A | Collect game pieces |
| Indexer | NEO | 14 | 30A | Stage pieces for shooter |
| Shooter | NEO | 15 | 60A | Flywheel for scoring |

**NEO Motor Specifications:**
- Max RPM: 5676 (free speed)
- Continuous Current: 30A
- Peak Current: 60A+
- Control: REVLib (SparkMAX)
- Built-in hall-effect encoder

## Sensors

### Encoders
| Sensor | Type | CAN ID | Purpose |
|--------|------|--------|---------|
| Front Left Encoder | CANcoder | 9 | Absolute position for swerve module |
| Front Right Encoder | CANcoder | 10 | Absolute position for swerve module |
| Back Left Encoder | CANcoder | 11 | Absolute position for swerve module |
| Back Right Encoder | CANcoder | 12 | Absolute position for swerve module |

**CANcoder Specifications:**
- Resolution: 4096 counts per revolution
- Absolute position: 0-360° (0-1 rotations)
- Update rate: 100 Hz default
- Interface: CAN bus

### Gyroscope/IMU
| Sensor | Type | CAN ID | Purpose |
|--------|------|--------|---------|
| Pigeon 2.0 | 9-axis IMU | 20 | Heading, pitch, roll tracking |

**Pigeon 2.0 Specifications:**
- 3-axis gyroscope
- 3-axis accelerometer  
- 3-axis magnetometer
- Update rate: 200 Hz
- Heading accuracy: ±1°
- Interface: CAN bus
- Vendor: CTRE Phoenix 6

### Vision
| Sensor | Type | Connection | Purpose |
|--------|------|------------|---------|
| Limelight 2+/3 | Vision Camera | Ethernet/NetworkTables | Target detection and distance |

**Limelight Specifications:**
- Resolution: 960x720 (Limelight 2+) or 1280x800 (Limelight 3)
- Frame rate: 90 FPS
- Field of view: 59.6° x 49.7° (diagonal 74°)
- Processing: Onboard Raspberry Pi
- Outputs: NetworkTables (tx, ty, ta, tv, distance)

## Control System

### RoboRIO 2.0
- Processor: Dual-core ARM Cortex-A53 @ 1.33 GHz
- RAM: 1 GB
- Storage: 4 GB internal
- Ports:
  - USB 2.0: 2x Type-A
  - Ethernet: 2x 1000BASE-T
  - MXP: 1x (34-pin)
  - CAN: 1x
  - Power: 12V input

### Power Distribution
- Power Distribution Hub (PDH) or Power Distribution Panel (PDP)
- Main breaker: 120A
- Individual breakers: Configurable per motor

### Network
- Radio: OM5P-AC or OM5P-AN
- Connection: Ethernet to RoboRIO
- Bandwidth: 5 GHz (AC) or 2.4 GHz (AN)

## Driver Station

### Controller
- **Type**: Logitech F310 Gamepad (or compatible)
- **Connection**: USB to Driver Station laptop
- **Port**: 0
- **Mode**: DirectInput (D switch)

### Laptop
- Windows 10/11 recommended
- FRC Driver Station software
- Minimum: 2 GB RAM, Intel i3 or equivalent

## Wiring Guidelines

### CAN Bus
- Total length: < 100 feet recommended
- Termination: 120Ω resistors at both ends
- Topology: Daisy chain (no stars)
- Wire: Twisted pair, 22-24 AWG

### Power
- Battery: 12V, 18Ah FRC-approved
- Main breaker: 120A
- Motor breakers: 
  - Kraken X60: 40A breakers
  - NEO: 40A breakers for drive, 30A for game pieces

### Signal
- PWM: Not used (all motors on CAN)
- Ethernet: CAT5e or better
- USB: Type-A for controller

## Physical Specifications

### Dimensions
- Wheel base: 24" (adjustable in Constants.java)
- Track width: 24" (adjustable in Constants.java)
- Wheel diameter: 4" (adjustable in Constants.java)

### Weight Estimate
- Motors: ~12 lbs (8x Kraken + 3x NEO)
- Electronics: ~10 lbs
- Structure: TBD
- Battery: ~13 lbs
- **Note**: Must stay under 125 lbs with battery and bumpers

## Software Requirements

### Vendor Libraries
1. **CTRE Phoenix 6** (v25.0.0+)
   - For: Kraken X60, CANcoder, Pigeon 2.0
   - Installation: Via WPILib online installer
   - URL: `https://maven.ctr-electronics.com/release/`

2. **REVLib** (v2025.0.0+)
   - For: NEO motors via SparkMAX
   - Installation: Via WPILib online installer  
   - URL: `https://maven.revrobotics.com/`

### Firmware Versions
- RoboRIO: 2025.0.0+
- Kraken X60: Latest Phoenix 6 compatible
- CANcoder: Latest Phoenix 6 compatible
- Pigeon 2.0: Latest Phoenix 6 compatible
- SparkMAX: Latest REVLib compatible

## Performance Specifications

### Drive Performance
- Max speed: 5.5 m/s (theoretical, Kraken X60)
- Max rotation: 3π rad/s
- Acceleration: Configurable via code

### Shooter Performance  
- Max RPM: 5000 (limited in software)
- Spin-up time: < 1 second (with proper tuning)
- Distance range: 1-5 meters (configurable)

## Compliance Notes

### FRC Rules
- All motors are FRC-legal for 2025 season
- Kraken X60: Legal for unlimited use
- NEO: Legal for unlimited use
- All sensors are legal
- RoboRIO 2.0: Required control system

### Inspection Checklist
- [ ] All motors properly secured
- [ ] CAN bus properly terminated
- [ ] All breakers sized correctly
- [ ] Battery properly secured
- [ ] Main breaker accessible
- [ ] E-stop functional
- [ ] All wire rated for current
- [ ] No exposed conductors
- [ ] Weight under 125 lbs
- [ ] Bumpers properly installed

## Maintenance

### Pre-Match
- Check all CAN connections
- Verify battery voltage > 12.5V
- Test all motors respond to commands
- Verify encoders reading correctly
- Check Pigeon 2.0 initializes
- Verify Limelight connection

### Post-Match
- Check for loose wires
- Inspect motor mounting
- Check wheel wear
- Verify no CAN errors in Driver Station
- Check for unusual motor temperatures

### Periodic
- Update firmware as needed
- Clean sensors
- Check gear mesh on swerve modules
- Lubricate bearings if needed
- Inspect electrical connections

---

**Document Version**: 1.0  
**Last Updated**: January 2026  
**Team**: JagBots 2929
