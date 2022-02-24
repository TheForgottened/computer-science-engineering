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

    private static float percentageLowerFromBinarySearch(int result, int length) {
        if (result < 0) result = Math.abs(result + 1);
        return (result / (float) length) * 100;
    }

    public static void main(String[] args) {
        int[] ar = {1, 3, 4, 5, 7, 8};
        int key = 5;

        int ansIterative = binIterative(ar, key);
        System.out.println("Percentage Iterative: " + percentageLowerFromBinarySearch(ansIterative, ar.length) + "%");

        int ansRecursive = binRecursive(ar, key);
        System.out.println("Percentage Recursive: " + percentageLowerFromBinarySearch(ansRecursive, ar.length) + "%");

        int ansJava = Arrays.binarySearch(ar, key);
        System.out.println("Percentage Java Imp.: " + percentageLowerFromBinarySearch(ansJava, ar.length) + "%");
    }
}
