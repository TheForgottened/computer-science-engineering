package pt.isec.forgotten;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UdpSerializedTimeServer {
    public static final int MAX_SIZE = 1000000;
    public static final String TIME_REQUEST = "TIME";

    public static void main(String[] args) {
        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet; //para receber os pedidos e enviar as respostas

        ByteArrayInputStream bin;
        ObjectInputStream oin;

        ByteArrayOutputStream bout;
        ObjectOutputStream oout;

        String receivedMsg;
        Calendar calendar;

        if (args.length != 1) {
            System.out.println("Sintaxe: java UdpSerializedTimeServerIncomplete listeningPort");
            return;
        }

        try {

            listeningPort = Integer.parseInt(args[0]);
            socket = new DatagramSocket(listeningPort);

            System.out.println("UDP Time Server iniciado...");

            while(true){
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);

                //Deserializar os bytes recebidos (objecto do tipo String)
                bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                oin = new ObjectInputStream(bin);
                receivedMsg = (String)oin.readObject();

                System.out.println("Recebido \"" + receivedMsg + "\" de " +
                        packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if(!receivedMsg.equalsIgnoreCase(TIME_REQUEST)){
                    continue;
                }

                calendar = GregorianCalendar.getInstance();

                //Serializar o objecto calendar para bout
                bout = new ByteArrayOutputStream();
                oout = new ObjectOutputStream(bout);
                oout.writeObject(calendar);

                packet.setData(bout.toByteArray());
                packet.setLength(bout.size());

                //O ip e porto de destino ja' se encontram definidos em packet
                socket.send(packet);
            }

        } catch(Exception e) {
            System.out.println("Problema:\n\t"+e);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
