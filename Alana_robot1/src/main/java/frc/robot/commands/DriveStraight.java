// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveStraight extends CommandBase {
  private Drivetrain drivetrain = null;
  private long timeToDrive = 55555;
  private long startTime = 0;

  /** Creates a new DriveStraight. */
  public DriveStraight(Drivetrain drivetrain, long timeToDrive) {
    System.out.println("Parameter timeToDrive " + timeToDrive);
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain= drivetrain;
    System.out.println("this.timeToDrive " + this.timeToDrive);
    this.timeToDrive = timeToDrive;
    System.out.println("timeToDrive " + timeToDrive);
    Thread.dumpStack();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = System.currentTimeMillis();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println("drivestraight.execute");
    drivetrain.autonDrive(0.3,0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.autonDrive(0,0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
     if(System.currentTimeMillis() - startTime < timeToDrive) {
      System.out.println("isFinished false " + System.currentTimeMillis() + "," + startTime + "," + timeToDrive);
      return false;
    } else {
      System.out.println("isFinished true " + System.currentTimeMillis() + "," + startTime + "," + timeToDrive);
      return true; 

    }
  }
}
