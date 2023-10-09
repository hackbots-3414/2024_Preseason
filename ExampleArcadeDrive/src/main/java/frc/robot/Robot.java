// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  private final WPI_TalonFX front_leftMotor = new WPI_TalonFX(10);
  private final WPI_TalonFX front_rightMotor = new WPI_TalonFX(13);
  private final WPI_TalonFX back_leftMotor = new WPI_TalonFX(11);
  private final WPI_TalonFX back_rightMotor = new WPI_TalonFX(14);

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(front_leftMotor, front_rightMotor);
  private final Joystick m_stick = new Joystick(0);


  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    back_leftMotor.follow(front_leftMotor);
    back_rightMotor.follow(front_rightMotor);
    back_rightMotor.setInverted(true);
    front_rightMotor.setInverted(true);

  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(-m_stick.getY(), -m_stick.getX());
  }
}
