/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Insertion;

import java.util.Comparator;

public class MSD {
    private static int R = 256;
    private static int[] count;
    private static String[] aux;
    private static int M = 4;

    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    private void sort(String[] a) {
        aux = new String[a.length];
        sort(a, aux, 0, a.length - 1, 0);
    }

    // todo - this is not soring right, the code never runs for lo = 6, hi=7 and d=3
    private static void sort(String[] a, String[] aux, int lo, int hi, int d) {

        if (hi <= lo + M) {

            sort(a, lo, hi, d);
            // Insertion.sort(a, lo, hi, new Comparator() {
            //     public int compare(Object o1, Object o2) {
            //         String v = (String) o1;
            //         String w = (String) o2;
            //         if (v.charAt(d) > w.charAt(d)) return 1;
            //         else if (w.charAt(d) < v.charAt(d)) return -1;
            //         return 0;
            //     }
            // });
            return;
        }

        count = new int[R + 2];

        for (int i = lo; i <= hi; i++)
            count[charAt(a[i], d) + 2]++;

        for (int r = 0; r < R + 1; r++)
            count[r + 1] += count[r];

        for (int i = lo; i <= hi; i++)
            aux[count[charAt(a[i], d) + 1]++] = a[i];

        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];

        for (int r = 0; r < R; r++) {
            System.out.printf("d: %d lo: %d hi: %d\n", d, lo, hi);
            sort(a, aux, lo + count[r], lo + count[r + 1] - 1, d + 1);
        }

    }

    private static void sort(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--) {
                exch(a, j, j - 1);
            }
        }
    }

    private static boolean less(String v, String w, int d) {
        for (int i = 0; i <= d; i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
        }
        // return v.charAt(d) < w.charAt(d);
        return false;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void main(String[] args) {
        /*Testing Insertion Sort*/
        String stg_1 = "ali";
        String stg_2 = "b";
        String[] my_tests = { "ali", "b", "ziba", "cami", "ali" };
        int d = 0;
        Insertion.sort(my_tests, 0, my_tests.length, new Comparator() {
            public int compare(Object o1, Object o2) {
                String v = (String) o1;
                String w = (String) o2;
                if (v.charAt(d) > w.charAt(d)) return -1;
                if (w.charAt(d) < v.charAt(d)) return 1;
                return 0;
            }
        });
        for (String s : my_tests) {
            System.out.println(s);
        }
        /*          My own insertion sort                    */
        System.out.printf("This is the resulf of my own sort\n");
        sort(my_tests, 0, my_tests.length - 1, d);
        for (String s : my_tests) {
            System.out.println(s);
        }
        MSD msd = new MSD();
        String randomString
                = "she sells seashells by the seashore the shells she sells are surely seashells";
        String stringTwo
                = "ABRACADABRA! BRACADABRA!A RACADABRA!AB ACADABRA!ABR CADABRA!ABRA ADABRA!ABRAC DABRA!ABRACA ABRA!ABRACAD BRA!ABRACADA RA!ABRACADAB A!ABRACADABR !ABRACADABRA";
        String[] original = stringTwo.split(" ");
        String[] a = stringTwo.split(" ");

        msd.sort(a);

        for (int i = 0; i < a.length; i++) {
            System.out.printf("original: %s sorted:  %s\n", original[i], a[i]);
        }
    }
}
