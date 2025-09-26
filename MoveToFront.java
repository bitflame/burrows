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
    private static char[] defaultList;
    private static char[] activeString;
    private static boolean[] isActive;
    private static List<Character> stringChars;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        defaultList = new char[R];
        activeString = new char[R];
        stringChars = new ArrayList<>();
        for (int i = 0; i < R; i++) {
            defaultList[i] = (char) i;
        }
        // System.out.println("Encoding. Here is what is in the array before endoding.");
        // printString();
        int count = 0;
        while (!BinaryStdIn.isEmpty()) {
            count++;
            char currentChar = BinaryStdIn.readChar();
            if (isActive[currentChar]) {
                // find characters current index and then move the character to the front
                BinaryStdOut.write(activeStringIndex(count, currentChar));
                updateActiveStringIndex(count, currentChar);
            }
            else {
                // since ascii value of the character is the default index of it...
                BinaryStdOut.write(currentChar);
                isActive[currentChar] = true;
                updateActiveStringIndex(count, currentChar);
                // Just in case the exact string is needed later
                stringChars.add(currentChar);
            }
        }
        // System.out.println("Here is what is in the array after the string is entered.");
        // printString();
        for (int i = 0; i < count; i++) BinaryStdOut.write(defaultList[i]);
        BinaryStdOut.close();
    }

    private static int activeStringIndex(int count, char currentChar) {
        for (int i = 0; i < count; i++) {
            if (activeString[i] == currentChar) return i;
        }
        return -1;
    }

    private static void updateActiveStringIndex(int count, char currentChar) {
        for (int i = count; i > 0; i--) {
            activeString[i] = activeString[i - 1];
        }
        activeString[0] = currentChar;
    }

    private static void printString() {
        for (int i = 0; i < R; i++) {
            // System.out.println(_chars[i]);

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
