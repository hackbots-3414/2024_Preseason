// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class PIDTurn extends PIDCommand {
  private Drivetrain drivetrain = null;

  /** Creates a new PIDTurn. */
  public PIDTurn(Drivetrain drivetrain, double targetAngleDegrees) {
    super(
        // The controller that the command will use
        new PIDController(0.3, 0, 0),
        // This should return the measurement
        drivetrain:: getTurnAngle,
        // This should return the setpoint (can also be a constant)
        () -> targetAngleDegrees,
        // This uses the output
        output -> {
          // Use the output here
          // we may need to check output and clamp it to a low speed
          //see
          //
          drivetrain.autonDrive(0, output);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    // Configure additional PID options by calling `getController` here.
    //Gryo will return between -180 and +180
    getController().enableContinuousInput(-180, 180);
    //We'll never be at exactly our measurement
    getController().setTolerance(2);
    SmartDashboard.putData("PIDTurn Controller", getController());
    
  }
@Override
public void initialize() {
  super.initialize();
  drivetrain.resetYaw();
  getController().reset();
}
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
