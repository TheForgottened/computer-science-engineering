package pt.isec.metapd.concurrentlb.runnables;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import static pt.isec.metapd.LoadBalancer.LOGGER;

public class CommandsInterpreter implements Runnable {
    private final AtomicBoolean mustStop;

    public CommandsInterpreter(AtomicBoolean mustStop) {
        this.mustStop = mustStop;
    }

    @Override
    public void run() {
        Scanner sysReader = new Scanner(System.in);

        LOGGER.info("CommandInterpreter thread started.");

        while (!mustStop.get()) {
            try {
                if (System.in.available() == 0) {
                    continue;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString());
            }
            String command = sysReader.nextLine();

            if (command.equalsIgnoreCase("stop")) {
                mustStop.set(true);
                LOGGER.log(Level.INFO, "Server is now shutting down.");
            } else {
                System.err.println("Invalid command!");
            }
        }
    }
}
