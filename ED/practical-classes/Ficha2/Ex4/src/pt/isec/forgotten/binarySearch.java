package pt.isec.forgotten;

import java.util.Arrays;

public class binarySearch {
    private static int binRecursive(int[] array, int key) {
        return binRecursive(array, key, 0, array.length - 1);
    }

    private static int binRecursive(int[] array, int key, int start, int end) {
        int mid = (start + end) / 2;

        if (array[mid] == key) return mid;
        if (end < start) return - (start + 1);

        if (key < array[mid]) return binRecursive(array, key, start, mid - 1);

        return binRecursive(array, key, mid + 1, end);
    }

    private static int binIterative(int[] array, int key) {
        int mid;
        int start = 0;
        int end = array.length - 1;

        while (start <= end) {
            mid = (start + end) / 2;

            if (key == array[mid]) return mid;
            if (end - start == 1) break;

            if (key < array[mid]) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }

        return - (start + 1);
    }

    public static void main(String[] args) {
        int[] ar = {1, 3, 4, 5, 7, 8};
        int key = 6;

        int ansIterative = binIterative(ar, key);
        System.out.println("Index Iterative: " + ansIterative + " (if -1 the number doesn't exist)");

        int ansRecursive = binRecursive(ar, key);
        System.out.println("Index Recursive: " + ansRecursive + " (if -1 the number doesn't exist)");

        System.out.println(Arrays.binarySearch(ar, key));
    }
}
