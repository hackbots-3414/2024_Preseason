// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


/** 
 * Very simple JUnit unit test.
 * See https://docs.wpilib.org/en/stable/docs/software/wpilib-tools/robot-simulation/unit-testing.html
 */
public class ConstantsTest {

    /**
     * Test our inchesToTicks() method
     */
    @Test
    public void testInchesToTicks() {
        assertEquals(60110.27043376122, Constants.inchesToTicks(51));
    }

    @Test
    public void testTicksToInches() {
        assertEquals(51, Constants.ticksToInches(60110.27043376122));
    }
}
