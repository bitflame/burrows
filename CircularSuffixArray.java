import edu.princeton.cs.algs4.Queue;

public class CircularSuffixArray {
    private static int R = 256;
    private static int[] count;
    private static int[] aux;
    private static CircularSuffix[] suffixes;
    private static int N;
    private static final int RADIX_SORT_CUTOFF = 12;
    private static int d;
    private static String s;
    private static Queue<Matches> matchingQueue;

    public CircularSuffixArray(String str) {
        if (str == null)
            throw new IllegalArgumentException(
                    "                         The strings passed to this method should not be null.");
        int rowCounter;
        matchingQueue = new Queue<>();
        s = str;
        N = str.length();
        int hi, lo = 0;
        suffixes = new CircularSuffix[s.length()];
        for (int i = 0; i < N; i++) {
            suffixes[i] = new CircularSuffix(i, s);
        }
        aux = new int[N];
        d = 0;
        sort(suffixes, 0, N - 1, d++);
        int startOfMatch = lo;
        while (!matchingQueue.isEmpty()) {
            Matches currentMatch = matchingQueue.dequeue();
            hi = currentMatch.hi;
            lo = currentMatch.lo;
            while (hi - lo <= 0 && !matchingQueue.isEmpty()) {
                currentMatch = matchingQueue.dequeue();
                hi = currentMatch.hi;
                lo = currentMatch.lo;
            }
            if (hi - lo > 0) {
                d = currentMatch.d;
                Radix_or_InsertionSort(suffixes, lo, hi, d);
                rowCounter = currentMatch.lo;
                startOfMatch = rowCounter;
                while (rowCounter <= hi) {
                    while (rowCounter < hi && rowCounter < N - 1
                            && suffixes[rowCounter].charAt(d) == suffixes[rowCounter + 1].charAt(
                            d)) {
                        rowCounter++;
                    }
                    if (rowCounter - startOfMatch >= 1) {
                        matchingQueue.enqueue(new Matches(startOfMatch, rowCounter, d + 1));
                    }
                    rowCounter++;
                    startOfMatch = rowCounter;
                }
            }
        }
    }

    private boolean nextColumn(CircularSuffix[] suffixes, int start, int end, int column) {
        for (int i = start; i < end; i++) {
            if (suffixes[i].charAt(column) != suffixes[i + 1].charAt(column))
                return false;
        }
        return true;
    }

    private void Radix_or_InsertionSort(CircularSuffix[] suffixes, int lo, int hi, int column) {
        if (hi - lo > RADIX_SORT_CUTOFF)
            sort(suffixes, lo, hi, column);
        else
            insertionSortForCircSuffixArray(suffixes, lo, hi, column, 0);
    }

