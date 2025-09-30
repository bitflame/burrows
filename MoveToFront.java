/* *****************************************************************************
 *  Name: Shahin M. Ansari
 *  Date: 09-26-25
 *  Description: Burrows-Wheeler encoding implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    private static char[] defaultList;
    private static boolean[] isActive;
    static int relevantBits = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        defaultList = new char[R];
        isActive = new boolean[256];
        for (int i = 0; i < R; i++) {
            defaultList[i] = (char) i;
        }
        // System.out.println("Encoding. Here is what is in the array before endoding.");
        // printString();
        int count = 0, uniqueCharCount = 0;
        while (!BinaryStdIn.isEmpty()) {
            count++;
            char currentChar = BinaryStdIn.readChar();
            if (isActive[currentChar]) {
                // find characters current index and then move the character to the front
                // BinaryStdOut.write(activeStringIndex(count, currentChar), relevantBits);
                BinaryStdOut.write(activeStringIndex(currentChar), relevantBits);
                updateActiveStringIndex(currentChar);
            }
            else {
                // since ascii value of the character is the default index of it...
                uniqueCharCount++;
                BinaryStdOut.write(activeStringIndex(currentChar), relevantBits);
                updateActiveStringIndex(currentChar);
                isActive[currentChar] = true;
            }
        }
        // System.out.println("Here is what is in the array after the string is entered.");
        // printString();
        // for (int i = 0; i < count; i++) BinaryStdOut.write(defaultList[i]);
        BinaryStdOut.close();
    }

    private static int activeStringIndex(char currentChar) {
        for (int i = 0; i < R; i++) {
            if (defaultList[i] == currentChar) return i;
        }
        return -1;
    }

    private static void shift(int i, int j) {
        defaultList[j] = defaultList[i];
    }

    private static void updateActiveStringIndex(char currentChar) {
        // find the current index of the character, and move the down stream character to the current location
        int index = activeStringIndex(currentChar);
        do {
            shift(index - 1, index);
            index--;
        } while (index > 0);
        defaultList[0] = currentChar;
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
