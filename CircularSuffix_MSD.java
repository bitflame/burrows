/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class CircularSuffix_MSD {
    private static int R = 256;
    private static String[] aux;
    private static char[][] suffixes;
    private static int N;
    private static char[] stringChars;
    private static String[] a;

    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    private static void sort(String[] a, String[] aux, int lo, int hi, int d) {
        int[] count = new int[R + 2];
        // compute freq counts
        for (int i = 0; i <= hi; i++) {
            count[charAt(a[i], d) + 2]++;
        }
        // Transform counts to indices 
        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }
        System.out.printf("");
        for (int i = 0; i <= hi; i++) {
            aux[count[charAt(a[i], d) + 1]++] = a[i];
        }
        for (int i = lo; i <= hi; i++) {
            a[i] = aux[i - lo];
        }
    }

    CircularSuffix_MSD(String string) {
        N = string.length();

        suffixes = new char[N][N + 1];
        suffixes[0][0] = 0;
        a = new String[N];
        int colIndex = 1, shiftedStringIndex = 1;
        stringChars = string.toCharArray();
        for (char c : stringChars) {
            suffixes[0][colIndex] = c;
            colIndex++;
        }
        // System.out.println("Here is line:" + 0);
        // System.out.println(suffixes[0]);
        for (int i = 1; i < N; i++) {
            suffixes[i][0] = (char) shiftedStringIndex;
            shiftedStringIndex++;
            colIndex = 1;
            for (char c : shiftArry(stringChars)) {
                suffixes[i][colIndex] = c;
                colIndex++;
            }
            // System.out.println("Here is line:" + i);
            // System.out.println(suffixes[i]);
        }
        System.out.println("Before Sorting: ");
        StringBuilder sb;
        for (int i = 0; i < N; i++) {
            sb = new StringBuilder();
            for (int j = 0; j < N + 1; j++) {
                System.out.printf("%c ", suffixes[i][j]);
                sb.append(suffixes[i][j]);
            }
            a[i] = sb.toString();
            System.out.println();
        }
        System.out.println();
        aux = new String[a.length];
        sort(a, aux, 0, string.length() - 1, 1);
        System.out.println("Here is contents of a after soring: ");
        for (String s : a) {
            System.out.println(s);
        }
    }

    private char[] shiftArry(char[] chars) {
        char temp = chars[0];
        for (int i = 0; i < N - 1; i++) {
            chars[i] = chars[i + 1];
        }
        chars[N - 1] = temp;
        return chars;
    }

    public static void main(String[] args) {
        CircularSuffix_MSD circularSuffixMsd = new CircularSuffix_MSD("ABRACADABRA!");
    }
}
