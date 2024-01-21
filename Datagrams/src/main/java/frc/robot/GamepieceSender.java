// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import frc.robot.Constants.GamepieceFinderConstants;

/** 
 * Add your docs here. 
 * Borrowed heavily from here: https://jenkov.com/tutorials/java-nio/datagram-channel.html
 * 
 */
public class GamepieceSender {
    public static void main(String[] args) throws Exception {
        DatagramChannel channel = DatagramChannel.open();
        ByteBuffer sendBuffer = ByteBuffer.allocate(GamepieceFinderConstants.DATAGRAM_BUFFER_SIZE);
        
        for (int i = 0; i < 10; i++) { 
            sendData(channel, sendBuffer);
            Thread.sleep(1000);
        }
        channel.close();
    }

    private static void sendData(DatagramChannel channel, ByteBuffer sendBuffer)  throws Exception {
        sendBuffer.clear();
        sendBuffer.put("|0,100|5,95|-10,95|".getBytes());
        sendBuffer.flip();
        int sent = channel.send(sendBuffer, new InetSocketAddress("127.0.0.1", GamepieceFinderConstants.UDP_PORT));
        System.out.println(sent);
    }
}

