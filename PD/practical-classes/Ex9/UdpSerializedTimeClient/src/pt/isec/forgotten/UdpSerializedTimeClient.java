package pt.isec.forgotten;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;

public class UdpSerializedTimeClient {
    public static final int MAX_SIZE = 1000000;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args) {

        InetAddress serverAddr = null;
        int serverPort = -1;

        ByteArrayInputStream bin;
        ObjectInputStream oin;

        ByteArrayOutputStream bout;
        ObjectOutputStream oout;

        DatagramSocket socket = null;
        DatagramPacket packet = null;
        Calendar response;

        if(args.length != 2){
            System.out.println("Sintaxe: java UdpSerializedTimeClientIncomplete serverAddress serverUdpPort");
            return;
        }

        try{

            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT*1000);

            //Serializar a String TIME para um array de bytes encapsulado por bout
            bout = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(bout);
            oout.writeObject(TIME_REQUEST);

            //Construir um datagrama UDP com o resultado da serialização
            packet = new DatagramPacket(bout.toByteArray(), bout.size(), serverAddr,
                    serverPort);

            socket.send(packet);

            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket.receive(packet);

            //Deserializar o fluxo de bytes recebido para um array de bytes encapsulado por bin
            bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            oin = new ObjectInputStream(bin);
            response = (Calendar)oin.readObject();

            System.out.println("Hora indicada pelo servidor: " + response);

        }catch(Exception e){
            System.out.println("Problema:\n\t"+e);
        }finally{
            if(socket != null){
                socket.close();
            }
        }
    }
}
