package pt.isec.forgotten;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeoutException;

public class TcpConcurrentTimeServer {
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 5; // seconds

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Sintaxe: java TcpConcurrentTimeServer listeningPort");
            return;
        }

        int listeningPort = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
            System.out.println("Server initialized successfully. Port used is " + listeningPort + ".");

            while (true) {
                try {
                    Socket toClientSocket = serverSocket.accept();
                    toClientSocket.setSoTimeout(TIMEOUT * 1000);
                    Thread clientThread = new Thread(
                            new RunnableClientThread(toClientSocket),
                            toClientSocket.getInetAddress().toString()
                    );
                    clientThread.start();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    static class RunnableClientThread implements Runnable {
        private Socket socket;

        RunnableClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Client " + socket.getInetAddress() + ":" + socket.getPort());
            try (
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
            ) {
                String request = (String) objectInputStream.readObject();

                if (request == null) return;

                Calendar calendar = GregorianCalendar.getInstance();
                switch (request) {
                    case TIME_REQUEST -> {
                        objectOutputStream.writeObject(new Time(
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                calendar.get(Calendar.SECOND)
                        ));
                        objectOutputStream.flush();
                    }
                    default -> System.out.println("UNEXPECTED");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }

            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }
}
