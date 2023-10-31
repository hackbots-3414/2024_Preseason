// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveStraightConstants;
import frc.robot.subsystems.Drivetrain;

public class DriveStraightEncoder extends CommandBase {
  private Drivetrain drivetrain = null;
  private double distanceToDrive = 0.0;
  private double startPos = 0.0;

  /** Creates a new DriveStraightEncoder. */
  public DriveStraightEncoder(Drivetrain drivetrain, double distanceToDrive) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    this.distanceToDrive = distanceToDrive;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startPos = drivetrain.getPosition();
    System.out.println("initialize: startPos = " + startPos);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (distanceToDrive < 0) {
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
        + ", distanceToDrive: " + distanceToDrive);
    System.out.println("isFinished(): " + (drivetrain.getPosition() - startPos >= distanceToDrive));

    if (distanceToDrive < 0) {
      return drivetrain.getPosition() - startPos < distanceToDrive;
    }
    return drivetrain.getPosition() - startPos >= distanceToDrive;
  }
}
