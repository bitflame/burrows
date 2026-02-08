import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


public class MoveToFront {
    private static final int R = 256;
    private static char[] defaultList;
    private static int relevantBits = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        defaultList = new char[R];
        for (int i = 0; i < R; i++) {
            defaultList[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char currentChar = BinaryStdIn.readChar();
            BinaryStdOut.write(activeStringIndex(currentChar), relevantBits);
            updateActiveStringIndex(currentChar);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static int activeStringIndex(int currentChar) {
        for (int i = 0; i < R; i++) {
            if (defaultList[i] == currentChar) return i;
        }
        return 0;
    }

    private static void shift(int i, int j) {
        defaultList[j] = defaultList[i];
    }

    private static void updateActiveStringIndex(int currentChar) {
        // find the current index of the character, and move the down stream character to the current location
        int index = activeStringIndex(currentChar);
        if (index > 0 && currentChar > 0) {
            do {
                shift(index - 1, index);
                index--;
            } while (index > 0);
        }
        defaultList[0] = (char) currentChar;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // System.out.println("Decoding.");
        defaultList = new char[R];
        for (int i = 0; i < R; i++) {
            defaultList[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int val = BinaryStdIn.readChar();
            BinaryStdOut.write(defaultList[val]);
            updateActiveStringIndex(defaultList[val]);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
