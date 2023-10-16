// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivetrain;

public class DefaultTeleop extends CommandBase {
  /** Creates a new defaultTeleop. */
  private CommandXboxController xboxController;
  private Drivetrain driveTrain;
  public DefaultTeleop(CommandXboxController m_driverController, Drivetrain driveTrain) {
    addRequirements(driveTrain);
    this.xboxController = m_driverController;
    this.driveTrain = driveTrain;


    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveTrain.drive(xboxController.getRightY(), xboxController.getLeftX());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
