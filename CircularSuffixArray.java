/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.ST;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    char[][] suffixes;
    int N;
    int first;
    int[] index;
    int[] next;
    int[][] indices;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("String object is null.");
        N = s.length();
        index = new int[N];
        next = new int[N];
        suffixes = new char[N][N + 1];
        char[] chars = s.toCharArray();
        int colIndex = 1, shiftedStringId = 0;
        suffixes[0][0] = 0;
        for (char c : chars) {
            suffixes[0][colIndex++] = c;
        }
        shiftedStringId = 1;
        for (int i = 1; i < N; i++) {
            colIndex = 1;
            suffixes[i][0] = (char) shiftedStringId;
            shiftedStringId++;
            for (char c : shiftArry(chars)) {
                suffixes[i][colIndex] = c;
                colIndex++;
            }
        }
        System.out.println("Before Sorting: ");
        for (int i = 0; i < N; i++) {
            for (int j = 1; j < N + 1; j++) {
                System.out.printf("%c ", suffixes[i][j]);
            }
            System.out.println();
        }

        System.out.println("After Sorting:");
        Arrays.sort(suffixes, new Comparator<char[]>() {
            public int compare(char[] o1, char[] o2) {
                for (int i = 1; i <= N; i++) {
                    if (o1[i] > o2[i]) return 1;
                    if (o2[i] > o1[i]) return -1;
                }
                return 0;
            }
        });
        for (int i = 0; i < N; i++) {
            index[i] = -1;
            next[i] = -1;
        }
        int suffixIndex = 0, rowIndex = 0;
        while (suffixIndex < N) {
            if (suffixIndex == suffixes[rowIndex][0] && index[rowIndex] != -1) {
                index[rowIndex] = suffixIndex;
                suffixIndex++;
            }
        }

        // i is the sortedIndex, and the Symbol table can give me the original index of the string
        ST<Character, Integer> originalIndex = new ST<>();
        for (int i = 0; i < N; i++) {
            originalIndex.put(suffixes[i][0], i);
        }
        for (int i = 0; i < N; i++) {
            for (int j = 1; j <= N; j++) {
                System.out.printf("%c ", suffixes[i][j]);
            }
            System.out.println();
        }

    }

    private char[] shiftArry(char[] chars) {
        // char[] chars = s.toCharArray();
        char temp = chars[0];
        for (int i = 0; i < N - 1; i++) {
            chars[i] = chars[i + 1];
        }
        chars[N - 1] = temp;
        return chars;
    }

    // length of s
    public int length() {
        return N;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > N) throw new IllegalArgumentException("Invalid index number request.");
        return suffixes[i][0];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < 12; i++) {
            System.out.printf("Current index: %d Original index: %d\n", i,
                              circularSuffixArray.index(i));
        }
    }

}

