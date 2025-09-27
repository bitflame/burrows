/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class CircularSuffixArray {
    char[][] suffixes;
    int N;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        N = s.length();
        suffixes = new char[N][N];
        char[] chars = s.toCharArray();
        for (int i = 0; i < N; i++) {
            suffixes[i] = new char[N];
            int index = 0;
            for (char c : shiftArry(chars, 1)) {
                suffixes[i][index] = c;
                index++;
            }
        }
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

