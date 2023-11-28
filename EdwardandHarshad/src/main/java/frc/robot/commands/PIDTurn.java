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
public class PIDTurn extends PIDCommand {
  private Drivetrain drivetrain = null;

  /** Creates a new PIDTurn. */
  public PIDTurn(Drivetrain drivetrain, double targetAngleDegrees) {
    super(
        // The controller that the command will use
        new PIDController(0.00655, 0.000065, 0),
        //i=0.0000655: overshot by 1.78
        //i=0.00006: undershot and output was too small for it to move, did not complete turn
        //i=0.000065: undershot by between 12 and 4
        //i=0.0000652: undershot by between 4 and 0.5
        // This should return the measurement
        drivetrain::getTurnAngle,
        // This should return the setpoint (can also be a constant)
        () -> targetAngleDegrees,
        // This uses the output
        output -> {
          // Use the output here
          // we may need to check output and clamp it to a low speed
          // see
         // https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/pidcontroller.html
         output = MathUtil.clamp(output, -0.2, 0.2);
          drivetrain.autonDrive(0, output);
          SmartDashboard.putNumber("Output", output);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    this.drivetrain = drivetrain;
    // Configure additional PID options by calling `getController` here.
    // Gyro will return between -180 and +180
    getController().enableContinuousInput(-180, 180);
    // We'll never be at exactly our measurement, so, how close is close enough?
    getController().setTolerance(2);
    SmartDashboard.putData("PIDTurn Controller", getController());
  }

  /**
   * Want to be sure we zero our gyro, so, overriding initialize().
   * Right-click, Source Action->Override / Implement methods
   * The @Override marker tells the compiler we intend to override
   * a method on our parent class. If the parent changes the signature,
   * the compiler will flag the error in this class.
   */
  @Override
  public void initialize() {
    // make sure the parent gets to do their tasks. If we don't
    // call super.initialize(), that logic won't run, and other
    // parts of this class will fail.
    super.initialize();
    drivetrain.resetYaw();
    getController().reset();
    SmartDashboard.putString("command", getClass().getName());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    SmartDashboard.putBoolean("atSetPoint", getController().atSetpoint());
    SmartDashboard.putNumber("SetPoint", getController().getSetpoint());
    return getController().atSetpoint();
  }
}
