// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static CommandBase exampleAuto(ExampleSubsystem subsystem) {
    return Commands.sequence(subsystem.exampleMethodCommand(), new ExampleCommand(subsystem));
  }

  public static CommandBase driveStraightByTime(Drivetrain drivetrain, long millisecondsToDrive) {
    return new DriveStraight(drivetrain, millisecondsToDrive);
  }

  public static CommandBase driveStraightByDistance(Drivetrain drivetrain, long distanceToDrive) {
    return new DriveStraightEncoder(drivetrain, distanceToDrive);
  }
  public static CommandBase driveStraightByInches(Drivetrain drivetrain, long ticksToInches) {
    return new DriveStraightEncoder(drivetrain, ticksToInches);
  }
  public static SendableChooser<Command> buildAutonPicker(Drivetrain drivetrain) {
    SendableChooser<Command> autonList = new SendableChooser<>();
    autonList.setDefaultOption("Do Nothing", new WaitCommand(5.0));
    autonList.addOption("Drive Straight 5 sec.", driveStraightByTime(drivetrain, 5000));
    autonList.addOption("Drive Straight 100K ticks", driveStraightByDistance(drivetrain, 100000));
    autonList.addOption("Drive Straight -100K ticks", driveStraightByDistance(drivetrain, -100000));
    Command outAndBack = driveStraightByDistance(drivetrain, 74).andThen(driveStraightByDistance(drivetrain, -74));
    autonList.addOption("Out and Back 50k", outAndBack);
    Command DriveStraight100 = driveStraightByInches(drivetrain, 100);
    autonList.addOption("Drive Straight 100 Inches", DriveStraight100);
    
    return autonList;
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
