package pt.isec.metapd;

import pt.isec.metapd.concurrentlb.ConcurrentLb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoadBalancer {
    public static final Logger LOGGER = Logger.getLogger(LoadBalancer.class.getName());
    private static FileHandler fileHandlerLogger;

    private static final AtomicBoolean mustStop = new AtomicBoolean(false);
    private static final int udpPort = 2420;

    public static void main(String[] args) {
        prepareLogger();
        LOGGER.info("Load balancer started.");

        ConcurrentLb concurrentLb = new ConcurrentLb(mustStop, udpPort);
        concurrentLb.start();
    }

    private static void prepareLogger() {
        SimpleDateFormat format = new SimpleDateFormat("M_d_HHmmss");
        try {
            fileHandlerLogger = new FileHandler("lb-" + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileHandlerLogger.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandlerLogger);
    }
}
