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

  public Gamepiece[] getGamepieces() {
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
    parseBuffer();
  }

  private void parseBuffer() {
    //[-169.9, 169.9,0]|[100,99.1,70.0]|"This is a test."
    byteReceiver.rewind();
    byte currentByte = byteReceiver.get();
    if(currentByte != '[') {
      LOG.trace("Bad message");
      return;
    }
  }
}
