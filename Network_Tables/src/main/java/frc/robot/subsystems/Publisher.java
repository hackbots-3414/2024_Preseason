// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.DoubleArrayTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.GamePiece;

public class Publisher extends SubsystemBase {

  // Network Tables is how the robot sends information

  private NetworkTableInstance networkInstance = NetworkTableInstance.getDefault(); 
  private NetworkTable gpTable = networkInstance.getTable("GamePiece");
  private DoubleArrayTopic gpAngleTopic = gpTable.getDoubleArrayTopic("angle");
  private DoubleArrayTopic gpConfidenceTopic = gpTable.getDoubleArrayTopic("confidence");
  private DoubleArrayEntry gpAngleEntry = gpAngleTopic.getEntry(new double[0], PubSubOption.keepDuplicates(true));
  private DoubleArrayEntry gpConfidenceEntry = gpConfidenceTopic.getEntry(new double[0], PubSubOption.keepDuplicates(true));
  public Publisher() {
    SmartDashboard.putNumber("#gamepieces", 0);
  }
  
  @Override
  public void periodic() {

    GamePiece[] gamepieces = createData();
    double[] anglearray = new double[gamepieces.length];
    double[] confidencearray = new double[gamepieces.length];
    // Angle how much you have to turn to get the note
    // Confidence shows how confident the robot knows that it is a note
    for (int i = 0; i < gamepieces.length;i++) {
 
      anglearray[i] = gamepieces[i].getAngle();
      // Getting the angle from gamepieces then assigning to anglearray
      confidencearray[i] = gamepieces[i].getConfidence();
      // Getting the Confidence from gamepieces then assigning to confidencearray
      
    }
     long timestamp = System.currentTimeMillis();
     gpAngleEntry.set(anglearray, timestamp);
     gpConfidenceEntry.set(confidencearray,timestamp);
     // For both arrays we need to have the same TimeStamp

  }


  private GamePiece[] createData() {
    GamePiece thisGamePiece;
    GamePiece[] gpList = new GamePiece[(int) SmartDashboard.getNumber("#gamepieces", 0)];
    for (int i = 0; i < gpList.length; i++) {
      thisGamePiece = new GamePiece();
      thisGamePiece.setConfidence(100 / (i + 1));
      thisGamePiece.setAngle(30 * (i - 3));
      gpList[i] = thisGamePiece;
    }
    return gpList;
  }

}
