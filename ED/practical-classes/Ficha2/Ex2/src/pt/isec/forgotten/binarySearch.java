package pt.isec.forgotten;

import java.util.Arrays;

public class binarySearch {
    private static boolean binIterative(int[] array, int key) {
        int mid;
        int start = 0;
        int end = array.length;

        while (start < end) {
            mid = (start + end) / 2;

            if (key == array[mid]) return true;
            if (end - start == 1) break;

            if (key < array[mid]) {
                end = mid + 1;
            } else {
                start = mid - 1;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[] ar = {1, 3, 4, 5, 6, 7, 8};

        boolean ans = binIterative(ar, 7);

        if (ans) System.out.println("Existe");
        else System.out.println("Não existe");
    }
}
