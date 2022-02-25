package pt.isec.forgotten;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TcpTimeClient {
    public static final int MAX_SIZE = 4000;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args) throws IOException {

        InetAddress serverAddr = null;
        int serverPort = -1;

        ObjectInputStream oin = null;
        ObjectOutputStream oout = null;

        Time response;

        if(args.length != 2) {
            System.out.println("Sintaxe: java TcpSerializedTimeClientIncomplete serverAddress serverUdpPort");
            return;
        }

        serverAddr = InetAddress.getByName(args[0]);
        serverPort = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(serverAddr, serverPort)) {
            socket.setSoTimeout(TIMEOUT*1000);

            //Cria os objectos que permitem serializar e deserializar objectos em socket
            System.out.println("AQUI 1");
            oin = new ObjectInputStream(socket.getInputStream());
            oout = new ObjectOutputStream(socket.getOutputStream());

            //Serializa a string TIME_REQUEST para o OutputStream associado a socket
            oout.writeObject(TIME_REQUEST);
            oout.flush();

            //Deserializa a resposta recebida em socket
            response = (Time) oin.readObject();

            if (response == null) {
                System.out.println("O servidor nao enviou qualquer resposta antes de"
                        + " fechar aligacao TCP!");
            } else {
                System.out.println("Hora indicada pelo servidor: " + response);
            }

        } catch(Exception e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }
    }
}
