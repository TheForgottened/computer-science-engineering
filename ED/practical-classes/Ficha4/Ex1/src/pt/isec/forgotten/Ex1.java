package pt.isec.forgotten;

import pt.isec.forgotten.reais.dezreais.DezReais;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Ex1 {
    public static void main(String[] args) {
        DezReais dezReais = new DezReais();
        ArrayList<Double> d = new ArrayList<>();
        d.add(0.1);
        d.add(2.0);
        d.add(3.0);

        Iterator<Double> bruh = d.iterator();

        System.out.println("===");
        while (bruh.hasNext()) {
            System.out.println(bruh.next());
        }
        System.out.println("===");

        d.iterator();

        dezReais.add(1.0, 2.0, 1000000, 1000, 5.5, 6.9, -1, -2, 10);

        Iterator<Double> a = dezReais.iterator();

        while (a.hasNext()) {
            System.out.println(a.next());
        }

        a = dezReais.iterator();
        a.next();
        a.next();
        a.remove();

        a = dezReais.iterator();
        while (a.hasNext()) {
            System.out.println(a.next());
        }

        Iterator<Double> b = dezReais.iterator();

        System.out.println("----");
        while (b.hasNext()) {
            System.out.println(b.next());
        }
        System.out.println("----");

        System.out.println(bigger(dezReais));
    }

    private static <T extends Comparable<? super T>> T bigger (Iterable<T> d) {
        Iterator<T> it = d.iterator();
        T max = it.next();
        T temp;

        while (it.hasNext()) {
            temp = it.next();
            if (temp.compareTo(max) > 0) max = temp;
        }

        return max;
    }
}
