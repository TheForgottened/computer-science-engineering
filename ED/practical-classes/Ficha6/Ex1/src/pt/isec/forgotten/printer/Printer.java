package pt.isec.forgotten.printer;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Printer {
    private String name;
    private String brand;
    private String model;
    private int port;

    private final Queue<PrintTask> tasks = new PriorityQueue<>(
            Comparator.comparingInt(PrintTask::totalPages)
    );

    public Printer(
        String name,
        String brand,
        String model,
        int port
    ) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.port = port;
    }

    public boolean hasTask() { return !tasks.isEmpty(); }
    public PrintTask nextTask() { return tasks.poll(); }
    public void addTask(PrintTask printTask) { tasks.add(printTask); }

    public int getTotalTasks() { return tasks.size(); }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getPort() { return port; }

    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setPort(int port) { this.port = port; }
}
