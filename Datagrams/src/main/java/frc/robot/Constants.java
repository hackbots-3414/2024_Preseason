// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/** Add your docs here. */
public class Constants {

    public static class GamepieceFinderConstants {
        // FRC 2024 rules allocate UDP/TCP ports 5800 - 5810 for team use
        public static final int UDP_PORT = 5800;
        public static final int CHANNEL_OPEN_RETRIES = 10;
        public static final int CHANNEL_OPEN_RETRY_DELAY = 10;
        public static final int DATAGRAM_BUFFER_SIZE = 512;
    }
}
