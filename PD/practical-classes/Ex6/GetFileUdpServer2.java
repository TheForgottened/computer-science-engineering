import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author Jose' Marinho
 */
public class GetFileUdpServer2 {

    public static final int MAX_SIZE = 4000;    
    
    public static void main(String[] args){
        
        File localDirectory;
        String requestedFileName, requestedCanonicalFilePath = null;
        FileInputStream requestedFileInputStream;
        
        DatagramSocket socket = null;
        int listeningPort;
        DatagramPacket packet;
        
        byte []fileChunk = new byte[MAX_SIZE];
        int nbytes;
        
        if(args.length != 2){
            System.out.println("Sintaxe: java GetFileUdpServer listeningPort localRootDirectory");
            return;
        }        
        
        localDirectory = new File(args[1].trim());

        if(!localDirectory.exists()){
           System.out.println("A directoria " + localDirectory + " nao existe!");
           return;
       }

       if(!localDirectory.isDirectory()){
           System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
           return;
       }

       if(!localDirectory.canRead()){
           System.out.println("Sem permissoes de leitura na directoria " + localDirectory + "!");
           return;
       }
        
       try {
           
            listeningPort = Integer.parseInt(args[0]);
            if(listeningPort <= 0) throw new NumberFormatException("Porto UDP de escuta indicado <= 0 (" + listeningPort + ")");
                        
            socket = new DatagramSocket(listeningPort);
            
            while(true){
                
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);                    
                socket.receive(packet);
                    
                requestedFileName = new String(packet.getData(), 0, packet.getLength()).trim();

                System.out.println("Recebido pedido para \"" + requestedFileName + "\" de " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

                requestedCanonicalFilePath = new File(localDirectory+File.separator+requestedFileName).getCanonicalPath();

                if(!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath()+File.separator)){
                    System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                    System.out.println("A directoria de base nao corresponde a " + localDirectory.getCanonicalPath()+"!");
                    continue;
                }

                requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath);
                System.out.println("Ficheiro " + requestedCanonicalFilePath + " aberto para leitura.");

                do{
                    nbytes = requestedFileInputStream.read(fileChunk);

                    if(nbytes == -1){//EOF
                        nbytes = 0;
                    }

                    packet.setData(fileChunk, 0, nbytes);
                    packet.setLength(nbytes);

                    socket.send(packet);   

                    /*try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {}*/                    
                   
                }while(nbytes > 0);     

                System.out.println("Transferencia concluida");

                requestedFileInputStream.close();
            }
            
        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo:\n\t"+e);
        }catch(SocketException e){
            System.out.println("Ocorreu uma excepcao ao nivel do socket UDP:\n\t"+e);
        }catch(FileNotFoundException e){   //Subclasse de IOException                 
            System.out.println("Ocorreu a excepcao {" + e + "} ao tentar abrir o ficheiro " + requestedCanonicalFilePath + "!");              
        }catch(IOException e){
            System.out.println("Ocorreu a excepcao de E/S: \n\t" + e);
        }finally{
            if(socket != null){
                socket.close();
            }
        } //try
       
    } // main    
}
