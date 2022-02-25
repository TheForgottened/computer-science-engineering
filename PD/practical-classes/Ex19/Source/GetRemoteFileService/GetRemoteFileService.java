import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.AccessDeniedException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jose'
 */

public class GetRemoteFileService  extends UnicastRemoteObject implements GetRemoteFileServiceInterface
{
    public static final String SERVICE_NAME = "servidor-ficheiros-pd";
    public static final int MAX_CHUNCK_SIZE = 10000; //bytes
    
    protected File localDirectory;
    List<GetRemoteFileObserverInterface> observers;    
    
    public GetRemoteFileService(File localDirectory) throws RemoteException 
    {
        this.localDirectory = localDirectory;
        observers = new ArrayList<>();
    }
    
    protected FileInputStream getRequestedFileInputStream(String fileName) throws IOException
    {        
        String requestedCanonicalFilePath;
        
        fileName = fileName.trim();        

        /*
         * Verifica se o ficheiro solicitado existe e encontra-se por baixo da localDirectory.
         */

        requestedCanonicalFilePath = new File(localDirectory+File.separator+fileName).getCanonicalPath();

        if(!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath()+File.separator)){
            System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
            System.out.println("A directoria de base nao corresponde a " + localDirectory.getCanonicalPath()+"!");
            
            notifyObservers("Solicitado ficheiro nao permitido: " + fileName);                    
            
            throw new AccessDeniedException("<SERV> Nao e' permitido aceder ao ficheiro: "+fileName);
        }

