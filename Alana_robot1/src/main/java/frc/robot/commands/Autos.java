// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static CommandBase exampleAuto(ExampleSubsystem subsystem) {
    return Commands.sequence(subsystem.exampleMethodCommand(), new ExampleCommand(subsystem));
  }

  public static CommandBase driveStraight(Drivetrain drivetrain) {
    return new DriveStraight(drivetrain,5000);
  }

  public static SendableChooser getAutonChooser(Drivetrain drivetrain) {
    SendableChooser chooser = new SendableChooser<>();
    chooser.addOption("5 sec no op", new WaitCommand(5));
    chooser.addOption("Drive Straomg 5 sec", driveStraight(drivetrain));
    return chooser;
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
