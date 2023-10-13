// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;



public class DriveStraightCommand extends CommandBase {
  private Drivetrain drivetrain = null;
  private long timetodrive = 0;
private long starttime = 0;
  /** Creates a new DriveStraightCommand. */
  public DriveStraightCommand(Drivetrain drivetrain, long timetodrive) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    this.timetodrive = timetodrive;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    starttime = System.currentTimeMillis();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.drive(0.3, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (System.currentTimeMillis() - starttime > timetodrive) {
      return false;
    } else {
        return true;
    }
  
  }
}
