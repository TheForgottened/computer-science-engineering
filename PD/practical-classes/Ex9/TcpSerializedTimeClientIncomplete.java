
import java.net.*;
import java.io.*;
import java.util.*;

public class TcpSerializedTimeClientIncomplete {

    public static final int MAX_SIZE = 4000;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args) throws IOException
    {
        
        InetAddress serverAddr = null;
        int serverPort = -1;
        Socket socket = null;
                
        ObjectInputStream oin = null;
        ObjectOutputStream oout = null;
        
        Calendar response;
        
        if(args.length != 2){
            System.out.println("Sintaxe: java TcpSerializedTimeClientIncomplete serverAddress serverUdpPort");
            return;
        }

        try{

            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);   
            
            socket = new Socket(serverAddr, serverPort);
            socket.setSoTimeout(TIMEOUT*1000);
            
            //Cria os objectos que permitem serializar e deserializar objectos em socket
            oin = ...;
            oout = ...;
            
            //Serializa a string TIME_REQUEST para o OutputStream associado a socket
            oout...;
            oout.flush();
            
            //Deserializa a resposta recebida em socket
            response = oin....;
            
            if(response == null){
                System.out.println("O servidor nao enviou qualquer respota antes de"
                        + " fechar aligacao TCP!");   
            }else{                
                System.out.println("Hora indicada pelo servidor: " + response);                
            }
            
        }catch(Exception e){
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }finally{
            if(socket != null){
                socket.close();
            }
        }
   }
  
}
