// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.Constants.DriveStraightConstants;

public class DriveStraightEncoder extends CommandBase {
  private Drivetrain drivetrain = null;
  private double distance = 0;
  private double startPos = 0;
    /** Creates a new DriveStraightEncoder. */
  public DriveStraightEncoder(Drivetrain drivetrain, double distance) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    this.distance = distance;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startPos = drivetrain.getPosition();
  }

  
  
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (distance < 0) {
      drivetrain.autonDrive(-DriveStraightConstants.DRIVE_STRAIGHT_SPEED, 0);
    } else {
      drivetrain.autonDrive(DriveStraightConstants.DRIVE_STRAIGHT_SPEED, 0);
    }
    System.out.println("execute");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.autonDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.out.println("isFinished(): drivetrain.getPosition(): " + drivetrain.getPosition() + ", startPos: " + startPos
    + ", distanceToDrive: " + distance);
    System.out.println("isFinished(): " + (drivetrain.getPosition() - startPos >= distance));

    if (distance < 0) {
      return drivetrain.getPosition() - startPos < distance;
    }
    return drivetrain.getPosition() - startPos >= distance;
  }
  }
