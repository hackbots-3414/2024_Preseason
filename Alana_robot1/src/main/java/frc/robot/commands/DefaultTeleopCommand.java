// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Drivetrain;
import frc.robot.subsystems.bot_training;

public class DefaultTeleopCommand extends CommandBase {
  private CommandXboxController xboxcontroller = null;
  private Drivetrain drivetrain = null;

  /** Creates a new DefaultTeleopCommand. */
  public DefaultTeleopCommand(CommandXboxController xboxController, Drivetrain drivetrain2) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.xboxcontroller = xboxController;
    this.drivetrain = drivetrain2;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.drive(xboxcontroller.getLeftY(), xboxcontroller.getRightX());
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
