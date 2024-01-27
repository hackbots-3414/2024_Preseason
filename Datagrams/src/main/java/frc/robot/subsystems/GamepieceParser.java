// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.function.LongPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frc.robot.Gamepiece;
import frc.robot.Constants.GamepieceFinderConstants;

/**
 * Container for all of the parser logic for Gamepiece data.
 * Assume received struct looks like this:
 * <code>
 * |<angle>, <confidence>|
 * </code>
 */
public abstract class GamepieceParser {
    private static Logger LOG = LoggerFactory.getLogger(GamepieceParser.class);
    private static final ArrayList<Gamepiece> EMPTY_LIST = new ArrayList<>();
    private static Gamepiece[] EMPTY_ARRAY = new Gamepiece[0];
    private static StringBuilder data = new StringBuilder(GamepieceFinderConstants.DATAGRAM_BUFFER_SIZE);
    private static char theChar = 0;
    private static int pipeCount = 0;
    private static int startWord = 0;
    private static int endWord = 0;
    private static int loopIndex = 0;


    /**
     * Parse a datagram buffer for note data
     * 
     * @param buffer
     * @return
     */
    public static Gamepiece[] parseRawBytes(ByteBuffer buffer) {
        ArrayList<Gamepiece> result = new ArrayList<>();
        // we can make this much more efficent, but the String comparison tools are nice
        // to have
        // rewind puts the ByteBuffer marker back at the beginning, resetting it for
        // reading
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
     * 
     * @param token
     * @return null if the token didn't contain a readable Gamepiece
     */
    private static Gamepiece parseGamepiece(String token) {
        token = token.strip();
        if (token.length() == 0) {
            // only whitespace or nothing means junk data we're not even going to log
            // (expected)
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
        } catch (Exception e) {
            LOG.warn("Error parsing {}:", token, e);
        }
        return gamepiece;
    }

    public static ArrayList<Gamepiece> parseByteByByte(ByteBuffer buffer) {
        buffer.limit(buffer.position());
        buffer.rewind();
        ArrayList<Gamepiece> result = new ArrayList<>();
        Gamepiece piece;
        while (buffer.hasRemaining()) {
            piece = parseGamepiece(buffer);
            LOG.trace("got gamepiece: {}", piece);
            if (piece != null) {
                result.add(piece);
            }
        }
        LOG.trace("parseByteByByte() returning {}", result);
        return result;
    }

    private static Gamepiece parseGamepiece(ByteBuffer buffer) {
        Gamepiece piece = new Gamepiece();
        char theChar = (char) buffer.get();
        // munch the leading pipe
        if (!buffer.hasRemaining() || theChar != '|') {
            LOG.trace("not pipe or digit >>{}<<", theChar);
            return null;
        }
        try {
            piece.setAngle(parseDouble(buffer));
            piece.setConfidence(parseDouble(buffer));
            buffer.position(buffer.position() - 1);
        } catch (Exception e) {
            // parse error - bad message, chuck it
            LOG.error("bad message, position {}", buffer.position(), e);
            return null;
        }
        return piece;
    }

    private static double parseDouble(ByteBuffer buffer) throws ParseException {
        int startIndex = buffer.position();
        char theChar = (char) buffer.get();
        while (theChar != ',' && theChar != '|' && buffer.hasRemaining()) {
            LOG.trace("read >>{}<<", theChar);
            theChar = (char) buffer.get();
        }
        byte[] doubleBytes = new byte[buffer.position() - startIndex - 1];
        buffer.get(startIndex, doubleBytes, 0, doubleBytes.length);
        String doubleString = new String(doubleBytes);
        LOG.trace("doubleString: >>{}<<", doubleString);
        return Double.valueOf(doubleString).doubleValue();
    }

    /**
     * Favorite of the attempts, most compact, and if the variables were reused as fields, quite efficient.
     * The message sent is "trusted", that is, must be well-formed as this parser leverages the format.
     * @param buffer
     * @return
     */
    public static Gamepiece[] parseWithStringBuilder(ByteBuffer buffer) {
        try {
            data.setLength(0);;
            buffer.rewind();
            pipeCount = 0;
            // load buffer into data, counting pipes to size output array
            for (loopIndex = 0; loopIndex < buffer.limit(); loopIndex++) {
                theChar = (char) buffer.get(loopIndex);
                if (theChar == '|')
                    pipeCount++;
                data.append(theChar);
            }
            if (pipeCount == 0) {
                return EMPTY_ARRAY;
            }
            // now that we know what we're looking at, parse data
            Gamepiece[] result = new Gamepiece[pipeCount - 1];
            int startWord = 0;
            int endWord = 0;
            for (loopIndex = 0; loopIndex < result.length; loopIndex++) {
                result[loopIndex] = new Gamepiece();
                startWord = data.indexOf("|", startWord) + 1;
                endWord = data.indexOf(",", startWord);
                result[loopIndex].setAngle(Double.valueOf(data.substring(startWord, endWord)));
                startWord = endWord + 1;
                endWord = data.indexOf("|", startWord);
                result[loopIndex].setConfidence(Double.valueOf(data.substring(startWord, endWord)));
            }
            return result;

        } catch (Exception e) {
            LOG.error("parseWithStringBuffer()", e);
        }
        return EMPTY_ARRAY;
    }
}
