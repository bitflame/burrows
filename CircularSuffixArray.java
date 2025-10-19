public class CircularSuffixArray {

    private static int R = 256;
    private static int[] count;
    private static int[] aux;
    private static CircularSuffix[] suffixes;
    private static int N;
    private static int offset, countFrequenciesCounter;
    private static int[] countFrequencies;
    private static final int RADIX_SORT_CUTOFF = 12;
    private static int d = 0;
    private static String s;

    public CircularSuffixArray(String str) {
        if (str == null)
            throw new IllegalArgumentException(
                    "The strings passed to this method should not be null.");
        countFrequenciesCounter = 0;
        s = str;
        N = s.length();
        suffixes = new CircularSuffix[s.length()];
        for (int i = 0; i < N; i++) {
            suffixes[i] = new CircularSuffix(i, s);
        }
        int generalCounter = 0; // index for the upcoming loops...
        offset = 0; // tracks the columns of common characters between lo and hi
        aux = new int[N];
        countFrequencies = new int[N];
        int lo = 0, hi = N - 1;
        // d: column being considered from lo: the first string to hi: the last string
        // System.out.printf(
        //         "line 31 messareturn super.toString();ge: lo: %d, hi: %d d: %d offset: %d \n", lo,
        //         hi, d, offset);
        sort(suffixes, lo, hi, d, 0);
        hi = countFrequencies[0] - 1;
        if (hi > lo) sortMatchingStrings(lo, hi, d);
        while (countFrequencies[generalCounter] != 0) {
            lo = lo + countFrequencies[generalCounter];
            hi = lo + countFrequencies[generalCounter + 1] - 1;
            generalCounter++;
            if (hi > lo)
                sortMatchingStrings(lo, hi, d);
        }
    }

    private static int[] arrayResize(int[] countFreq, int currentSize, int newSize) {
        int[] newCountFreqArray = new int[newSize];
        for (int i = 0; i < countFreq.length; i++) {
            newCountFreqArray[i] = countFreq[i];
        }
        return newCountFreqArray;
    }

    private void sortMatchingStrings(int lo, int hi, int d) {
        // returns the values for offset, matchingLo, matchingHi, and count of the
        // region with matching characters
        // for the current column referred to by d
        int startOfMatch = lo, endOfMatch = lo, currentIndex = lo, localOffset
                = 0; // matching hi starts at lo and
        // increascountFrequencieses
        offset = 0;
        boolean foundRowsWithMatchingCharacters = true;
        currentIndex = lo;
        while (foundRowsWithMatchingCharacters) {
            foundRowsWithMatchingCharacters = false;
            while (currentIndex < hi) {
                if (currentIndex <= hi
                        && suffixes[currentIndex].charAt(offset + d) == suffixes[currentIndex + 1]
                        .charAt(offset + d)) {
                    startOfMatch = currentIndex;
                    foundRowsWithMatchingCharacters = true;
                    while (currentIndex < hi
                            && suffixes[currentIndex].charAt(offset + d) == suffixes[currentIndex
                            + 1]
                            .charAt(offset + d)) {
                        currentIndex++;
                    }
                    localOffset = offset + 1;
                    endOfMatch = currentIndex;
                    int temp = startOfMatch;
                    // can I add another col? temp is just a temporary variable poiting to each line
                    // instead of currentIndex
                    while (suffixes[temp].charAt(d + localOffset) == suffixes[temp + 1]
                            .charAt(d + localOffset) && temp <= endOfMatch) {
                        while (temp < endOfMatch
                                && suffixes[temp].charAt(d + localOffset) == suffixes[temp
                                + 1].charAt(d + localOffset)) {
                            temp++;
                        }

                        if (temp == endOfMatch
                                && suffixes[startOfMatch].charAt(d + localOffset) == suffixes[
                                startOfMatch + 1]
                                .charAt(d + localOffset)) {
                            temp = startOfMatch;
                            localOffset++;
                        }

                    }
                    offset++;
                    if (localOffset > offset)
                        offset = localOffset;
                    if (startOfMatch - endOfMatch > RADIX_SORT_CUTOFF) {
                        sort(suffixes, startOfMatch, endOfMatch, d, offset);
                        foundRowsWithMatchingCharacters = false;
                    }
                    else {
                        insertionSortForCircSuffixArray(suffixes, startOfMatch, endOfMatch, d,
                                                        offset);
                        foundRowsWithMatchingCharacters = false;
                    }
                }
                currentIndex++;
            }
            currentIndex = lo;
            offset++;
        }
    }

    private void insertionSortForCircSuffixArray(CircularSuffix[] a, int lo, int hi, int d,
                                                 int offset) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d + offset); j--) {
                exchCircSuffixes(a, j, j - 1);
            }
        }
        // System.out.printf(
        //         "Line 126 message - lo: %d hi: %d d: %d offset: %d After Sorting in Insertion sort: \n",
        //         lo, hi, d, offset);
        // for (int i = 0; i < a.length; i++) {
        //     System.out.printf("%d ", a[i].start);
        //     for (int j = 0; j < a.length; j++) {
        //         System.out.print((char) a[i].charAt(j));
        //     }
        //     System.out.println();
        // }
        // System.out.println();
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

    private static void sort(CircularSuffix[] a, int lo, int hi, int d, int offset) {
        count = new int[R + 2];
        // char c;

        for (int i = lo; i <= hi; i++) {
            // c = (char) a[i].charAt(d + offset);
            // System.out.println(c);
            count[a[i].charAt(d + offset) + 2]++;
        }

        for (int i = 1; i < R; i++) {
            if (count[i] > 0) {
                countFrequencies[countFrequenciesCounter] = count[i];
                countFrequenciesCounter++;
                if (countFrequenciesCounter * 2 > countFrequencies.length)
                    countFrequencies = arrayResize(countFrequencies, countFrequencies.length,
                                                   countFrequencies.length * 2);
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
        // System.out.printf("Line 210 message - hi: %d lo: %d d: %d offset: %d After Sorting: \n", hi,
        //         lo, d, offset);
        // for (int i = 0; i < a.length; i++) {
        //     System.out.printf("Start of this suffix: %d ", a[i].start);
        //     for (int j = 0; j < a.length; j++) {
        //         System.out.print((char) a[i].charAt(j));

        //     }
        //     System.out.println();
        // }
        // System.out.println();
        offset = 0;
    }

    public static void main(String[] args) {
        System.out.println("Processing String 1");
        String shortString = "ABC";
        CircularSuffixArray csaShort = new CircularSuffixArray(shortString);
        for (int i = 0; i < shortString.length(); i++) {
            System.out.println(csaShort.index(i));
        }
        System.out.println("Processing String 2");
        String str = "ABRACADABRA!";
        int N = str.length();// change str to ARD!RCAAAABB and see if it sorted
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(circularSuffixArray.length());

        for (int i = 0; i < N; i++) {
            System.out.println(circularSuffixArray.index(i));
        }

        System.out.println("Processing String 3");
        str = "BANANA";
        circularSuffixArray = new CircularSuffixArray(str);
        System.out.println(circularSuffixArray.index(0));
        System.out.println();
        System.out.println("Processing String 4");
        str = "ARD!RCAAAABB";
        circularSuffixArray = new CircularSuffixArray(str);
    }
}

