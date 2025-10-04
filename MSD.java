/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class MSD {
    private static int R = 256;
    private static final int M = 15;
    private static String[] aux;

    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    private void sort(String[] a) {
        int N = a.length;
        aux = new String[N];
        sort(a, 0, N - 1, 0);
    }

    // todo - First sort the suffixes array to the modified MSD that takes the start character, then try to get rid of the creating the suffixes and using the index
    private static void sort(String[] a, int lo, int hi, int d) {
        if (hi < lo) return;
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            count[charAt(a[i], d) + 2]++;
        }
        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }
        for (int i = lo; i <= hi; i++) {
            aux[count[charAt(a[i], d) + 1]++] = a[i];
        }
        for (int i = lo; i <= hi; i++) {
            a[i] = aux[i - lo];
        }
        for (int r = 0; r < R; r++) {
            // System.out.printf("lo: %d, hi: %d: d: %d\n", lo, hi, d);
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1);
        }
    }

    public static void main(String[] args) {
        MSD msd = new MSD();
        String randomString
                = "she sells seashells by the seashore the shells she sells are surely seashells";
        String stringTwo
                = "ABRACADABRA! BRACADABRA!A RACADABRA!AB ACADABRA!ABR CADABRA!ABRA ADABRA!ABRAC DABRA!ABRACA ABRA!ABRACAD BRA!ABRACADA RA!ABRACADAB A!ABRACADABR !ABRACADABRA";
        String[] a = stringTwo.split(" ");
        msd.sort(a);
        for (String s : a) {
            System.out.printf("%s\n", s);
        }
    }
}
