public class CircularSuffixArray {

    private static int R = 256;
    private static int[] count;
    private static int[] aux;
    private static CircularSuffix[] suffixes;
    private static int N;
    private static int offset, countFrequenciesCounter;
    private static int[] countFrequencies;
    private static String s;
    private static final int RADIX_SORT_CUTOFF = 12;
    private static int d = 0;

    public CircularSuffixArray(String s) {
        countFrequenciesCounter = 0;
        this.s = s;
        N = s.length();
        suffixes = new CircularSuffix[s.length()];
        for (int i = 0; i < N; i++) {
            suffixes[i] = new CircularSuffix(i, s);
        }
        int generalCounter = 0; // index for the upcoming loops...
        offset = 0; // tracks the columns of common characters between lo and hi
        aux = new int[N];
        countFrequencies = new int[N];
        int lo = 0, hi = N
                - 1; // d: column being considered from lo: the first string to hi: the last string
        System.out.printf("line 29 message: lo: %d, hi: %d d: %d offset: %d \n", lo, hi, d, offset);
        sort(suffixes, lo, hi, d, 0);
        while (countFrequencies[generalCounter] != 0) {
            lo = lo + countFrequencies[generalCounter];
            hi = lo + countFrequencies[generalCounter + 1] - 1;
            generalCounter++;
            if (hi > lo) sortMatchingStrings(lo, hi, d);
        }
    }

    private void updateRowsSorted(int rowsSorted) {
        while (suffixes[rowsSorted].charAt(d) < suffixes[rowsSorted].charAt(d)) {
            rowsSorted++;
        }
    }

    private void sortMatchingStrings(int lo, int hi, int d) {
        // returns the values for offset, matchingLo, matchingHi, and count of the region with matching characters
        // for the current column referred to by d
        int startOfMatch = lo, endOfMatch = lo, currentIndex = lo, localOffset
                = 0; // matching hi starts at lo and increases
        offset = d;
        boolean foundRowsWithMatchingCharacters = true;
        currentIndex = lo;
        while (foundRowsWithMatchingCharacters) {
            foundRowsWithMatchingCharacters = false;
            while (currentIndex < hi) {
                if (currentIndex <= hi
                        && suffixes[currentIndex].charAt(offset + localOffset) == suffixes[
                        currentIndex + 1].charAt(offset + localOffset)) {
                    startOfMatch = currentIndex;
                    foundRowsWithMatchingCharacters = true;
                    while (currentIndex < hi
                            && suffixes[currentIndex].charAt(offset + localOffset) == suffixes[
                            currentIndex + 1].charAt(offset + localOffset)) {
                        currentIndex++;
                    }
                    localOffset++;
                    endOfMatch = currentIndex;
                    int temp = startOfMatch;
                    // can I add another col? temp is just a temporary variable poiting to each line instead of currentIndex
                    while (suffixes[startOfMatch].charAt(offset + localOffset) == suffixes[
                            startOfMatch + 1].charAt(offset + localOffset) && temp <= endOfMatch) {
                        while (temp < endOfMatch
                                && suffixes[temp].charAt(offset + localOffset) == suffixes[temp
                                + 1].charAt(offset + localOffset)) {
                            temp++;
                        }
                        localOffset++;
                        if (temp == endOfMatch
                                && suffixes[startOfMatch].charAt(offset + localOffset) == suffixes[
                                startOfMatch + 1].charAt(offset + localOffset)) {
                            temp = startOfMatch;
                        }
                    }
                    if (startOfMatch - endOfMatch > RADIX_SORT_CUTOFF)
                        sort(suffixes, startOfMatch, endOfMatch, offset, localOffset);
                    else insertionSortForCircSuffixArray(suffixes, startOfMatch, endOfMatch, offset,
                                                         localOffset);
                }
                currentIndex++;
            }
            currentIndex = lo;
        }
    }

    private void insertionSortForCircSuffixArray(CircularSuffix[] a, int lo, int hi, int d,
                                                 int offset) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d + offset); j--) {
                exchCircSuffixes(a, j, j - 1);
            }
        }
        System.out.printf(
                "Line 90 message - hi: %d lo: %d d: %d offset: %d After Sorting in Insertion sort: \n",
                hi, lo, d, offset);
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                System.out.print((char) a[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
    }

    private void exchCircSuffixes(CircularSuffix[] a, int second, int first) {
        int temp = a[first].start;
        a[first].start = a[second].start;
        a[second].start = temp;
    }

    private boolean less(CircularSuffix c1, CircularSuffix c2, int d) {
        return c1.charAt(d) < c2.charAt(d);
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

    private static void sort(CircularSuffix[] a, int lo, int hi, int d, int offset) {
        count = new int[R + 2];
        char c;

        for (int i = lo; i <= hi; i++) {
            c = (char) a[i].charAt(d + offset);
            // System.out.println(c);
            count[a[i].charAt(d + offset) + 2]++;
        }

        for (int i = 1; i < R; i++) {
            if (count[i] > 0) {
                countFrequencies[countFrequenciesCounter] = count[i];
                countFrequenciesCounter++;
            }
        }

        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        for (int i = lo; i <= hi; i++) {
            // c = (char) a[i].charAt(d);
            aux[count[a[i].charAt(d + offset) + 1]++] = a[i].start;
            // boundris.enqueue(count[a[i].charAt(d)]);
        }
        for (int i = lo; i <= hi; i++) {
            a[i].start = aux[i - lo];
        }
        // updateOffset(lo, hi, d);
        System.out.printf("Line 155 message - hi: %d lo: %d d: %d offset: %d After Sorting: \n", hi,
                          lo, d, offset);
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                System.out.print((char) a[i].charAt(j));
            }
            System.out.println();
        }
        System.out.println();
        offset = 0;
    }

    public static void main(String[] args) {
        String str = "ABRACADABRA!";
        int N = str.length();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(str);
    }
}

