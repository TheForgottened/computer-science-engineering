package pt.isec.forgotten;

import pt.isec.forgotten.figures.Figure;
import pt.isec.forgotten.figures.Rectangle;

public class Ex3 {
    public static void main(String[] args) {
        Figure nozes = new Rectangle(10, 5);
        Figure castanhas = new Rectangle(10, 10);

        System.out.println(nozes.compareTo(castanhas));
    }

    public static int ex3b(Rectangle rectangle, Comparable<? super Rectangle> comparable) {
        return comparable.compareTo(rectangle);
    }

    public static <T> int ex3c(T t, Comparable<? super T> comparable) {
        return comparable.compareTo(t);
    }
}
