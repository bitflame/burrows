import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output returns first and t[] for ex.
    // 3
    // ARD!RCAAAABB t [] is a[] in key-indexed counting sort
    private static int N;
    private static int R = 256;
    private static char[] t;
    private static int[] count;
    private static char[] aux;
    private static int first;

    public static void transform() {
        String str = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(str);
        N = str.length();
        t = new char[N];
        for (int i = 0; i < N; i++) {
            if (circularSuffixArray.index(i) == 0) first = i;
        }
        BinaryStdOut.write(first);
        CircularSuffixArray.CircularSuffix currentSuffix;
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write((char) str.charAt(circularSuffixArray.index(i)));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // read in an integer and a string, loop through the string and set
        // next[] to the index of str.chartAt(0) == ! i.e. the string that
        // starts with the character you are looking at - The first instance of it
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
