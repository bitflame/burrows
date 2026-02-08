public class CircularSuffix_MSD {
    private static int start;
    private static int R = 256;

    private static String str = "ABRACADABRA!";
    private static String[] aux;

    private class CircularSuffix {
        int start;

        CircularSuffix(int start) {
            this.start = start;
        }

        int charAt(int i) {
            return i > str.length() ? -1 : str.charAt((start + i) % str.length());
        }
    }

    // modified MSD to take a starting position
    public static void sort(String[] a, int start) {
        int N = a.length;
        aux = new String[N];
        sort(a, 0, N - 1, 0);
    }

    private static void sort(String[] a, int lo, int hi, int d) {
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            count[charAt(i) + 2]++;
        }
        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }
        for (int i = lo; i <= hi; i++) {
            aux[count[charAt(i) + 1]++] = a[i];
        }

        for (int i = lo; i <= hi; i++) {
            a[i] = aux[i - lo];
        }
        // There is a ommitted recursive call below in the text
    }


    private static int charAt(int i) {
        return i >= str.length() ? -1 : str.charAt((start + i) % str.length());
    }


    CircularSuffix_MSD(int start) {
        this.start = start;
    }

    public static void main(String[] args) {
        CircularSuffix_MSD[] suffixes = new CircularSuffix_MSD[12];
        String a = "ABRACADABRA!";
        for (int i = 0; i < a.length(); i++) {
            suffixes[i] = new CircularSuffix_MSD(i);
            System.out.println(suffixes[i].charAt(0));
        }
    }
}
