package pt.isec.metapd;

import pt.isec.metapd.concurrentserver.ConcurrentServer;
import pt.isec.metapd.repository.database.DbConnectionHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {
    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static FileHandler fileHandlerLogger;

    private static final AtomicBoolean mustStop = new AtomicBoolean(false);

    public static void main(String[] args) {
        prepareLogger();

        if (args.length != 3) {
            LOGGER.log(Level.SEVERE, "Invalid number of arguments! Usage: <lb address> <lb port> <dbms address>");
            return;
        }

        try {
            Class.forName(DbConnectionHelper.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "JDBC connection driver not found.\n{0}", e.toString());
            return;
        }

        LOGGER.log(Level.INFO, "Server started.");

        ConcurrentServer concurrentServer = new ConcurrentServer(
                mustStop,
                args[0],
                Integer.parseInt(args[1]),
                args[2]
        );
        concurrentServer.start();
    }

    private static void prepareLogger() {
        SimpleDateFormat format = new SimpleDateFormat("M_d_HHmmss");
        try {
            fileHandlerLogger = new FileHandler("server-" + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileHandlerLogger.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandlerLogger);
    }
}
