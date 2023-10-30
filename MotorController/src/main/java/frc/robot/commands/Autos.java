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
  public static CommandBase driveStraightByTime(DriveTrain driveTrain, long millisecondsToDrive) {
    return new DriveStraight(driveTrain, millisecondsToDrive);
  }
  public static CommandBase driveStraightByDistance(DriveTrain driveTrain, long distanceToDrive) {
    return new DriveStraight(driveTrain, distanceToDrive);
  }
  public static SendableChooser buildAutonPicker(DriveTrain driveTrain) {
    SendableChooser autonList = new SendableChooser<>();
    autonList.setDefaultOption("Do Nothing", new WaitCommand(5));
    autonList.addOption("Drive Straight 5 sec", driveStraightByTime(driveTrain, 5000));
    autonList.addOption("Drive Straight 100K ticks", driveStraightByDistance(driveTrain, 100000));
    autonList.addOption("Drive Straight -100K ticks", driveStraightByDistance(driveTrain, -100000));
    return autonList;
  }
  private Autos() {
    throw new UnsupportedOperationException("This is a utility class");
  }
}
