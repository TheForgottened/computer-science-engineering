package pt.isec.forgotten.printer;

import java.util.*;

public class MapPrinterManager {
    private final Map<String, Printer> map = new HashMap<>();

    public Printer getPrinter(String name) { return map.get(name); }
    public Set<String> getPrinterNames() { return map.keySet(); }

    public void addPrinter(Printer printer) { map.put(printer.getName(), printer); }
    public void removePrinterByName(String name) { map.remove(name); }

    public boolean printerExists(String name) { return map.containsKey(name); }
}
