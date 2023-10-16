// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem;

public final class Autos {
  public static CommandBase exampleAuto(ExampleSubsystem subsystem) {
    return Commands.sequence(subsystem.exampleMethodCommand(), new ExampleCommand(subsystem));
  }
  public static CommandBase driveStraight(DriveTrain driveTrain) {
    return new DriveStraight(driveTrain, 5000);
  }
  public static SendableChooser getAutonChooser(DriveTrain driveTrain) {
    SendableChooser chooser = new SendableChooser<>();
    chooser.setDefaultOption("S sec no-op", new WaitCommand(5));
    chooser.addOption("Drive Straight 5 sec", driveStraight(driveTrain));
    return chooser;
  }
  private Autos() {
    throw new UnsupportedOperationException("This is a utility class");
  }
}