        /*
         * Abre o ficheiro solicitado para leitura.
         */
        return new FileInputStream(requestedCanonicalFilePath);
        
    }
    
    @Override
    public byte [] getFileChunk(String fileName, long offset) throws RemoteException, IOException
    {            
        byte [] fileChunk = new byte[MAX_CHUNCK_SIZE];
        int nbytes;        
        
        fileName = fileName.trim();

        if(offset == 0){
            try{
                System.out.println("getFileChunk: recebido pedido remoto de " + getClientHost()+ " para: " + fileName);
                notifyObservers("getFileChunk: recebido pedido remoto de " + getClientHost()+ " para: " + fileName);
            }catch(ServerNotActiveException e){
                System.out.println("getFileChunk: recebido pedido local para: " + fileName);
                notifyObservers("getFileChunk: recebido pedido local para: " + fileName);
            }

            System.out.println("getFileChunk: pedido atendido pela thread com id: " + Thread.currentThread().getId());
        }
        
        try(FileInputStream requestedFileInputStream = getRequestedFileInputStream(fileName)){
                                    
            /*
             * Obtem um bloco de bytes do ficheiro, omitindo os primeiros offset bytes.
             */
            requestedFileInputStream.skip(offset);
            nbytes = requestedFileInputStream.read(fileChunk);

            if(nbytes == -1){//EOF
                return null;
            }

            /*
             * Se fileChunk nao esta' totalmente preenchido (MAX_CHUNCK_SIZE), recorre-se
             * a um array auxiliar com tamanho correspondente ao numero de bytes efectivamente lidos.
             */
            if(nbytes < fileChunk.length){
                return Arrays.copyOf(fileChunk, nbytes);                
            }
                
            return fileChunk;
            
        }catch(FileNotFoundException e){   //Subclasse de IOException                 
            System.out.println("Ocorreu a excepcao {" + e + "} ao tentar abrir o ficheiro!"); 
            notifyObservers("Ficheiro inexistente: " + fileName);
            throw new FileNotFoundException("<SERV> Ficheiro inexistente: " + fileName);
        }catch(IOException e){
            System.out.println("Ocorreu a excepcao de E/S: \n\t" + e);
            notifyObservers("Excepcao no acesso para leitura ao ficheiro: " + fileName);
            throw new IOException("<SERV> Excepcao no acesso para leitura ao ficheiro: " + fileName, e.getCause());
        }
        
    }
    
    @Override
    public void getFile(String fileName, GetRemoteFileClientInterface cliRemoto) throws RemoteException, IOException
    {        
        byte [] fileChunk = new byte[MAX_CHUNCK_SIZE];
        int nbytes;
        
        fileName = fileName.trim();
        
        try{
            System.out.println("getFile: recebido pedido remoto de " + this.getClientHost()+ " para: " + fileName);
            notifyObservers("getFile: recebido pedido remoto de " + this.getClientHost()+ " para: " + fileName);
        }catch(ServerNotActiveException e){
            System.out.println("getFile: recebido pedido local para: " + fileName);
            notifyObservers("getFile: recebido pedido local para: " + fileName);
        }

        System.out.println("getFile: pedido atendido pela thread com id: " + Thread.currentThread().getId());

        try(FileInputStream requestedFileInputStream=getRequestedFileInputStream(fileName)){            
            /*
             * Obtem os bytes do ficheiro por bloco de bytes.
             */
            while((nbytes = requestedFileInputStream.read(fileChunk))!=-1){                         

                /*
                 * Escreve o bloco actual no cliente, invocando o metodo writeFileChunk da sua interface remota.
                 */
                cliRemoto.writeFileChunk(fileChunk, nbytes);
                
            }
                
            System.out.println("Ficheiro " + new File(localDirectory+File.separator+fileName).getCanonicalPath() + 
                    " transferido para o cliente com sucesso.");
            notifyObservers("Ficheiro " + new File(localDirectory+File.separator+fileName).getCanonicalPath() + 
                    " transferido para o cliente com sucesso.");
            System.out.println();            
            
        }catch(FileNotFoundException e){   //Subclasse de IOException                 
            System.out.println("Ocorreu a excepcao {" + e + "} ao tentar abrir o ficheiro!");
            notifyObservers("Excepcao ao tentar abrir o ficheiro: " + fileName);
            throw new FileNotFoundException("<SERV> Excepcao ao tentar abrir o ficheiro: " + fileName);
        }catch(IOException e){
            System.out.println("Ocorreu a excepcao de E/S: \n\t" + e);
            notifyObservers("Excepcao ao aceder para leitura ao ficheiro: " + fileName);
            throw new IOException("<SERV> Excepcao ao aceder par aleitura ao ficheiro: " + fileName, e.getCause());
        }
                
    }
    
    public synchronized void addObserver(GetRemoteFileObserverInterface observer) throws java.rmi.RemoteException
    {
        if(!observers.contains(observer)){
            observers.add(observer);
            System.out.println("+ um observador.");
        }

    }
    
    public synchronized void removeObserver(GetRemoteFileObserverInterface observer) throws java.rmi.RemoteException
    {
        if(observers.remove(observer))
            System.out.println("- um observador.");
    }
    
    protected synchronized void notifyObservers(String msg)
    {
        int i;
        
        for(i=0; i < observers.size(); i++){
            try{       
                observers.get(i).notifyNewOperationConcluded(msg);
            }catch(RemoteException e){
                observers.remove(i--);
                System.out.println("- um observador (observador inacessivel).");
            }
        }
    }
    
    /*
     * Lanca e regista um servico com interface remota do tipo GetRemoteFileInterface
     * sob o nome dado pelo atributo estatico SERVICE_NAME.
     */
    static public void main(String []args)
    {
        File localDirectory;
        
        /*
         * Se existirem varias interfaces de rede activas na maquina onde corre esta aplicacao/servidor RMI,
         * convem definir de forma explicita o endereço que deve ser incluido na referencia remota do serviço
         * RMI criado. Para o efeito, o endereco deve ser atribuido 'a propriedade java.rmi.server.hostname.
         *
         * Pode ser no código atraves do metodo System.setProperty():
         *      - System.setProperty("java.rmi.server.hostname", "10.65.129.232"); //O endereco usado e' apenas um exemplo
         *      - System.setProperty("java.rmi.server.hostname", args[1]); //Neste caso, assume-se que o endereco e' passado como segundo argumento na linha de comando
         * 
         * Tambem pode ser como opcao passada 'a maquina virtual Java:
         *      - java -Djava.rmi.server.hostname=10.202.128.22 GetRemoteFileService c:\temp\ //O endereco usado e' apenas um exemplo
         *      - No Netbeans: Properties -> Run -> VM Options -> -Djava.rmi.server.hostname=10.202.128.22 //O endereco usado e' apenas um exemplo
         */

        /*
         * Trata os argumentos da linha de comando
         */
        if(args.length != 1){
            System.out.println("Sintaxe: java GetFileUdpServer localRootDirectory");
            return;
        }        

        localDirectory = new File(args[0].trim());

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
       
       /*
        * Lanca o rmiregistry localmente no porto TCP por omissao (1099.
        */
        try{
                        
            try{
                
                System.out.println("Tentativa de lancamento do registry no porto " + 
                                    Registry.REGISTRY_PORT + "...");
                
                LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                
                System.out.println("Registry lancado!");
                                
            }catch(RemoteException e){
                System.out.println("Registry provavelmente ja' em execucao!");       
            }
            
            /*
             * Cria o servico
             */            
            GetRemoteFileService fileService = new GetRemoteFileService(localDirectory);
            
            System.out.println("Servico GetRemoteFile criado e em execucao ("+fileService.getRef().remoteToString()+"...");
            
            /*
             * Regista o servico no rmiregistry local para que os clientes possam localiza'-lo, ou seja,
             * obter a sua referencia remota (endereco IP, porto de escuta, etc.).
             */
            
            Naming.bind("rmi://localhost/" + SERVICE_NAME, fileService);     
                   
            System.out.println("Servico " + SERVICE_NAME + " registado no registry...");
            
            /*
             * Para terminar um servico RMI do tipo UnicastRemoteObject:
             * 
             *  UnicastRemoteObject.unexportObject(fileService, true);
             */
            
        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(MalformedURLException | AlreadyBoundException e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }                
    }    
}
