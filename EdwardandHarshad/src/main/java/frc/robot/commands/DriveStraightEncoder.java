// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveStraightEncoder extends CommandBase {
  private Drivetrain drivetrain = null;
  private double distancetoDrive = 0.0;
  private double startPos = 0.0;

  /** Creates a new DriveStraightEncoder. */
  public DriveStraightEncoder(Drivetrain drivetrain, double distancetoDrive) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    this.distancetoDrive = distancetoDrive;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startPos = drivetrain.getPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.autonDrive(0.3,0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.autonDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return drivetrain.getPosition() - startPos >= distancetoDrive;
  }
}
