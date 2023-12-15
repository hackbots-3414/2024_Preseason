// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.RamseteConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.ExampleSubsystem;

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static CommandBase exampleAuto(ExampleSubsystem subsystem) {
    return Commands.sequence(subsystem.exampleMethodCommand(), new ExampleCommand(subsystem));
  }

  public static CommandBase driveStraightByTime(Drivetrain drivetrain, long millisecondsToDrive) {
    return new DriveStraight(drivetrain, millisecondsToDrive);
  }

  public static CommandBase driveStraightByDistance(Drivetrain drivetrain, double distanceToDrive) {
    return new DriveStraightEncoder(drivetrain, distanceToDrive);
  }

  public static CommandBase turn(Drivetrain drivetrain, double targetAngleDegrees) {
    return new Turn(drivetrain, targetAngleDegrees);
  }

  public static CommandBase createPIDTurn90(Drivetrain drivetrain, double targetAngleDegrees) {
    return new PIDTurn90(drivetrain, targetAngleDegrees);
  }

  public static CommandBase createPIDTurn45(Drivetrain drivetrain, double targetAngleDegrees) {
    return new PIDTurn45(drivetrain, targetAngleDegrees);
  }

  public static CommandBase createOptimizedTrajectory(Drivetrain drivetrain) {
      // Create a voltage constraint to ensure we don't accelerate too fast
      var autoVoltageConstraint =
      new DifferentialDriveVoltageConstraint(
          new SimpleMotorFeedforward(
              RamseteConstants.ksVolts,
              RamseteConstants.kvVoltSecondsPerMeter,
              RamseteConstants.kaVoltSecondsSquaredPerMeter),
          RamseteConstants.kDriveKinematics,
          10);

  // Create config for trajectory
  TrajectoryConfig config =
      new TrajectoryConfig(
              RamseteConstants.kMaxSpeedMetersPerSecond,
              RamseteConstants.kMaxAccelerationMetersPerSecondSquared)
          // Add kinematics to ensure max speed is actually obeyed
          .setKinematics(RamseteConstants.kDriveKinematics)
          // Apply the voltage constraint
          .addConstraint(autoVoltageConstraint);

  // An example trajectory to follow.  All units in meters.
  Trajectory exampleTrajectory =
      TrajectoryGenerator.generateTrajectory(
          // Start at the origin facing the +X direction
          new Pose2d(0, 0, new Rotation2d(0)),
          // Pass through these two interior waypoints, making an 's' curve path
          List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
          // End 3 meters straight ahead of where we started, facing forward
          new Pose2d(3, 0, new Rotation2d(0)),
          // Pass config
          config);

  RamseteCommand ramseteCommand =
      new RamseteCommand(
          exampleTrajectory,
          drivetrain::getPose,
          new RamseteController(RamseteConstants.kRamseteB, RamseteConstants.kRamseteZeta),
          new SimpleMotorFeedforward(
              RamseteConstants.ksVolts,
              RamseteConstants.kvVoltSecondsPerMeter,
              RamseteConstants.kaVoltSecondsSquaredPerMeter),
          RamseteConstants.kDriveKinematics,
          drivetrain::getWheelSpeeds,
          new PIDController(RamseteConstants.kPDriveVel, 0, 0),
          new PIDController(RamseteConstants.kPDriveVel, 0, 0),
          // RamseteCommand passes volts to the callback
          drivetrain::tankDriveVolts,
          drivetrain);

  // Reset odometry to the starting pose of the trajectory.
  drivetrain.resetOdometry(exampleTrajectory.getInitialPose());

  // Run path following command, then stop at the end.
  return ramseteCommand.andThen(() -> drivetrain.tankDriveVolts(0, 0));
}

  public static CommandBase createHealthToPhysicsAndBackAgain(Drivetrain drivetrain) {
    return new DriveStraightEncoder(drivetrain, 176).andThen(
        new PIDTurn45(drivetrain, -45),
        new DriveStraightEncoder(drivetrain, 156),
        new PIDTurn45(drivetrain, -45),
        new DriveStraightEncoder(drivetrain, 178),
        new PIDTurn90(drivetrain, -90),
        new DriveStraightEncoder(drivetrain, 25),
        new PIDTurn90(drivetrain, -90),
        new DriveStraightEncoder(drivetrain, 168),
        new PIDTurn45(drivetrain, 45),
        new DriveStraightEncoder(drivetrain, 125),
        new PIDTurn45(drivetrain, 45),
        new DriveStraightEncoder(drivetrain, 140));
  }

  public static SendableChooser<Command> buildAutonPicker(Drivetrain drivetrain) {
    SendableChooser<Command> autonList = new SendableChooser<>();
    autonList.setDefaultOption("Do Nothing", new WaitCommand(5.0));
    autonList.addOption("Drive Straight 5 sec.", driveStraightByTime(drivetrain, 5000));
    autonList.addOption("Drive Straight 100K ticks", driveStraightByDistance(drivetrain, 113));
    autonList.addOption("Drive Straight -100K ticks", driveStraightByDistance(drivetrain, -113));
    Command outAndBack = driveStraightByDistance(drivetrain, 74).andThen(driveStraightByDistance(drivetrain, -74));
    autonList.addOption("Out and Back 50k", outAndBack);
    autonList.addOption("Turn 90", turn(drivetrain, 90));
    autonList.addOption("Turn -90", turn(drivetrain, -90));
    autonList.addOption("PID Turn 90", createPIDTurn90(drivetrain, 90));
    autonList.addOption("PID Turn 45", createPIDTurn45(drivetrain, 45));
    autonList.addOption("PID Turn -90", createPIDTurn90(drivetrain, -90));
    autonList.addOption("PID Turn -45", createPIDTurn45(drivetrain, -45));

    autonList.addOption("There and Back Again", createHealthToPhysicsAndBackAgain(drivetrain));
    return autonList;
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
