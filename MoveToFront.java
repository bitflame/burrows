/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    private static char[] _chars;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        _chars = new char[R];
        for (int i = 0; i < R; i++) {
            _chars[i] = (char) i;
        }
        // List<Character> stringChars = new ArrayList<>();
        // System.out.println("Encoding. Here is what is in the array before endoding.");
        // printString();
        while (!BinaryStdIn.isEmpty()) {
            char currentChar = BinaryStdIn.readChar();
            BinaryStdOut.write(currentChar);
            // BinaryStdOut.write(_chars[currentChar]);
            // stringChars.add(currentChar);
        }
        // System.out.println("Here is what is in the array after the string is entered.");
        // printString();
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
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
        // encode();
    }
}
