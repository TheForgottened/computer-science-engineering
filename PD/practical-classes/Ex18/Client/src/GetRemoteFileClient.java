/*
 * Exemplo de utilizacao do servico com interface remota GetRemoteFileInterface.
 * Assume-se que o servico encontra-se registado sob o nome "servidor-ficheiros-pd".
 * Esta classe tambem implementa uma interface remota (GetRemoteFileClientInterface)
 * que deve incluir o metodo:
 *
 *       void writeFileChunk(byte [] fileChunk, int nbytes) throws java.io.IOException
 *
 */
import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Jose'
 */
public class GetRemoteFileClient extends UnicastRemoteObject implements GetRemoteFileClientInterface
{

    FileOutputStream fout = null;

    public GetRemoteFileClient() throws RemoteException {}

    public synchronized void setFout(FileOutputStream fout) {
        this.fout = fout;
    }

    @Override
    public synchronized void writeFileChunk(byte[] fileChunk, int nbytes) throws RemoteException, IOException {
        if (fout == null) {
            System.out.println("There is no file opened for writing!");
            throw new IOException("<CLI> There is no file opened for writing!");
        }

        try {
            fout.write(fileChunk, 0, nbytes);
        } catch (IOException e) {
            System.out.println("Exception writing on file: " + e);
            throw new IOException("<CLI> Exception writing on file", e.getCause());
        }
    }

    public static void main(String[] args){
            
        String objectUrl;        
        File localDirectory;
        String fileName;                
        
        String localFilePath;
        FileOutputStream localFileOutputStream = null;     
        
        GetRemoteFileClient myRemoteService = null;
        GetRemoteFileServiceInterface remoteFileService;
               
        /**
         * Trata os argumentos da linha de comando.
         */        
        
        if(args.length != 3){
            System.out.println("Deve passar na linha de comando: (1) a localizacao do RMI registry onte esta' "
                                + "registado o servico; (2) a directoria local onde pretende guardar "
                                + "o ficheiro obtido; e (3) o ficheiro pretendido!");
            System.out.println();
            return;
        }        

        objectUrl = "rmi://"+args[0]+"/servidor-ficheiros-pd";        
        localDirectory = new File(args[1].trim());
        fileName = args[2].trim();
                
        if(!localDirectory.exists()){
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return;
        }
        
        if(!localDirectory.isDirectory()){
            System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
            return;
        }
        if(!localDirectory.canWrite()){
            System.out.println("Sem permissoes de escrita na directoria " + localDirectory);
            return;
        }
               
        try{
            
            /**
             * Cria o ficheiro local.
             */ 
            localFilePath = new File(localDirectory.getPath()+File.separator+fileName).getCanonicalPath();
            localFileOutputStream = new FileOutputStream(localFilePath);
            
            System.out.println("Ficheiro " + localFilePath + " criado.");
                        
            /**
             * Obtem a referencia remota para o servico com nome "servidor-ficheiros-pd".
             */
            remoteFileService = (GetRemoteFileServiceInterface) Naming.lookup(objectUrl);
            
            /**
             * Lanca o servico local para acesso remoto por parte do servidor.
             */
            myRemoteService = new GetRemoteFileClient();
            
            /**
             * Passa ao servico RMI LOCAL uma referencia para o objecto localFileOutputStream.
             */
            myRemoteService.setFout(localFileOutputStream);
            
            /**
             * Obtem o ficheiro pretendido, invocando o metodo getFile no servico remoto.
             */    
                    
            remoteFileService.getFile(fileName, myRemoteService);
                        
        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
        }catch(NotBoundException e){
            System.out.println("Servico remoto desconhecido - " + e);
        }catch(IOException e){
            System.out.println("Erro E/S - " + e);
        }catch(Exception e){
            System.out.println("Erro - " + e);
        }finally{
            if(localFileOutputStream != null){
                /**
                 * Encerra o ficheiro.
                 */
                try{
                    localFileOutputStream.close();
                }catch(IOException e){}
            }
            
            if(myRemoteService != null){
                
                /**
                * Retira do servico local a referencia para o objecto localFileOutputStream.
                */
                myRemoteService.setFout(null);
                
                /**
                 * Termina o serviço local.
                 */
                try{
                    UnicastRemoteObject.unexportObject(myRemoteService, true);
                }catch(NoSuchObjectException e){}
            }
        }
            
    }
}
