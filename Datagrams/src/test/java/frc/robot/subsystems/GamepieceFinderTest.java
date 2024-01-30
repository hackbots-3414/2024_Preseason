// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.junit.jupiter.api.Test;

import frc.robot.Gamepiece;
import frc.robot.Constants.GamepieceFinderConstants;



/** 
 * Add your docs here. 
 * Borrowed heavily from here: https://jenkov.com/tutorials/java-nio/datagram-channel.html
 * 
 */
public class GamepieceFinderTest {

    private GamepieceFinder subsystem = null;
    private DatagramChannel channel = null;
    private ByteBuffer sendBuffer = ByteBuffer.allocate(GamepieceFinderConstants.DATAGRAM_BUFFER_SIZE);


    public GamepieceFinderTest() throws IOException {
        subsystem = new GamepieceFinder();
        channel = DatagramChannel.open();
        sendBuffer = ByteBuffer.allocate(GamepieceFinderConstants.DATAGRAM_BUFFER_SIZE);

    }

    @Test
    public void testUpdate() throws Exception {
        double[] ref = {0.0, 100.0, 5, 95, -10, 95};
        for (int i = 0; i < 10; i++) { 
            sendData(channel, sendBuffer);
            subsystem.updateBuffer();
            assertTrue(subsystem.getGamepieces().length == 3, "Gamepiece array has 3 elements");
            checkPieces(ref);
            assertTrue(subsystem.getLastUpdateTimestamp() >= System.currentTimeMillis() - 10, "last update stamp is valid");
            //Thread.sleep(1000);
        }
        channel.close();
    }

    private static void sendData(DatagramChannel channel, ByteBuffer sendBuffer)  throws Exception {
        sendBuffer.clear();
        sendBuffer.put("|0,100|5,95|-10, 95|".getBytes());
        sendBuffer.flip();
        int sent = channel.send(sendBuffer, new InetSocketAddress("127.0.0.1", GamepieceFinderConstants.UDP_PORT));
        System.out.println(sent);
    }

    private void checkPieces(double[] values) {
        Gamepiece[] pieces = subsystem.getGamepieces();
        for (int i = 0; i < pieces.length; i ++) {
            assertEquals(values[i * 2], pieces[i].getAngle());
            assertEquals(values[i * 2 + 1], pieces[i].getConfidence());
        }
    }
}


