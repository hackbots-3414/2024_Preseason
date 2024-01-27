// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Gamepiece;
import frc.robot.Constants.NoteFinderConstants;

/**
 * Notefinder receives datagram packets for commands to use
 * [array of angles]|[array of confidences]|"quoted status message"
 * 512 bytes max payload
 * 1 decimal place
 * 
 * [-169.9, 169.9,0]|[100,99.1,70.0]|"This is a test.
 * `~!@#$%^&*()_+|\}]{[]};:',.<>/?"
 */
public class NoteFinder extends SubsystemBase {
  private static final Logger LOG = LoggerFactory.getLogger(NoteFinder.class);
  private DatagramChannel noteChannel = null;
  private ByteBuffer byteReceiver = ByteBuffer.allocate(NoteFinderConstants.BUFFER_SIZE);
  private ArrayList<Gamepiece> gamepieces = new ArrayList<>();
  private StringBuffer status = new StringBuffer();
  private long lastUpdateTime = 0;

  /** Creates a new NoteFinder. */
  public NoteFinder() {
    try {
      noteChannel = DatagramChannel.open();
      noteChannel.configureBlocking(false);
      noteChannel.socket().bind(new InetSocketAddress(NoteFinderConstants.DATAGRAM_PORT));
    } catch (IOException ioe) {
      LOG.error("Failure to open note channel", ioe);
    }
  }

  public synchronized Gamepiece[] getGamepieces() {
    return gamepieces.toArray(new Gamepiece[gamepieces.size()]);
  }

  public String getStatus() {
    return status.toString();
  }

  public long getLastUpdateTime() {
    return lastUpdateTime;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // dataReceiver();
  }

  public void dataReceiver() {
    try {
      byteReceiver.clear();
      SocketAddress senderAddress = noteChannel.receive(byteReceiver);
      if (senderAddress == null) {
        return;
      }
      LOG.trace("receive data: {} Sender: {}", byteReceiver, senderAddress);
    } catch (Exception ioe) {
      LOG.error("Failure to receive data", ioe);
    }
    try {
      parseBuffer2();
      LOG.trace("Updated Game Pieces: {}", gamepieces);
    } catch (Exception e) {
      LOG.error("Bad MESSAGE", e);
    }
  }

  private synchronized void parseBuffer2() {
    // [-169.9,169.9,0]|[100,99.1,70.0]|"This is a test."
    byteReceiver.rewind();
    gamepieces.clear();
    StringBuilder stringBuilder = new StringBuilder(NoteFinderConstants.BUFFER_SIZE);
    char currentByte = 0;
    int commaCount = 0;
    // Copying message into a bytebuilder
    for (int i = 0; i < byteReceiver.limit(); i++) {
      currentByte = (char) byteReceiver.get();
      if (currentByte == ',') {
        commaCount++;
      }
      stringBuilder.append(currentByte);
    }
    String firstString = null;
    String secondString = null;
    String thirdString = null;
    firstString = stringBuilder.substring(1, stringBuilder.indexOf("]"));
    secondString = stringBuilder.substring(stringBuilder.indexOf("|") + 2, stringBuilder.lastIndexOf("]"));
    thirdString = stringBuilder.substring(stringBuilder.indexOf("\"") + 1, stringBuilder.lastIndexOf("\""));
    LOG.trace("First String: {} Second String: {} Third String: {}", firstString, secondString, thirdString);
    status.setLength(0);
    status.append(thirdString);
    if (firstString.length() == 0) {
      return;
    }
    commaCount = commaCount/2;
    double[] angleArray = parseDoubleArray(firstString, commaCount);
    double[] confidenceArray = parseDoubleArray(secondString, commaCount);
    Gamepiece gamepiece = null;
    for (int i = 0; i < angleArray.length; i++) {
      gamepiece = new Gamepiece();
      gamepiece.setAngle(angleArray[i]);
      gamepiece.setConfidence(confidenceArray[i]);
      gamepieces.add(gamepiece);
    }
  }

  private double[] parseDoubleArray(String stringIn, int commaCount) {
    double[] returnArray = new double[commaCount + 1];
    String[] doubleString = stringIn.split(",");
    for (int i = 0; i < returnArray.length; i++) {
      returnArray[i] = Double.valueOf(doubleString[i]);
    }
    return returnArray;
  }

 private double[] parseDoubleArrayOnce (String stringIn) {
  return null;
 }

  private synchronized void parseBuffer() {
    // [-169.9,169.9,0]|[100,99.1,70.0]|"This is a test."
    byteReceiver.rewind();
    StringBuilder stringBuilder = new StringBuilder(NoteFinderConstants.BUFFER_SIZE);
    int leftBracketIndex = 0;
    int rightBracketIndex = 0;
    int commaCount = 0;
    char currentByte = 0;
    int startVar = 0;
    int endVar = 0;
    // Copying message into a bytebuilder
    for (int i = 0; i < byteReceiver.limit(); i++) {
      currentByte = (char) byteReceiver.get();
      stringBuilder.append(currentByte);
      switch (currentByte) {
        case '[':
          leftBracketIndex = i;
          break;
        case ',':
          commaCount++;
          break;
        case ']':
          rightBracketIndex = i;
          i = byteReceiver.limit();
          break;
      }
    }
    Gamepiece note = null;
    for (int i = 0; i <= commaCount; i++) {
      note = new Gamepiece();
      if (i == 0) {
        startVar = leftBracketIndex + 1;
        endVar = stringBuilder.indexOf(",");
        if (endVar < startVar) {
          endVar = rightBracketIndex;
        }
      } else if (i == commaCount) {
        startVar = endVar + 1;
        endVar = rightBracketIndex;
      } else {
        startVar = endVar + 1;
        endVar = stringBuilder.indexOf(",", startVar);
      }
      if (startVar < endVar) {
        note.setAngle(Double.valueOf(stringBuilder.substring(startVar, endVar)));
        gamepieces.add(note);
      }

    }
  }
}