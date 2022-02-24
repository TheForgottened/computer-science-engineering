package pt.isec.forgotten.printer;

import java.util.*;

public class QueuePrinterManager {
    private final Queue<Printer> printers = new PriorityQueue<>(
            (o1, o2) -> {
                int differenceInTasks = o2.getTotalTasks() - o1.getTotalTasks();

                if (differenceInTasks != 0) return differenceInTasks;

                return 0;
            }
    );

    public void print(PrintTask printTask) {
        Printer printer = printers.poll();
        if (printer == null) return;

        printer.addTask(printTask);
        printers.add(printer);
    }

    public void addPrinter(Printer printer) { printers.add(printer); }
    public void removePrinterByName(String name) {
        for (Printer printer : printers) {
            if (printer.getName().equals(name)) {
                printers.remove(printer);
                return;
            }
        }
    }

    public boolean printerExists(String name) {
        for (Printer printer : printers) {
            if (printer.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
}
