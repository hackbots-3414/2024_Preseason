// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import javax.lang.model.util.ElementScanner14;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Turn extends CommandBase {
  private Drivetrain drivetrain = null;
  private double targetAngleDegrees = 0;

  /** Creates a new Turn. */
  public Turn(Drivetrain drivetrain, double targetAngleDegrees) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    this.targetAngleDegrees = targetAngleDegrees;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetYaw();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (targetAngleDegrees < 0) {
      drivetrain.autonDrive(0, -0.15);
    } else {
      drivetrain.autonDrive(0, 0.15);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double currentAngle = drivetrain.getTurnAngle();
    return (targetAngleDegrees > 0 && targetAngleDegrees - currentAngle <= 0) ||
    (targetAngleDegrees < 0 && targetAngleDegrees - currentAngle >= 0);
  }}