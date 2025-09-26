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
    private static List<Character> stringChars;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        stringChars = new ArrayList<>();
        int count = 0;
        while (!BinaryStdIn.isEmpty()) {
            count++;
            char currentChar = BinaryStdIn.readChar();
            if (stringChars.contains(currentChar)) {
                BinaryStdOut.write(charIndex(currentChar));
            }
            else {
                BinaryStdOut.write(0);
            }
            stringChars.add(currentChar);
        }
        BinaryStdOut.close();
    }

    private static int charIndex(char currentChar) {
        int index = 0;
        for (char c : stringChars) {
            if (c == currentChar) return index;
            index++;
        }
        return -1;
    }

    private static void printString() {
        for (char c : stringChars) System.out.println(" " + c + " ");
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        System.out.println("Decoding.");
        for (char c : stringChars) {
            for (int i = c; i > 0; i--) {
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
