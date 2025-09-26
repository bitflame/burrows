/* *****************************************************************************
 *  Name: Shahin M. Ansari
 *  Date: 09-26-25
 *  Description: Burrows-Wheeler encoding implementation
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
    static int relevantBits = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        defaultList = new char[R];
        activeString = new char[R];
        stringChars = new ArrayList<>();
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
                BinaryStdOut.write(activeStringIndex(count, currentChar), relevantBits);
                updateActiveStringIndex(uniqueCharCount, currentChar);
            }
            else {
                // since ascii value of the character is the default index of it...
                uniqueCharCount++;
                BinaryStdOut.write(currentChar);
                isActive[currentChar] = true;
                updateActiveStringIndex(uniqueCharCount, currentChar);
                // Just in case the exact string is needed later
                stringChars.add(currentChar);
            }
        }
        // System.out.println("Here is what is in the array after the string is entered.");
        // printString();
        // for (int i = 0; i < count; i++) BinaryStdOut.write(defaultList[i]);

        BinaryStdOut.close();
    }

    private static int activeStringIndex(int uniqueCharCount, char currentChar) {
        for (int i = 0; i < uniqueCharCount; i++) {
            if (activeString[i] == currentChar) return i;
        }
        return -1;
    }

    private static void updateActiveStringIndex(int uniqueCharCount, char currentChar) {
        int index;
        for (int i = uniqueCharCount; i > 0; i--) {
            if (activeString[i - 1] == currentChar) {
                index = i - 1;
                while (index <= uniqueCharCount) {
                    activeString[index] = activeString[++index];
                }
                i--;
            }
            activeString[i] = activeString[i - 1];
        }
        activeString[0] = currentChar;
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
