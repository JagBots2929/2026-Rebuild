#!/usr/bin/env python3
"""
JagBots 2929 - Robot Bootstrap Script
Provides utilities for robot setup, calibration, and testing
"""

import subprocess
import sys
import json
import time
from pathlib import Path

class RobotBootstrap:
    """Bootstrap utilities for FRC robot setup"""
    
    def __init__(self):
        self.team_number = 2929
        self.robot_name = "JagBots-2026"
        
    def check_dependencies(self):
        """Check if required dependencies are installed"""
        print("🔍 Checking dependencies...")
        
        dependencies = [
            ("WPILib", "gradle"),
            ("Git", "git"),
        ]
        
        missing = []
        for name, command in dependencies:
            if not self._command_exists(command):
                missing.append(name)
                print(f"  ❌ {name} not found")
            else:
                print(f"  ✅ {name} found")
        
        if missing:
            print(f"\n⚠️  Missing dependencies: {', '.join(missing)}")
            print("Please install missing dependencies before continuing.")
            return False
        
        print("✅ All dependencies found!\n")
        return True
    
    def _command_exists(self, command):
        """Check if a command exists in PATH"""
        try:
            subprocess.run(
                [command, "--version"],
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                check=False
            )
            return True
        except FileNotFoundError:
            return False
    
    def build_robot_code(self):
        """Build the robot code"""
        print("🔨 Building robot code...")
        try:
            result = subprocess.run(
                ["./gradlew", "build"],
                check=True,
                capture_output=True,
                text=True
            )
            print("✅ Build successful!")
            return True
        except subprocess.CalledProcessError as e:
            print(f"❌ Build failed: {e}")
            print(e.stderr)
            return False
    
    def deploy_to_robot(self):
        """Deploy code to robot"""
        print(f"🚀 Deploying to robot (Team {self.team_number})...")
        try:
            result = subprocess.run(
                ["./gradlew", "deploy"],
                check=True,
                capture_output=True,
                text=True
            )
            print("✅ Deployment successful!")
            return True
        except subprocess.CalledProcessError as e:
            print(f"❌ Deployment failed: {e}")
            print(e.stderr)
            return False
    
    def generate_calibration_config(self):
        """Generate a calibration configuration template"""
        print("📝 Generating calibration configuration...")
        
        config = {
            "swerve_modules": {
                "front_left": {
                    "drive_motor_id": 1,
                    "turning_motor_id": 2,
                    "encoder_id": 9,
                    "offset_radians": 0.0,
                    "description": "Measure with wheels pointing forward"
                },
                "front_right": {
                    "drive_motor_id": 3,
                    "turning_motor_id": 4,
                    "encoder_id": 10,
                    "offset_radians": 0.0,
                    "description": "Measure with wheels pointing forward"
                },
                "back_left": {
                    "drive_motor_id": 5,
                    "turning_motor_id": 6,
                    "encoder_id": 11,
                    "offset_radians": 0.0,
                    "description": "Measure with wheels pointing forward"
                },
                "back_right": {
                    "drive_motor_id": 7,
                    "turning_motor_id": 8,
                    "encoder_id": 12,
                    "offset_radians": 0.0,
                    "description": "Measure with wheels pointing forward"
                }
            },
            "vision": {
                "camera_height_inches": 24.0,
                "target_height_inches": 104.0,
                "camera_angle_degrees": 30.0,
                "description": "Measure camera mounting height and angle"
            },
            "shooter": {
                "distance_rpm_map": [
                    {"distance_meters": 1.0, "rpm": 2500},
                    {"distance_meters": 2.0, "rpm": 3000},
                    {"distance_meters": 3.0, "rpm": 3500},
                    {"distance_meters": 4.0, "rpm": 4000},
                    {"distance_meters": 5.0, "rpm": 4500}
                ],
                "description": "Test and adjust RPM values for each distance"
            }
        }
        
        config_path = Path("calibration_config.json")
        with open(config_path, 'w') as f:
            json.dump(config, f, indent=2)
        
        print(f"✅ Calibration config saved to {config_path}")
        print("📋 Edit this file with your measured values, then update Constants.java")
    
    def run_diagnostics(self):
        """Run basic robot diagnostics"""
        print("🔬 Running diagnostics...")
        
        checks = [
            ("Network", self._check_network),
            ("RoboRIO", self._check_roborio),
            ("CAN Bus", self._check_can_bus),
        ]
        
        results = []
        for name, check_func in checks:
            print(f"\n  Testing {name}...")
            result = check_func()
            results.append((name, result))
            if result:
                print(f"  ✅ {name} OK")
            else:
                print(f"  ⚠️  {name} check failed")
        
        print("\n📊 Diagnostics Summary:")
        for name, result in results:
            status = "✅ PASS" if result else "❌ FAIL"
            print(f"  {status}: {name}")
    
    def _check_network(self):
        """Check network connectivity to robot"""
        try:
            result = subprocess.run(
                ["ping", "-c", "1", f"roboRIO-{self.team_number}-FRC.local"],
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                timeout=5,
                check=False
            )
            return result.returncode == 0
        except:
            return False
    
    def _check_roborio(self):
        """Check RoboRIO connection"""
        # This would need actual robot connection
        # For now, just check if we can resolve the hostname
        return self._check_network()
    
    def _check_can_bus(self):
        """Check CAN bus status"""
        # This would require actual robot connection and Phoenix diagnostics
        # Placeholder for now
        print("    (Requires robot connection - skipping)")
        return None
    
    def print_motor_ids(self):
        """Print motor ID reference"""
        print("\n🔌 Motor ID Reference:")
        print("\n  Swerve Drive:")
        print("    Front Left  - Drive: 1,  Turn: 2,  Encoder: 9")
        print("    Front Right - Drive: 3,  Turn: 4,  Encoder: 10")
        print("    Back Left   - Drive: 5,  Turn: 6,  Encoder: 11")
        print("    Back Right  - Drive: 7,  Turn: 8,  Encoder: 12")
        print("\n  Game Piece Systems:")
        print("    Intake:  13")
        print("    Indexer: 14")
        print("    Shooter: 15")
        print()
    
    def interactive_menu(self):
        """Display interactive menu"""
        while True:
            print("\n" + "="*50)
            print(f"  JagBots {self.team_number} - Robot Bootstrap")
            print("="*50)
            print("\n1. Check Dependencies")
            print("2. Build Robot Code")
            print("3. Deploy to Robot")
            print("4. Build and Deploy")
            print("5. Generate Calibration Config")
            print("6. Run Diagnostics")
            print("7. Print Motor ID Reference")
            print("8. Exit")
            
            choice = input("\nSelect option (1-8): ").strip()
            
            if choice == "1":
                self.check_dependencies()
            elif choice == "2":
                self.build_robot_code()
            elif choice == "3":
                self.deploy_to_robot()
            elif choice == "4":
                if self.build_robot_code():
                    self.deploy_to_robot()
            elif choice == "5":
                self.generate_calibration_config()
            elif choice == "6":
                self.run_diagnostics()
            elif choice == "7":
                self.print_motor_ids()
            elif choice == "8":
                print("\n👋 Goodbye!")
                break
            else:
                print("❌ Invalid option. Please select 1-8.")
            
            input("\nPress Enter to continue...")


