package pt.isec.forgotten;

import java.util.Arrays;

public class binarySearch {
    private static int binRecursive(int[] array, int key, int start, int end) {
        int mid = (start + end) / 2;

        if (array[mid] == key) return mid;
        if (array.length == 1) return -1;

        if (key < array[mid]) return binRecursive(array, key, start, mid + 1);

        return binRecursive(array, key, mid - 1, end);
    }

    private static int binIterative(int[] array, int key) {
        int mid;
        int start = 0;
        int end = array.length;

        while (start < end) {
            mid = (start + end) / 2;

            if (key == array[mid]) return mid;
            if (end - start == 1) break;

            if (key < array[mid]) {
                end = mid + 1;
            } else {
                start = mid - 1;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] ar = {1, 3, 4, 5, 6, 7, 8};

        int ansIterative = binIterative(ar, 7);
        int ansRecursive = binRecursive(ar, 7, 0, ar.length);

        System.out.println("Index Iterative: " + ansIterative + " (if -1 the number doesn't exist)");
        System.out.println("Index Recursive: " + ansRecursive + " (if -1 the number doesn't exist)");
    }
}
