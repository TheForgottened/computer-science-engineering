package pt.isec.forgotten;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

public class TcpTimeClient {

    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; // segundos

    public static void main(String[] args) {
        String response = "";

        if (args.length != 2) {
            System.out.println("Sintaxe: java TcpTimeClient serverAddress serverTcpPort");
            return;
        }

        try (Socket socket = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]))) {
            socket.setSoTimeout(TIMEOUT * 1000);

            PrintStream printStreamOut = new PrintStream(socket.getOutputStream(), true);
            printStreamOut.println(TIME_REQUEST);

            BufferedReader bufferedReaderIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            response = bufferedReaderIn.readLine();
        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        }

        System.out.println("Resposta do servidor: " + response);
    }
}
