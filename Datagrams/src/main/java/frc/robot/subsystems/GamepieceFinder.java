// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Gamepiece;
import frc.robot.Constants.GamepieceFinderConstants;

/**
 * Combining a few concepts to receive detected gamepiece data
 * 
 * @see https://docs.wpilib.org/en/stable/docs/software/convenience-features/scheduling-functions.html
 *      for info on how to schedule this subsystem to update its data more
 *      frequently than the main command loop
 * @see https://jenkov.com/tutorials/java-nio/index.html for a good intro to
 *      Java's NIO
 * @see https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/package-summary.html
 *      for Java 17's NIO overview
 */
public class GamepieceFinder extends SubsystemBase {
  private static final Logger LOG = LoggerFactory.getLogger(GamepieceFinder.class);

  private DatagramChannel channel = null;
  ByteBuffer receiveBuffer = ByteBuffer.allocate(GamepieceFinderConstants.DATAGRAM_BUFFER_SIZE);
  long lastUpdateTimestamp = 0;
  private Gamepiece[] gamepieces = new Gamepiece[0];

  /** Creates a new GamepieceFinder. */
  public GamepieceFinder() {
    LOG.trace("c'tor ENTER");
    int attempts = 0;
    // do not know how long it might take to
    while (attempts < GamepieceFinderConstants.CHANNEL_OPEN_RETRIES) {
      try {
        prepServer();
        break;
      } catch (IOException oops) {
        LOG.error("Attempt {} to open channel and bind to UDP_PORT {} error: {}", attempts,
            GamepieceFinderConstants.UDP_PORT, oops);
      }
      LOG.warn("Sleeping a bit before retrying...");
      Timer.delay(GamepieceFinderConstants.CHANNEL_OPEN_RETRY_DELAY);
    }
    if (attempts >= GamepieceFinderConstants.CHANNEL_OPEN_RETRIES) {
      // We're in really bad shape for gamepiece recognition - continue to allow robot
      // to start
      // but alert no automatic gamepiece detection
      LOG.error("Exceeded retries - UNABLE TO START GAMEPIECE RECOGNITION");
    }
    LOG.trace("c'tor EXIT");
  }

  /**
   * Get the server-side socket open to receive data
   * @throws IOException if something goes wrong
   */
  private void prepServer() throws IOException {
    LOG.trace("prepServer() ENTER");
    channel = DatagramChannel.open();
    // do not block - that is, wait, for data
    channel.configureBlocking(false);
    channel.bind(new InetSocketAddress(GamepieceFinderConstants.UDP_PORT));
    LOG.trace("prepServer() EXIT");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // moved logic into updateBuffer() to refresh more frequently

    // do we check here if data is too "old" and remove it?
    // scenario is if CL100 disconnects and doesn't reconnect for some time
  }

  /**
   * Call to receive a datagram and parse
   */
  public void updateBuffer() {
    LOG.trace("updateBuffer() ENTER");
    SocketAddress success = null;
    // flush any leftover data
    receiveBuffer.clear();
    try {
      success = channel.receive(receiveBuffer);
    } catch (Exception e) {
      // being overly broad here, but can't allow robot to crash
      LOG.info("Error reading datagram: {}", e);
    }
    if (success == null) {
      // null means no data read, per Javadoc
      LOG.trace("updateBuffer() - no datagrame received - EXIT");
      return;
    }
    // if we get here, we got data
    updateGamepieces();
    LOG.trace("updateBuffer() EXIT");
  }

  /**
   * Parse the datagram buffer into a Gamepiece array
   */
  private void updateGamepieces() {
    LOG.trace("updateGamepieces() ENTER");
    lastUpdateTimestamp = System.currentTimeMillis();
    // assignment in Java is atomic - no need to worry about another thread reading
    // partial or corrupted data
    gamepieces = GamepieceParser.parseRawBytes(receiveBuffer);
    LOG.debug("Updated gamepieces: {}, length: {}", gamepieces, gamepieces.length);
    LOG.trace("updateGamepieces() EXIT");
  }

  public Gamepiece[] getGamepieces() {
    return gamepieces;
  }
}
