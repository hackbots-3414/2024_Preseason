// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class drivetrain extends SubsystemBase {
  private WPI_TalonFX motorFrontLeft = new WPI_TalonFX(10);
  private WPI_TalonFX motorBackLeft = new WPI_TalonFX(11);
  private WPI_TalonFX motorFrontRight = new WPI_TalonFX(13);
  private WPI_TalonFX motorBackRight = new WPI_TalonFX(14);
  private DifferentialDrive differentialDrive = new DifferentialDrive(motorFrontLeft, motorFrontRight);
    /** Creates a new Drivetrain. */
    public drivetrain() {
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
    motorFrontRight.feed();
  }

  public void drive(double xspeed, double zRotation) {
    differentialDrive.arcadeDrive(xspeed, zRotation);
  }
  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
