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

public static CommandBase driveStraight(Drivetrain drivetrain, long millisecondsToDrive) {
  return new DriveStraight(drivetrain, millisecondsToDrive);
}

public static CommandBase driveStraightByDistance(Drivetrain drivetrain, double distanceToDrive) {
  return new DriveStraightEncoder(drivetrain, distanceToDrive);
}

public static SendableChooser getAutoChooser(Drivetrain drivetrain) {
  SendableChooser chooser = new SendableChooser<>();
  chooser.setDefaultOption("5 sec no-op", new WaitCommand(5));
  chooser.addOption("Drive Straight 5 sec", driveStraight(drivetrain, 5000));
  chooser.addOption("Drive Straight 100K ticks", driveStraightByDistance(drivetrain, 100000));
  return chooser;
}

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
