/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    char[][] suffixes;
    int N;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        N = s.length();
        suffixes = new char[N][N];
        char[] chars = s.toCharArray();
        int index = 0;
        suffixes[0] = new char[N];
        for (char c : chars) {
            suffixes[0][index++] = c;
        }
        for (int i = 1; i < N; i++) {
            suffixes[i] = new char[N];
            index = 0;
            for (char c : shiftArry(chars, 1)) {
                suffixes[i][index] = c;
                index++;
            }
        }
        System.out.println("Before Sorting: ");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%c ", suffixes[i][j]);
            }
            System.out.println();
        }
        System.out.println("After Sorting:");
        Arrays.sort(suffixes, new Comparator<char[]>() {
            public int compare(char[] o1, char[] o2) {
                for (int i = 0; i < N; i++) {
                    if (o1[i] > o2[i]) return 1;
                    if (o2[i] > o1[i]) return -1;
                }
                return 0;
            }
        });
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%c ", suffixes[i][j]);
            }
            System.out.println();
        }
    }

    private char[] shiftArry(char[] chars, int shiftDigits) {
        // char[] chars = s.toCharArray();
        char[] cache = new char[shiftDigits];
        int charPointer = 0;
        while (charPointer < shiftDigits) {
            cache[charPointer] = chars[charPointer];
            charPointer++;
        }
        for (int i = shiftDigits - 1; i < chars.length - 1; i++) {
            chars[i] = chars[i + shiftDigits];
        }
        charPointer = chars.length - shiftDigits;
        for (int i = 0; i < shiftDigits; i++) {
            chars[charPointer] = cache[i];
        }
        return chars;
    }

    // length of s
    public int length() {
        return -1; // for now
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return -1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");

    }

}

