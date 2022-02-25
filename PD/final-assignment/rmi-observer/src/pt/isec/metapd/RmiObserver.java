package pt.isec.metapd;

import pt.isec.metapd.resources.MetaPDConstants;
import pt.isec.metapd.rmi.RemoteLbObservableInterface;
import pt.isec.metapd.rmi.RemoteLbObserver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RmiObserver {
    public static final Logger LOGGER = Logger.getLogger(RmiObserver.class.getName());
    private static FileHandler fileHandlerLogger;

    public static void main(String[] args) {
        prepareLogger();
        LOGGER.info("RMI Observer started.");

        if (args.length != 1) {
            LOGGER.log(Level.SEVERE, "Invalid number of arguments! Usage: <rmi service address>");
            return;
        }

        Scanner sysReader = new Scanner(System.in);
        RemoteLbObserver remoteLbObserver = null;
        RemoteLbObservableInterface remoteLbObservable = null;

        try {
            remoteLbObserver = new RemoteLbObserver();
            LOGGER.log(Level.INFO, "RMI service created and running.");

            String objectUrl = "rmi://" + args[0] + "/" + MetaPDConstants.RMI_SERVICE_NAME;
            remoteLbObservable = (RemoteLbObservableInterface) Naming.lookup(objectUrl);

            remoteLbObservable.addObserver(remoteLbObserver);

            String option = "";
            while (!option.equalsIgnoreCase("s")) {
                System.out.println("SERVICE RUNNING! ASYNCHRONOUS NOTIFICATIONS WILL BE PRINTED AUTOMATICALLY");
                System.out.println("OPTIONS:");
                System.out.println("\t'l' -> List all active servers");
                System.out.println("\t's' -> Stop the program");
                System.out.println();

                option = sysReader.nextLine();

                if (option.equalsIgnoreCase("l")) {
                    System.out.println("> START");
                    System.out.println(remoteLbObservable.connectedServersToString());
                    System.out.println("> END");
                }
            }
        } catch(RemoteException e) {
            LOGGER.log(Level.SEVERE, "RMI remote exception: {0}", e.toString());
        } catch(IOException | NotBoundException e) {
            LOGGER.log(Level.SEVERE, "RMI exception: {0}", e.toString());
        } finally {
            if (remoteLbObservable != null) {
                try {
                    remoteLbObservable.removeObserver(remoteLbObserver);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            if (remoteLbObserver != null) {
                try {
                    UnicastRemoteObject.unexportObject(remoteLbObserver, true);
                } catch (NoSuchObjectException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void prepareLogger() {
        SimpleDateFormat format = new SimpleDateFormat("M_d_HHmmss");
        try {
            fileHandlerLogger = new FileHandler("rmi-observer-" + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileHandlerLogger.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandlerLogger);
    }
}