    private void insertionSortForCircSuffixArray(CircularSuffix[] a, int lo, int hi, int d,
                                                 int offset) {

        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--) {
                exchCircSuffixes(a, j, j - 1);
            }
        }
        // System.out.printf("Line 181 message - lo: %d hi: %d d: %d offset: %d applied Insertion sSort:\n",
        //         lo, hi, d, offset);
        // for (int i = 0; i < a.length; i++) {
        //     System.out.printf("Line: %2d- index: %2d", i, a[i].start);
        //     for (int j = 0; j < a.length; j++) {
        //         System.out.printf("%2c ", (char) a[i].charAt(j));
        //     }
        //     System.out.println();
        // }
        // System.out.println();
        // System.out.printf("Here is what is in it afterwards: \n");
        // printCircSuffixArray(a);
    }

    private void exchCircSuffixes(CircularSuffix[] a, int second, int first) {
        int temp = a[first].start;
        a[first].start = a[second].start;
        a[second].start = temp;
    }

    private boolean less(CircularSuffix c1, CircularSuffix c2, int d) {
        return c1.charAt(d) < c2.charAt(d);
    }

    private static class Matches {
        int lo = 0;
        int hi = N;
        int d = 0;
        // Matches parentMatch;

        private Matches(int lo, int hi, int d) {
            this.lo = lo;
            this.hi = hi;
            this.d = d;
            // this.parentMatch = parentMatch;
        }
    }

    private static class CircularSuffix {
        int start;
        String str;

        private CircularSuffix(int start, String str) {
            this.start = start;
            this.str = str;
        }

        int charAt(int i) {
            return i >= str.length() ? -1 : str.charAt((start + i) % str.length());
        }
    }

    // returns the index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > N)
            throw new IllegalArgumentException(
                    "The suffix index should be within the string lenght it refers to.");
        return suffixes[i].start;
    }

    // length of s
    public int length() {
        return N;
    }

    private static void sort(CircularSuffix[] a, int lo, int hi, int d) {
        int updatedLo = lo, updatedHi = 0;
        count = new int[R + 2];
        // char c;

        for (int i = lo; i <= hi; i++) {
            // c = (char) a[i].charAt(d + offset);
            // System.out.println(c);
            count[a[i].charAt(d) + 2]++;
        }

        // for (int i = 1; i < R; i++) {
        // if (count[i] > 0) {
        // countFrequencies[countFrequenciesCounter] = count[i];
        // countFrequenciesCounter++;
        // if (countFrequenciesCounter * 2 > countFrequencies.length)
        // countFrequencies = arrayResize(countFrequencies, countFrequencies.length,
        // countFrequencies.length * 2);
        // }
        // }
        // if (lo == 0 && count[0] > 0) {
        // countFrequencies[countFrequenciesCounter++] = new Matches(lo, count[0], d +
        // 1);
        // }
        Matches currentMatch;
        for (int r = 0; r < R; r++) {
            if (count[r + 1] > 0) {
                if (matchingQueue.isEmpty()) {
                    updatedLo = 0;
                    currentMatch = new Matches(updatedLo, updatedLo + count[r + 1] - 1, d + 1);
                    matchingQueue.enqueue(currentMatch);
                    updatedLo = count[r + 1];
                }
                else {
                    updatedHi = updatedLo + count[r + 1] - 1;
                    currentMatch = new Matches(updatedLo, updatedHi, d + 1);
                    matchingQueue.enqueue(currentMatch);
                    updatedLo += count[r + 1];
                }
            }

            count[r + 1] += count[r];
        }
        // System.out.printf("Here is what is in a[] before the change...Hi and lo
        // values are %d %d\n", hi, lo);
        // printCircSuffixArray(a);
        for (int i = lo; i <= hi; i++) {
            // c = (char) a[i].charAt(d);
            aux[count[a[i].charAt(d) + 1]++] = a[i].start;
            // boundris.enqueue(count[a[i].charAt(d)]);
        }

        for (int i = lo; i <= hi; i++) {
            a[i].start = aux[i - lo];
        }
        // System.out.println("Here is what is in it afterwards: ");
        // printCircSuffixArray(a);
        // System.out.printf("Line 316 message - hi: %d lo: %d d: %d offset: %d After Radix Sorting: \n", hi,
        //         lo, d, offset);
        // for (int i = 0; i < a.length; i++) {
        //     System.out.printf("Line: %2d- Index: %2d ", i, a[i].start);
        //     for (int j = 0; j < a.length; j++) {
        //         System.out.printf("%2c ", (char) a[i].charAt(j));

        //     }
        //     System.out.println();
        // }
        // System.out.println();
    }

    private static void printCircSuffixArray(CircularSuffix[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(suffixes[i].start);
            for (int j = 0; j < a.length; j++) {
                System.out.printf(" %c", (char) a[i].charAt(j));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // System.out.println(" Processing the current string---------------");
        // String currentString = "BANANA";
        // CircularSuffixArray csaCurrent = new CircularSuffixArray(currentString);
        // System.out.println(" Printing the count frequencies:");
        // for (int i: countFrequencies) {
        // System.out.printf("%d ",i);
        // }
        // System.out.println("Expecting 3, Actual: " + csaCurrent.index(1));
        System.out.println("           Test #1-              Processing The String ABC");
        String shortString = "ABC";
        CircularSuffixArray csaShort = new CircularSuffixArray(shortString);
        // for (int i = 0; i < shortString.length(); i++) {
        // System.out.println(csaShort.index(i));
        // }
        System.out.println("Printing the final results of sorting the string: ABC ");
        for (int i = 0; i < csaShort.N; i++) {
            for (int j = 0; j < csaShort.N; j++) {
                System.out.printf(" %c ", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println("           Test #2-            Processing The String ABRACADABRA!");
        String str = "ABRACADABRA!";
        int N = str.length();// change str to ARD!RCAAAABB and see if it sorted
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Printing the final results of sorting the string: ABRACADABRA! ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c ", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.printf("ABRACADABRA! IndexOf(0) - Expectd: 11, Actual: %d\n",
                          circularSuffixArray.index(0));
        System.out.printf("ABRACADABRA! IndexOf(1) - Expectd: 10, Actual: %d\n",
                          circularSuffixArray.index(1));
        System.out.printf("ABRACADABRA! IndexOf(2) - Expectd: 7, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.printf("ABRACADABRA! IndexOf(3) - Expectd: 0, Actual: %d\n",
                          circularSuffixArray.index(3));
        System.out.printf("ABRACADABRA! IndexOf(4) - Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(4));
        System.out.printf("ABRACADABRA! IndexOf(5) - Expectd: 5, Actual: %d\n",
                          circularSuffixArray.index(5));
        System.out.printf("ABRACADABRA! IndexOf(6) - Expectd: 8, Actual: %d\n",
                          circularSuffixArray.index(6));
        System.out.printf("ABRACADABRA! IndexOf(7) - Expectd: 1, Actual: %d\n",
                          circularSuffixArray.index(7));
        System.out.printf("ABRACADABRA! IndexOf(8) - Expectd: 4, Actual: %d\n",
                          circularSuffixArray.index(8));
        System.out.printf("ABRACADABRA! IndexOf(9) - Expectd: 6, Actual: %d\n",
                          circularSuffixArray.index(9));
        System.out.printf("ABRACADABRA! IndexOf(10) - Expectd: 9, Actual: %d\n",
                          circularSuffixArray.index(10));
        System.out.printf("ABRACADABRA! IndexOf(11) - Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(11));
        // System.out.println(circularSuffixArray.length());
        // for (int i = 0; i < N; i++) {
        // System.out.println(circularSuffixArray.index(i));
        // }
        System.out.println("           Test #3-        Processing String BANANA");
        str = "BANANA";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Printing the final results of sorting the string: BANANA ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c ", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println("Expecting 5, Getting: " + circularSuffixArray.index(0));
        System.out.println("Expecting 3, Getting: " + circularSuffixArray.index(1));
        System.out.println("Expecting 1, Getting: " + circularSuffixArray.index(2));
        System.out.println("Expecting 0, Getting: " + circularSuffixArray.index(3));
        System.out.println("Expecting 4, Getting: " + circularSuffixArray.index(4));
        System.out.println("Expecting 2, Getting: " + circularSuffixArray.index(5));
        System.out.println();
        System.out.println("           Test #4-         Processing The String ARD!RCAAAABB");
        str = "ARD!RCAAAABB";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Printing the final results of sorting the string: ARD!RCAAAABB ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.printf("ARD!RCAAAABB IndexOf(0) - Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(0));
        System.out.printf("ARD!RCAAAABB IndexOf(10) - Expectd: 6, Actual: %d\n",
                          circularSuffixArray.index(1));
        System.out.printf("ARD!RCAAAABB IndexOf(2) - Expectd: 7, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.printf("ARD!RCAAAABB IndexOf(3) - Expectd: 8, Actual: %d\n",
                          circularSuffixArray.index(3));
        System.out.printf("ARD!RCAAAABB IndexOf(4) - Expectd: 9, Actual: %d\n",
                          circularSuffixArray.index(4));
        System.out.printf("ARD!RCAAAABB IndexOf(5) - Expectd: 0, Actual: %d\n",
                          circularSuffixArray.index(5));
        System.out.printf("ARD!RCAAAABB IndexOf(6) - Expectd: 11, Actual: %d\n",
                          circularSuffixArray.index(6));
        System.out.printf("ARD!RCAAAABB IndexOf(7) - Expectd: 10, Actual: %d\n",
                          circularSuffixArray.index(7));
        System.out.printf("ARD!RCAAAABB IndexOf(8) - Expectd: 5, Actual: %d\n",
                          circularSuffixArray.index(8));
        System.out.printf("ARD!RCAAAABB IndexOf(9) - Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(9));
        System.out.printf("ARD!RCAAAABB IndexOf(10) - Expectd: 4, Actual: %d\n",
                          circularSuffixArray.index(10));
        System.out.printf("ARD!RCAAAABB IndexOf(11) - Expectd: 1, Actual: %d\n",
                          circularSuffixArray.index(11));
        System.out.println("          Test #5-         Processing String ABBB");
        str = "ABBB";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Printing the final results of sorting the Test # 5 string: ABBB  ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c ", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.printf("ABBB IndexOf(0) - Expectd: 0, Actual: %d\n",
                          circularSuffixArray.index(0));
        System.out.printf("ABBB IndexOf(1) - Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(1));
        System.out.printf("ABBB IndexOf(2) - Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.printf("ABBB IndexOf(3) - Expectd: 1, Actual: %d\n",
                          circularSuffixArray.index(3));

        System.out.println("         Test #6-          Processing String ABBBB");
        str = "ABBBB";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Printing the final results of sorting Test #6 - string: ABBBB ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c ", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.printf("ABBBB IndexOf(0) - Expectd: 0, Actual: %d\n",
                          circularSuffixArray.index(0));
        System.out.printf("ABBBB IndexOf(1) - Expectd: 4, Actual: %d\n",
                          circularSuffixArray.index(1));
        System.out.printf("ABBBB IndexOf(2) - Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.printf("ABBBB IndexOf(3) - Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(3));
        System.out.printf("ABBBB IndexOf(4) - Expectd: 1, Actual: %d\n",
                          circularSuffixArray.index(4));

        System.out.println("           Test #7-         Processing String AAABBA");
        str = "AAABBA";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Printing the final results of Test #7 sorting the string: AAABBA ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            // System.out.printf("index of %d is %d\n", i, circularSuffixArray.index(i));
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c ", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf("AAABBA IndexOf(0) - Expectd: 5, Actual: %d\n",
                          circularSuffixArray.index(0));
        System.out.printf("AAABBA IndexOf(1) - Expectd: 0, Actual: %d\n",
                          circularSuffixArray.index(1));
        System.out.printf("AAABBA IndexOf(2) - Expectd: 1, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.printf("AAABBA IndexOf(3) - Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(3));
        System.out.printf("AAABBA IndexOf(4) - Expectd: 4, Actual: %d\n",
                          circularSuffixArray.index(4));
        System.out.printf("AAABBA IndexOf(5) - Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(5));
        // System.out.printf("AAABBA IndexOf(0) RecycledObject - Expectd: 5, Actual:
        // %d\n", circularSuffixArray.index(0));
        // CircularSuffixArray lastCirculSuffArray = new CircularSuffixArray("AAABA");
        // System.out.printf("AAABBA IndexOf(0) New Object - Expectd: 5, Actual: %d\n",
        // lastCirculSuffArray.index(0));

        System.out.println("        Test #8    ----                Processing String BBABBAA");
        str = "BBABBAA";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Printing the final results of Test #8 sorting the string: BBABBAA ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c ", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf("BBABBAA IndexOf(0) - Expectd: 5, Actual: %d\n",
                          circularSuffixArray.index(0));
        System.out.printf("BBABBAA IndexOf(1) - Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(1));
        System.out.printf("BBABBAA IndexOf(2) - Expectd: 6, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.printf("BBABBAA IndexOf(3) - Expectd: 4, Actual: %d\n",
                          circularSuffixArray.index(3));
        System.out.printf("BBABBAA IndexOf(4) - Expectd: 1, Actual: %d\n",
                          circularSuffixArray.index(4));
        System.out.printf("BBABBAA IndexOf(5) - Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(5));
        System.out.printf("BBABBAA IndexOf(6) - Expectd: 0, Actual: %d\n",
                          circularSuffixArray.index(6));
        System.out.println("        Test # 9    ----                Processing String BBAABBBB");
        str = "BBAABBBB";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Printing the final results of Test #9 - sorting the string: BBAABBBB ");
        for (int i = 0; i < circularSuffixArray.N; i++) {

            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf("BBAABBBB IndexOf(0) RecycledObject - Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(0));
        System.out.printf("BBAABBBB IndexOf(1) RecycledObject - Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(1));
        System.out.printf("BBAABBBB IndexOf(2) RecycledObject - Expectd: 1, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.printf("BBAABBBB IndexOf(3) RecycledObject - Expectd: 0, Actual: %d\n",
                          circularSuffixArray.index(3));
        System.out.printf("BBAABBBB IndexOf(4) RecycledObject - Expectd: 7, Actual: %d\n",
                          circularSuffixArray.index(4));
        System.out.printf("BBAABBBB IndexOf(5) RecycledObject - Expectd: 6, Actual: %d\n",
                          circularSuffixArray.index(5));
        System.out.printf("BBAABBBB IndexOf(6) RecycledObject - Expectd: 5, Actual: %d\n",
                          circularSuffixArray.index(6));
        System.out.printf("BBAABBBB IndexOf(7) RecycledObject - Expectd: 4, Actual: %d\n",
                          circularSuffixArray.index(7));

        System.out.println("        Test # 10    ----                Processing String ABABABBAB");
        str = "ABABABBAB";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(
                "Printing the final results of Test #10 - sorting the string: ABABABBAB ");
        for (int i = 0; i < circularSuffixArray.N; i++) {

            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }

        System.out.println();
        System.out.printf("ABABABBAB IndexOf(0) RecycledObject - Expectd: 7, Actual: %d\n",
                          circularSuffixArray.index(0));
        System.out.printf("ABABABBAB IndexOf(1) RecycledObject - Expectd: 0, Actual: %d\n",
                          circularSuffixArray.index(1));
        System.out.printf("ABABABBAB IndexOf(2) RecycledObject - Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.printf("ABABABBAB IndexOf(3) RecycledObject - Expectd: 4, Actual: %d\n",
                          circularSuffixArray.index(3));
        System.out.printf("ABABABBAB IndexOf(4) RecycledObject - Expectd: 6, Actual: %d\n",
                          circularSuffixArray.index(4));
        System.out.printf("ABABABBAB IndexOf(5) RecycledObject - Expectd: 8, Actual: %d\n",
                          circularSuffixArray.index(5));
        System.out.printf("ABABABBAB IndexOf(6) RecycledObject - Expectd: 1, Actual: %d\n",
                          circularSuffixArray.index(6));
        System.out.printf("ABABABBAB IndexOf(7) RecycledObject - Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(7));
        System.out.println();

        System.out.println("        Test # 11    ----                Processing String AABBABBABB");
        str = "AABBABBABB";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(
                "Printing the final results of Test #11 - sorting the string: AABBABBABB ");
        for (int i = 0; i < circularSuffixArray.N; i++) {

            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf(
                "AABBABBABB IndexOf(0) RecycledObject - Expectd: A value instead of error , Actual: %d\n",
                circularSuffixArray.index(0));

        // System.out.println("Test # 12 ---- Processing String
        // ABAABBABABBABBABAABAABAAABBAABBBAAAAABBAAABAABAABA");
        // str = "ABAABBABABBABBABAABAABAAABBAABBBAAAAABBAAABAABAABA";
        // circularSuffixArray = new CircularSuffixArray(str);
        // System.out.println(
        // "Printing the final results of Test #11 - sorting the string:
        // ABAABBABABBABBABAABAABAAABBAABBBAAAAABBAAABAABAABA ");
        // for (int i = 0; i < circularSuffixArray.N; i++) {
        // for (int j = 0; j < circularSuffixArray.N; j++) {
        // System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
        // }
        // System.out.println();
        // }
        // System.out.println();
        System.out.println("Test # 13 ---- Processing String ATTTT");
        str = "ATTTT";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(
                "Printing the final results of Test #13 - sorting the string: ATTTT ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf("ATTTT IndexOf(2) Expectd: 3, Actual: %d\n",
                          circularSuffixArray.index(2));
        System.out.println("Test # 14 ---- Processing String BBABAB");
        str = "BBABAB";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(
                "Printing the final results of Test #14 - sorting the string: BBABAB ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf("BBABAB IndexOf(0) Expectd: 2, Actual: %d\n",
                          circularSuffixArray.index(0));

        System.out.println("Test # 15 ---- Processing String BBBABBA");
        str = "BBBABBA";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(
                "Printing the final results of Test #15 - sorting the string: BBBABBA ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf("BBBABBA IndexOf(5) Expectd: 4, Actual: %d\n",
                          circularSuffixArray.index(5));

        System.out.println("Test # 16 ---- Processing String BABABBA");
        str = "BABABBA";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(
                "Printing the final results of Test #16 - sorting the string: BABABBA ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf("BABABBA IndexOf(3) Expectd: 5, Actual: %d\n",
                          circularSuffixArray.index(3));
        str = "";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println("Expected value: 0, Actual: " + circularSuffixArray.length());

        // System.out.println("Test # 17 ---- Processing String
        // BABBBBBBAAAAAABBBBBAABABABBABBBABBABBBBBBBBABABAAB");
        // str = "BABBBBBBAAAAAABBBBBAABABABBABBBABBABBBBBBBBABABAAB";
        // circularSuffixArray = new CircularSuffixArray(str);
        // System.out.println(
        // "Printing the final results of Test #17 - sorting the string:
        // BABBBBBBAAAAAABBBBBAABABABBABBBABBABBBBBBBBABABAAB ");
        // for (int i = 0; i < circularSuffixArray.N; i++) {
        // for (int j = 0; j < circularSuffixArray.N; j++) {
        // System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
        // }
        // System.out.println();
        // }
        // System.out.println();
        // System.out.printf("BABBBBBBAAAAAABBBBBAABABABBABBBABBABBBBBBBBABABAAB
        // IndexOf(13) Expectd: 31, Actual: %d\n",
        // circularSuffixArray.index(13));

        System.out.println(
                "Test # 18 ---- Processing String BBBBBBBBAAAABBAAAAABBBABBBBABAABBBABABBBBBBAABBBBA");
        str = "BBBBBBBBAAAABBAAAAABBBABBBBABAABBBABABBBBBBAABBBBA";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(
                "Printing the final results of Test #18 - sorting the string: BBBBBBBBAAAABBAAAAABBBABBBBABAABBBABABBBBBBAABBBBA ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        System.out.printf(
                "BBBBBBBBAAAABBAAAAABBBABBBBABAABBBABABBBBBBAABBBBA IndexOf(27) Expectd: 12, Actual: %d\n",
                circularSuffixArray.index(27));
        // System.out.println("Test # 19 ---- Processing String uŒÿ—(õ+{‹£SUBESC^‰ESCš");
        System.out.println("Test # 19 ---- Processing String BÿAUR");
        str = "BÿAUR";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(
                "Printing the final results of Test #18 - sorting the string: uŒÿ—(õ+{‹£SUBESC^‰ESCš ");
        for (int i = 0; i < circularSuffixArray.N; i++) {
            for (int j = 0; j < circularSuffixArray.N; j++) {
                System.out.printf(" %c", CircularSuffixArray.suffixes[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
    }
}
