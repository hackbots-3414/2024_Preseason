// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {
  private WPI_TalonFX motorFrontLeft = new WPI_TalonFX(10);
  private WPI_TalonFX motorBackLeft = new WPI_TalonFX(11);
  private WPI_TalonFX motorFrontRight = new WPI_TalonFX(13);
  private WPI_TalonFX motorBackRight = new WPI_TalonFX(14);
  private DifferentialDrive differentialDrive = new DifferentialDrive(motorFrontLeft, motorFrontRight);
  public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(
      Constants.RamseteConstants.kTrackwidthInch);
  private AHRS navx = new AHRS();
  private final DifferentialDriveOdometry m_odometry;

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    motorBackLeft.follow(motorFrontLeft);
    motorBackRight.follow(motorFrontRight);

    motorFrontRight.setInverted(true);
    motorBackRight.setInverted(true);

    resetEncoders();
    m_odometry = new DifferentialDriveOdometry(
        navx.getRotation2d(), motorFrontLeft.getSelectedSensorPosition(), motorFrontRight.getSelectedSensorPosition());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    motorFrontLeft.feed();
    motorFrontRight.feed();
    motorBackLeft.feed();
    motorBackRight.feed();
    SmartDashboard.putNumber("Encoder Average: ", getPosition());
    SmartDashboard.putNumber("Left Front Position: ", motorFrontLeft.getSelectedSensorPosition());
    SmartDashboard.putNumber("Yaw", getTurnAngle());
    m_odometry.update(
        navx.getRotation2d(), motorFrontLeft.getSelectedSensorPosition(), motorFrontRight.getSelectedSensorPosition());
  }
 /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }
   /**
   * Returns the current wheel speeds of the robot.
   *
   * @return The current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(motorFrontLeft.getSelectedSensorVelocity(), motorFrontRight.getSelectedSensorVelocity());
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(
      navx.getRotation2d(), motorFrontLeft.getSelectedSensorPosition(), motorFrontRight.getSelectedSensorPosition(), pose);
  }
  public void drive(double xSpeed, double zRotation) {
    differentialDrive.arcadeDrive(xSpeed, zRotation);
  }

  public void autonDrive(double xSpeed, double zRotation) {
    differentialDrive.arcadeDrive(xSpeed, zRotation, false);
  }

  public double getPosition() {
    return (motorFrontLeft.getSelectedSensorPosition() + motorBackLeft.getSelectedSensorPosition()
        + motorFrontRight.getSelectedSensorPosition() + motorBackRight.getSelectedSensorPosition()) / 4.0;
  }

  public double getTurnAngle() {
    return navx.getYaw();
  }

  public void resetYaw() {
    navx.reset();
  }
  public void resetEncoders() {
    motorFrontLeft.setSelectedSensorPosition(0, 0, 20);
    motorBackLeft.setSelectedSensorPosition(0, 0, 20);
    motorFrontRight.setSelectedSensorPosition(0, 0, 20);
    motorBackRight.setSelectedSensorPosition(0, 0, 20);
  }
}
  }
}
