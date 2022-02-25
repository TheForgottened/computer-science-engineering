package pt.isec.forgotten;

import java.io.IOException;
import java.net.*;

public class UdpTimeClient {

    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; // segundos

    public static void main(String[] args) {

        InetAddress serverAddr;
        int serverPort;

        DatagramPacket packet;
        String response = "";

        if (args.length != 2) {
            System.out.println("Sintaxe: java UdpTimeClient serverAddress serverUdpPort");
            return;
        }

        try (DatagramSocket mySocket = new DatagramSocket()) {
            mySocket.setSoTimeout(TIMEOUT * 1000);

            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            packet = new DatagramPacket(TIME_REQUEST.getBytes(), TIME_REQUEST.length(), serverAddr, serverPort);
            mySocket.send(packet);

            byte[] buffer = new byte[MAX_SIZE];
            packet = new DatagramPacket(buffer, MAX_SIZE);
            mySocket.receive(packet);
            response = new String(packet.getData(), 0, packet.getLength());
        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        }

        System.out.println("Resposta do servidor: " + response);
    }
}
