// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends SubsystemBase {
  private WPI_TalonSRX motorFrontLeft = new WPI_TalonSRX(3);
  private WPI_TalonSRX motorBackLeft = new WPI_TalonSRX(4);
  private WPI_TalonSRX motorFrontRight = new WPI_TalonSRX(1);
  private WPI_TalonSRX motorBackRight = new WPI_TalonSRX(2);
  private DifferentialDrive differentialDrive = new DifferentialDrive(motorFrontLeft, motorFrontRight);

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    motorBackLeft.follow(motorFrontLeft);
    motorBackRight.follow(motorFrontRight);

    motorFrontRight.setInverted(true);
    motorBackRight.setInverted(true);

    motorFrontLeft.setSelectedSensorPosition(0, 0, 20);
    motorBackLeft.setSelectedSensorPosition(0, 0, 20);
    motorFrontRight.setSelectedSensorPosition(0, 0, 20);
    motorBackRight.setSelectedSensorPosition(0, 0, 20);
  }

  @Override 
  public void periodic() {
    // This method will be called once per scheduler run
    motorFrontLeft.feed();
    motorFrontRight.feed();
    motorBackLeft.feed();
    motorBackRight.feed();
    SmartDashboard.putNumber("Encoder Average: " , getPosition());
    SmartDashboard.putNumber("Left Front Position: " , motorFrontLeft.getSelectedSensorPosition());

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
