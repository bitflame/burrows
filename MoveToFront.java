/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.List;

public class MoveToFront {
    private static final int R = 256;
    private static char[] _chars;
    private static List<Character> stringChars;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        _chars = new char[R];
        for (int i = 0; i < R; i++) {
            _chars[i] = (char) i;
        }
        stringChars = new ArrayList<>();
        // System.out.println("Encoding. Here is what is in the array before endoding.");
        // printString();
        int count = 0;
        while (!BinaryStdIn.isEmpty()) {
            count++;
            char currentChar = BinaryStdIn.readChar();
            for (int i = currentChar; i > 0; i--) {
                _chars[i] = _chars[i - 1];
            }
            _chars[0] = currentChar;
        }
        // System.out.println("Here is what is in the array after the string is entered.");
        // printString();
        // todo - need to get clarification on where move to front is happening. It is doing what I expect and I get the result I want from Hexdump so far
        for (int i = 0; i < count; i++) BinaryStdOut.write(_chars[i]);
        BinaryStdOut.close();
    }

    private static void printString() {
        for (int i = 0; i < R; i++) {
            // System.out.println(_chars[i]);
            BinaryStdOut.write(_chars[i]);
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        System.out.println("Decoding.");
        for (char c : stringChars) {
            for (int i = c; i > 0; i--) {
                _chars[c] = _chars[c - 1];
            }
        }
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
        // encode();
    }
}
