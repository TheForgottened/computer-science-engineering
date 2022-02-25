
import java.util.*;
import java.net.*;
import java.io.*;

public class TcpSerializedTimeServerIncomplete {

    public static final String TIME_REQUEST = "TIME";
    
    public static void main(String args[])
    {
        ServerSocket socket;       
        int listeningPort;        
        socket = null;
        Socket toClientSocket;
        
        ObjectInputStream oin;
        ObjectOutputStream oout;
        
        String request;
        Calendar calendar;     
        
        if(args.length != 1){
            System.out.println("Sintaxe: java TcpSerializedTimeServerIncomplete listeningPort");
            return;
        }
        
        try{
            
            listeningPort = Integer.parseInt(args[0]);
            socket = new ServerSocket(listeningPort);                    

            System.out.println("TCP Time Server iniciado no porto " + socket.getLocalPort() + " ...");

            while(true){     
                
                toClientSocket = socket.accept();        

                try{
                    oout = ...;
                    oin = ...;

                    request = ...;

                    if(request == null){ //EOF
                        continue; //to next client request
                    }

                    System.out.println("Recebido \"" + request.trim() + "\" de " + 
                            toClientSocket.getInetAddress().getHostAddress() + ":" + 
                            toClientSocket.getPort());

                    if(!request.equalsIgnoreCase(TIME_REQUEST)){
                        System.out.println("Unexpected request");
                        continue;
                    }
                    
                    calendar = GregorianCalendar.getInstance();
                    
                    oout....;
                    oout.flush();

                }catch(Exception e){
                    System.out.println("Problema na comunicacao com o cliente " + 
                            toClientSocket.getInetAddress().getHostAddress() + ":" + 
                                toClientSocket.getPort()+"\n\t" + e);
                }finally{
                    try{
                        toClientSocket.close();
                    }catch(IOException e){}
                }
            }
            
        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(IOException e){
            System.out.println("Ocorreu um erro ao nivel do socket de escuta:\n\t"+e);
        }finally{
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException ex) {}
            }
        }
    }
           
}

