package pt.isec.forgotten;

import java.io.IOException;
import java.util.Arrays;

public class binarySearch {
    private static boolean binRecursive(int[] array, int key) {
        int mid = array.length / 2;

        if (array[mid] == key) return true;
        if (array.length == 1) return false;

        int[] newArray;

        if (key < array[mid]) {
            newArray = Arrays.copyOfRange(array, 0, mid);
        } else {
            newArray = Arrays.copyOfRange(array, mid, array.length);
        }

        return binRecursive(newArray, key);
    }

    public static void main(String[] args) {
        int[] ar = {1, 3, 4, 5, 6, 7, 8};

        boolean ans = binRecursive(ar, 6);

        if (ans) System.out.println("Existe");
        else System.out.println("Não existe");
    }
}
