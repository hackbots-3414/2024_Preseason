// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  private final WPI_TalonFX m_leftMotorFront = new WPI_TalonFX(10); 
  private final WPI_TalonFX m_rightMotorFront = new WPI_TalonFX(13);
  private final WPI_TalonFX m_leftMotorBack = new WPI_TalonFX(11); 
  private final WPI_TalonFX m_rightMotorBack = new WPI_TalonFX(14);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotorFront, m_rightMotorFront);
  private final XboxController m_stick = new XboxController(0);

  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_leftMotorBack.follow(m_leftMotorFront);
    m_rightMotorBack.follow(m_rightMotorFront);
    m_rightMotorFront.setInverted(true);
    m_rightMotorBack.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(-m_stick.getLeftY(), -m_stick.getRightX());
  }
}