def main():
    """Main entry point"""
    print("""
    ╔═══════════════════════════════════════════════╗
    ║   JagBots 2929 - Robot Bootstrap Utility     ║
    ║            2026 Season                        ║
    ╚═══════════════════════════════════════════════╝
    """)
    
    bootstrap = RobotBootstrap()
    
    # Check if running with arguments
    if len(sys.argv) > 1:
        command = sys.argv[1].lower()
        
        if command == "check":
            bootstrap.check_dependencies()
        elif command == "build":
            bootstrap.build_robot_code()
        elif command == "deploy":
            bootstrap.deploy_to_robot()
        elif command == "all":
            if bootstrap.check_dependencies() and bootstrap.build_robot_code():
                bootstrap.deploy_to_robot()
        elif command == "calibrate":
            bootstrap.generate_calibration_config()
        elif command == "diagnose":
            bootstrap.run_diagnostics()
        elif command == "ids":
            bootstrap.print_motor_ids()
        else:
            print(f"❌ Unknown command: {command}")
            print("\nAvailable commands:")
            print("  check      - Check dependencies")
            print("  build      - Build robot code")
            print("  deploy     - Deploy to robot")
            print("  all        - Check, build, and deploy")
            print("  calibrate  - Generate calibration config")
            print("  diagnose   - Run diagnostics")
            print("  ids        - Print motor ID reference")
    else:
        # Run interactive menu
        bootstrap.interactive_menu()


if __name__ == "__main__":
    main()
