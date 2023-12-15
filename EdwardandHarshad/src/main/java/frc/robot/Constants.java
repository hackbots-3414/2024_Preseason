// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.fasterxml.jackson.databind.ser.std.SqlTimeSerializer;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  static {
    SmartDashboard.putNumber("Gearbox Ratio", 11.3);
  }
  public static double GEARBOX = SmartDashboard.getNumber("Gearbox Ratio", 11.3);

  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class RamseteConstants {
    public static final double ksVolts = 0.13624;
    public static final double kvVoltSecondsPerMeter = 2.453;
    public static final double kaVoltSecondsSquaredPerMeter = 0.30992;
    public static final double kPDriveVel = 1.9767;
    public static final double kTrackwidthMeter = 0.69;
    public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackwidthMeter);
    public static final double kMaxSpeedMetersPerSecond = 2;
    public static final double kMaxAccelerationMetersPerSecondSquared = 1;
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;
  }

  public static class DriveStraightConstants {
    public static final double DRIVE_STRAIGHT_SPEED = 0.3;
  }

  public static double inchesToTicks(double inches) {
    // 74 inches = 86146 ticks
    // 74 / Math.PI / 6.25 = 3.77 rotations * 2048 ticks = 7718 ticks
    // gearbox = 86146 / 7718 = 11.16
    // double gearbox = 9.4;
    // 113 inches = 134895
    // 113 / Math.PI / 6.25 = 5.755 rotations * 2048 ticks = 11786
    // gearbox = 134895 / 11786 = 11.45
    double rotations = inches / Math.PI / 6.25;
    GEARBOX = SmartDashboard.getNumber("Gearbox Ratio", GEARBOX);
    return rotations * GEARBOX * 2048;
  }

  /**
   * Inverse of inchesToTicks()
   * 
   * @param ticks
   * @return
   */
  public static double ticksToInches(double ticks) {
    GEARBOX = SmartDashboard.getNumber("Gearbox Ratio", GEARBOX);
    double gearbox = GEARBOX;

    return (ticks / gearbox / 2048) * (6.25 * Math.PI);
  }
}
