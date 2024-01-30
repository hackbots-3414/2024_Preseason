// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import frc.robot.Constants.NoteFinderConstants;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.junit.jupiter.api.Test;

/** Add your docs here. */
public class NoteFinderTest {
    private static NoteFinder noteFinder = new NoteFinder();
    static DatagramChannel noteChannel = null;
    static ByteBuffer byteSender = null;
    static {
        try {
            noteChannel = DatagramChannel.open();
            byteSender = ByteBuffer.allocate(NoteFinderConstants.BUFFER_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public NoteFinderTest() throws IOException {
    }
    private int sendMessage(String message) throws IOException {
        byteSender.clear();
        byteSender.put(message.getBytes());
        byteSender.flip();
        int bytesSent = noteChannel.send(byteSender,
                new InetSocketAddress("127.0.0.1", NoteFinderConstants.DATAGRAM_PORT));
        noteFinder.dataReceiver();
        return bytesSent;

    }
    public void testZeroNotes() throws IOException {
        sendMessage("[]|[]|\"No Game Piece\"");
        assertEquals("No Game Piece", noteFinder.getStatus(), "Status message finder");
        assertEquals(0, noteFinder.getGamepieces().length, " Number of Gamepieces");
        System.out.println("Now - LastUpdateTime: " + (System.currentTimeMillis() - noteFinder.getLastUpdateTime()));
        assertTrue(noteFinder.getLastUpdateTime() > System.currentTimeMillis() - 25, "Testing LastUpdateTime");
    } 
    public void testOneNote() throws IOException {
        sendMessage("[162]|[62]|\"One Game Piece\"");
        assertEquals("One Game Piece", noteFinder.getStatus(), "Status message finder");
        assertEquals(1, noteFinder.getGamepieces().length, " Number of Gamepieces");
        System.out.println("Now - LastUpdateTime: " + (System.currentTimeMillis() - noteFinder.getLastUpdateTime()));
        assertTrue(noteFinder.getLastUpdateTime() > System.currentTimeMillis() - 25, "Testing LastUpdateTime");
        assertEquals(162.0, noteFinder.getGamepieces()[0].getAngle(), "Angle Tester");
        assertEquals(62.0, noteFinder.getGamepieces()[0].getConfidence(), "Confidence Tester");
    }
    @Test
    public void testBothTests() throws IOException {
        testZeroNotes();
        testOneNote();
    }
}
