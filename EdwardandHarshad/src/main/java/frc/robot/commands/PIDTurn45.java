// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class PIDTurn45 extends PIDCommand {
  private Drivetrain drivetrain = null;

  /** Creates a new PIDTurn. */
  public PIDTurn45(Drivetrain drivetrain, double targetAngleDegrees) {
    super(
        // The controller that the command will use
        new PIDController(0.009, 0.0007, 0.0003),
        // This should return the measurement
        drivetrain::getTurnAngle,
        // This should return the setpoint (can also be a constant)
        () -> targetAngleDegrees,
        // This uses the output
        output -> {
          // Use the output here
          // We may need to check output and clamp it to a low speed. See
          // https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/pidcontroller.html
        output = MathUtil.clamp(output, -0.2, 0.2);
          drivetrain.autonDrive(0, output);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    // Configure additional PID options by calling `getController` here.
    // Gyro will return between -180 and 180. (Don't use PID turn for 180 degree turns, the math will be way messed up)
    getController().enableContinuousInput(-180, 180);
    //We'll never be at exactly our measurement, so, how close is close enough?
    getController().setTolerance(2);
    SmartDashboard.putData("PIDTurn Controller ", getController());
  }

  /*
   * Want to be sure we zero our gyro, so overriding initialize()
   * Right-click, Source Action, Override / Implement methods.
   * THe @Override marder tells the compiler we intend to override a method on our parent class. 
   * If the parent changes the signature, the compiler will flag the error in this class.
   */
  @Override
  public void initialize() {
    super.initialize();
    drivetrain.resetYaw();
    SmartDashboard.putString("command", getClass().getName());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
    // return getController().atSetpoint();
  }
}
