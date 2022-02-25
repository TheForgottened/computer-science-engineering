package pt.isec.forgotten;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Calendar;

public class UdpTimeServer {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";

    public static void main(String[] args) {
        int listeningPort;

        DatagramPacket packet; //para receber os pedidos e enviar as respostas
        String receivedMsg, timeMsg;
        Calendar calendar;
        if (args.length != 1) {
            System.out.println("Sintaxe: java UdpTimeServer_v2 listeningPort");
            return;
        }

        listeningPort = Integer.parseInt(args[0]);

        try (DatagramSocket socket = new DatagramSocket(listeningPort)) {
            System.out.println("UDP Time Server iniciado...");

            byte[] buffer = new byte[MAX_SIZE];

            while (true) {
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                receivedMsg = new String(buffer, 0, packet.getLength());

                System.out.println("Recebido \"" + receivedMsg + "\" de " +
                        packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                    return;
                }

                calendar = Calendar.getInstance();
                timeMsg = calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);

                InetAddress clientAddr = packet.getAddress();
                int clientPort = packet.getPort();
                packet = new DatagramPacket(timeMsg.getBytes(), timeMsg.length(), clientAddr, clientPort);
                socket.send(packet);
            }

        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        }
    }
}
