package pt.isec.forgotten;

public class Ex4 {
    public static void main(String[] args) {

    }

    public static <T> boolean biggerInArray(T[] tArray, Comparable<? super T> key) {
        for (T element : tArray) {
            if (key.compareTo(element) < 0) return true;
        }

        return false;
    }
}
