// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.GamePiece;
import frc.robot.subsystems.Subscriber;

public class AlignAndDrive extends Command {
  private Subscriber m_Subscriber = null;

  /** Creates a new AlignAndDrive. */
  public AlignAndDrive(Subscriber sub) {
    m_Subscriber = sub;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(sub);

    SmartDashboard.putString("Direction to Drive", "Initialized");

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    GamePiece[] gp = m_Subscriber.getGamePiecesInView();
    if (gp.length != 0) {
      if (gp[0].getAngle() < 0) {
        SmartDashboard.putString("Direction to Drive", "Left");
      } else if (gp[0].getAngle() > 0) {
        SmartDashboard.putString("Direction to Drive", "Right");
      } else {
        SmartDashboard.putString("Direction to Drive", "Straight");
      }        
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putString("Direction to Drive", "Stop");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
