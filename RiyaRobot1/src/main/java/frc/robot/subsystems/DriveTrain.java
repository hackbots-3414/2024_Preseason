// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {
  private WPI_TalonFX motorFrontLeft = new WPI_TalonFX(10);
  private WPI_TalonFX motorBackLeft = new WPI_TalonFX(12);
  private WPI_TalonFX motorFrontRight = new WPI_TalonFX(13);
  private WPI_TalonFX motorBackRight = new WPI_TalonFX(14);
  private DifferentialDrive differentialDrive = new DifferentialDrive(motorFrontLeft, motorFrontRight);
  /** Creates a new DriveTrain. */
  public DriveTrain() {
    motorBackLeft.follow(motorFrontLeft);
    motorBackRight.follow(motorFrontRight);

    motorFrontRight.setInverted(true);
    motorBackRight.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    motorFrontLeft.feed();
    motorBackLeft.feed();
    motorFrontRight.feed();
    motorBackRight.feed();
  }
   // This method will tell the robot to move
  public void drive (double xSpeed, double zRotation) {
    differentialDrive.arcadeDrive(0, 0);
  }
}
