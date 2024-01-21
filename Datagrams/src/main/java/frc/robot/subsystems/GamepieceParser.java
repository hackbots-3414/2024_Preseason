// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frc.robot.Gamepiece;

/** 
 * Container for all of the parser logic for Gamepiece data. 
 * Assume received struct looks like this:
 * <code>
 * |<angle>, <confidence>|
 * </code>
*/
public abstract class GamepieceParser {
    private static Logger LOG = LoggerFactory.getLogger(GamepieceParser.class);

    /**
     * Parse a datagram buffer for note data
     * @param buffer
     * @return
     */
    public static Gamepiece[] parseRawBytes(ByteBuffer buffer) {
        ArrayList<Gamepiece> result = new ArrayList<>();
        // we can make this much more efficent, but the String comparison tools are nice to have
        // rewind puts the ByteBuffer marker back at the beginning, resetting it for reading
        buffer.rewind();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String text = new String(bytes);
        // StringTokenizer is very handy for separating a delimited string
        StringTokenizer tokenizer = new StringTokenizer(text, "|");
        Gamepiece theGamepiece = null;
        while (tokenizer.hasMoreTokens()) {
            theGamepiece = parseGamepiece(tokenizer.nextToken());
            if (theGamepiece != null) {
                result.add(theGamepiece);
            }
        }

        return result.toArray(new Gamepiece[result.size()]);
    }

    /**
     * Parse a single record for a note
     * @param token
     * @return null if the token didn't contain a readable Gamepiece
     */
    private static Gamepiece parseGamepiece(String token) {
        token = token.strip();
        if (token.length() == 0) {
            // only whitespace or nothing means junk data we're not even going to log (expected)
            return null;
        }
        Gamepiece gamepiece = new Gamepiece();
        // at this point, should have angle,confidence, so, tokenize on the ,
        int commaIndex = token.indexOf(',');
        if (commaIndex < 1) {
            // no comma means junk data
            return null;
        }
        try {
            // substring includes the from, but excludes the to
            double angle = Double.parseDouble(token.substring(0, commaIndex).strip());
            double confidence = Double.parseDouble((token.substring(commaIndex + 1).strip()));
            gamepiece.setAngle(angle);
            gamepiece.setConfidence(confidence);
        } catch (Exception e)
        {
            LOG.warn("Error parsing {}:", token, e);            
        }
        return gamepiece;
    }
}
