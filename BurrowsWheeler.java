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
        t = "ARD!RCAAAABB".toCharArray();
        N = t.length;
        count = new int[R + 1];
        aux = new char[N];
        for (int i = 0; i < N; i++) {
            count[t[i] + 1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }
        System.out.println("");
        for (int i = 0; i < N; i++) {
            aux[count[t[i]]++] = t[i];
        }
        for (int i = 0; i < N; i++) {
            t[i] = aux[i];
        }
        // for (char c : t) {
        //     System.out.printf("%c ", c);
        // }
        // System.out.println();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        // inverseTransform();
    }
}
