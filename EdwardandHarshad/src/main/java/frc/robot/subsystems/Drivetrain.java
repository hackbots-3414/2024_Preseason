// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import javax.print.attribute.standard.RequestingUserName;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
  private WPI_TalonFX motorFrontLeft = new WPI_TalonFX(10);
  private WPI_TalonFX motorBackLeft = new WPI_TalonFX(11);
  private WPI_TalonFX motorFrontRight = new WPI_TalonFX(13);
  private WPI_TalonFX motorBackRight = new WPI_TalonFX(14);
  private DifferentialDrive differentialDrive = new DifferentialDrive(motorFrontLeft, motorFrontRight);

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    motorBackLeft.follow(motorFrontLeft);
    motorBackRight.follow(motorFrontRight);

    motorFrontRight.setInverted(true);
    motorBackRight.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    motorFrontLeft.feed();
    motorFrontRight.feed();
    motorBackLeft.feed();
    motorBackRight.feed();
  }

  public void drive(double xSpeed, double zRotation) {
    differentialDrive.arcadeDrive(xSpeed, zRotation);
  }

  public void autonDrive(double xSpeed, double zRotation) {
    differentialDrive.arcadeDrive(xSpeed, zRotation, false);
  }

  public double getPosition() {
    double average = (motorFrontLeft.getSelectedSensorPosition() + motorFrontRight.getSelectedSensorPosition()
        + motorBackLeft.getSelectedSensorPosition() + motorBackRight.getSelectedSensorPosition()) / 4;
        return average;
  }
}
