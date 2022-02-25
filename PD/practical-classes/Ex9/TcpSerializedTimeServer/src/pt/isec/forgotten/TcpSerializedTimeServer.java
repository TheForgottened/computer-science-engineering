package pt.isec.forgotten;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TcpSerializedTimeServer {
    public static final String TIME_REQUEST = "TIME";

    public static void main(String args[]) {
        int listeningPort;

        ObjectInputStream oin;
        ObjectOutputStream oout;

        String request;
        Calendar calendar;

        if (args.length != 1) {
            System.out.println("Sintaxe: java TcpSerializedTimeServerIncomplete listeningPort");
            return;
        }

        listeningPort = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
            System.out.println("TCP Time Server iniciado no porto " + serverSocket.getLocalPort() + " ...");

            while(true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    oout = new ObjectOutputStream(clientSocket.getOutputStream());
                    oin = new ObjectInputStream(clientSocket.getInputStream());

                    request = (String) oin.readObject();

                    if(request == null) { //EOF
                        continue; //to next client request
                    }

                    System.out.println("Recebido \"" + request.trim() + "\" de " +
                            clientSocket.getInetAddress().getHostAddress() + ":" +
                            clientSocket.getPort());

                    if (!request.equalsIgnoreCase(TIME_REQUEST)) {
                        System.out.println("Unexpected request");
                        continue;
                    }

                    calendar = GregorianCalendar.getInstance();

                    oout.writeObject(calendar);
                    oout.flush();

                } catch(Exception e) {
                    System.out.println("Problema na comunicacao com o cliente " + e);
                }
            }

        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(IOException e){
            System.out.println("Ocorreu um erro ao nivel do socket de escuta:\n\t"+e);
        }
    }
}
