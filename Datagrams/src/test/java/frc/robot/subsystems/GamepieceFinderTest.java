// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.junit.jupiter.api.Test;

import frc.robot.Constants.GamepieceFinderConstants;
import frc.robot.Robot;



/** 
 * Add your docs here. 
 * Borrowed heavily from here: https://jenkov.com/tutorials/java-nio/datagram-channel.html
 * 
 */
public class GamepieceFinderTest {

    @Test
    public void testUpdate() throws Exception {
        GamepieceFinder subsystem = new GamepieceFinder();

        DatagramChannel channel = DatagramChannel.open();
        ByteBuffer sendBuffer = ByteBuffer.allocate(GamepieceFinderConstants.DATAGRAM_BUFFER_SIZE);
        
        for (int i = 0; i < 10; i++) { 
            sendData(channel, sendBuffer);
            subsystem.updateBuffer();
            assertTrue(subsystem.getGamepieces().length == 3, "Gamepiece array has 3 elements");
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
}


