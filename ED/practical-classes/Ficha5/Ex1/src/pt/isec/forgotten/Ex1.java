package pt.isec.forgotten;

import java.util.*;

public class Ex1 {
    static long NUMBER_RUNS = 20;
    static long BASE_NUMBER = 50000;

    public static void main(String[] args) {
        List arrayList = new ArrayList();
        List linkedList = new LinkedList();

        Pilha pArrayList = new Pilha(arrayList);
        Pilha pLinkedList = new Pilha(linkedList);

        /*
        System.out.println("N\t| ArrayList | Linked List");

        for (int i = 1; i <= 30; i++) {
            long n = BASE_NUMBER * i;
            double time = 0;

            for (int j = 0; j < NUMBER_RUNS; j++) time += testaPilha(pArrayList, n);
            System.out.printf("%d\t| %9.2f\t| ", i, (float) time / NUMBER_RUNS);

            time = 0;

            for (int j = 0; j < NUMBER_RUNS; j++) time += testaPilha(pLinkedList, n);
            System.out.printf("%.2f\n", (float) time / NUMBER_RUNS);
        }
        */

        for (int i = 0; i < 10; i++) arrayList.add(i);
        System.out.println(arrayList);
        revertList(arrayList);
        System.out.println(arrayList);
    }

    public static <T> void myFill(List<? super T> list, T o) {
        ListIterator it = list.listIterator();

        while (it.hasNext()) {
            it.next();
            it.set(o);
        }
    }

    public static void printBackwards(List<?> list) {
        ListIterator it = list.listIterator(list.size());

        while (it.hasPrevious()) {
            System.out.println(it.previous());
        }
    }

    public static <T> void printReverse(Collection<T> collection) {
        Pilha<T> pilha = new Pilha<>(new ArrayList<T>());

        for (T valor : collection) pilha.push(valor);
        while (!pilha.empty()) System.out.println(pilha.pop());
    }

    public static void revertList(List<?> list) {
        ListIterator itBegin = list.listIterator();
        ListIterator itEnd = list.listIterator(list.size());

        for (int i = 0, mid = list.size() / 2; i < mid; i++) {
            Object temp = itBegin.next();
            itBegin.set(itEnd.previous());
            itEnd.set(temp);
        }
    }

    public static <T> void removeEven(List<T> list) {
        List<T> evenObj = new ArrayList<>(list.size() / 2 + 1);
        ListIterator it = list.listIterator();

        while (it.hasNext()) {
            it.next();

            if (it.hasNext()) {
                evenObj.add((T) it.next());
            }
        }

        list.clear();
        list.addAll(evenObj);
    }

    public static <T> void removeEvenQuicker(List<T> list) {
        ListIterator it = list.listIterator();

        while (it.hasNext()) {
            it.next();
            it.remove();

            if (it.hasNext()) {
                it.next();
            }
        }
    }

    public static double testaPilha(Pilha p, long sz) {
        long startTime = System.nanoTime();

        for (int i = 0; i < sz; i++) p.push(i);
        for (int i = 0; i < sz; i++) p.pop();

        return (System.nanoTime() - startTime) / 1000000.0;
    }
}
