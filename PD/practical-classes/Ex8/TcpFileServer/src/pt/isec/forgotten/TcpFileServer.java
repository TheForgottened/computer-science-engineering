package pt.isec.forgotten;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TcpFileServer {
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args){

        File localDirectory;
        String requestedFileName, requestedCanonicalFilePath = null;
        FileInputStream requestedFileInputStream = null;


        int listeningPort;
        BufferedReader bin;
        OutputStream out;

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

        listeningPort = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(listeningPort);) {

            if(listeningPort <= 0) throw new NumberFormatException("Porto UDP de escuta indicado <= 0 (" + listeningPort + ")");

            while(true) {
                try (Socket socket = serverSocket.accept()) {
                    socket.setSoTimeout(TIMEOUT * 1000);

                    bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = socket.getOutputStream();

                    requestedFileName = bin.readLine();

                    if (requestedFileName.equalsIgnoreCase("MORRE")) break;

                    requestedCanonicalFilePath = new File(localDirectory + File.separator + requestedFileName).getCanonicalPath();

                    if (!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath() + File.separator)) {
                        System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                        System.out.println("A directoria de base nao corresponde a " + localDirectory.getCanonicalPath() + "!");
                        continue;
                    }

                    requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath);
                    System.out.println("Ficheiro " + requestedCanonicalFilePath + " aberto para leitura.");

                    do {
                        nbytes = requestedFileInputStream.read(fileChunk);

                        if (nbytes == -1) break;

                        out.write(fileChunk, 0, nbytes);
                        out.flush();
                    } while (nbytes > 0);

                    System.out.println("Transferencia concluida");
                } catch (Exception e) {
                    System.out.println("Erro na comunicacao com o cliente atual " + e);
                } finally {
                    if (requestedFileInputStream != null) {
                        requestedFileInputStream.close();
                        requestedFileInputStream = null;
                    }
                }
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
            if (requestedFileInputStream != null) {
                try {
                    requestedFileInputStream.close();
                } catch (IOException ignored) { /* ignored */ }
            }
        } //try
    } // main
}
