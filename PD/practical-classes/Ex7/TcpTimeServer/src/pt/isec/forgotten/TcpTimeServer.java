package pt.isec.forgotten;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.nio.Buffer;
import java.util.Calendar;

public class TcpTimeServer {
    public static final String TIME_REQUEST = "TIME";

    public static void main(String[] args) {
        int listeningPort;

        String receivedMsg = "";
        String timeMsg = "";
        Calendar calendar;
        if (args.length != 1) {
            System.out.println("Sintaxe: java TcpTimeServer listeningPort");
            return;
        }

        listeningPort = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
            System.out.println("TCP Time Server iniciado...");

            BufferedReader bufferedReaderIn;
            PrintStream printStreamOut;

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    bufferedReaderIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    receivedMsg = bufferedReaderIn.readLine();

                    if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                        return;
                    }

                    System.out.println("Conexão: " + socket.getInetAddress().getHostName() + ":" + socket.getPort() + " >> " + receivedMsg);

                    calendar = Calendar.getInstance();
                    timeMsg = calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                            calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);

                    printStreamOut = new PrintStream(socket.getOutputStream(), true);
                    printStreamOut.println(timeMsg);
                } catch (Exception e) {
                    System.out.println(e);
                    return;
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        }
    }
}
