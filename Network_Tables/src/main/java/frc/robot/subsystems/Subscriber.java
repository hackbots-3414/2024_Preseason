// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.DoubleArrayTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.GamePiece;

public class Subscriber extends SubsystemBase {
  private NetworkTableInstance networkInstance = NetworkTableInstance.getDefault(); 
  private NetworkTable gpTable = networkInstance.getTable("GamePiece");
  private DoubleArrayTopic gpAngleTopic = gpTable.getDoubleArrayTopic("angle");
  private DoubleArrayTopic gpConfidenceTopic = gpTable.getDoubleArrayTopic("confidence");
  private DoubleArrayEntry gpAngleEntry = gpAngleTopic.getEntry(new double[0], PubSubOption.keepDuplicates(true));
  private DoubleArrayEntry gpConfidenceEntry = gpConfidenceTopic.getEntry(new double[0], PubSubOption.keepDuplicates(true)); 


  @Override
  public void periodic() {
    double[] anglearray = gpAngleEntry.get();
    double[] confidencearray = gpConfidenceEntry.get();
    // the values from the entries have been put into the array
     GamePiece[] gamepieces = new GamePiece[anglearray.length];
    for (int i = 0; i < gamepieces.length;i++) {
      gamepieces[i] = new GamePiece();
      gamepieces[i].setAngle(anglearray[i]);
      gamepieces[i].setConfidence(confidencearray[i]);
    }  

  }
}
