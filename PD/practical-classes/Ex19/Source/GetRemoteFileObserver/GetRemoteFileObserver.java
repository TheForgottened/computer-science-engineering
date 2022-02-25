import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;

/**
 *
 * @author Jose'
 */
public class GetRemoteFileObserver extends UnicastRemoteObject implements GetRemoteFileObserverInterface
{
    
    public GetRemoteFileObserver() throws RemoteException {}

    public void notifyNewOperationConcluded(String description) throws RemoteException
    {
        System.out.println("-> " + description);
        System.out.println();
    }
    
    public static void main(String[] args) {
        try{
            
            /*
            * Se existirem varias interfaces de rede activas na maquina onde corre esta aplicacao RMI,
            * convem definir de forma explicita o endereço que deve ser incluido na referencia remota do serviço
            * RMI criado. Para o efeito, o endereco deve ser atribuido 'a propriedade java.rmi.server.hostname.
            *
            * Pode ser no código atraves do metodo System.setProperty():
            *      - System.setProperty("java.rmi.server.hostname", "10.65.129.232"); //O endereco usado e' apenas um exemplo
            *      - System.setProperty("java.rmi.server.hostname", args[1]); //Neste caso, assume-se que o endereco e' passado como segundo argumento na linha de comando
            * 
            * Tambem pode ser como opcao passada 'a maquina virtual Java:
            *      - java -Djava.rmi.server.hostname=10.202.128.22 GetRemoteFileObserver ... //O endereco usado e' apenas um exemplo
            *      - No Netbeans: Properties -> Run -> VM Options -> -Djava.rmi.server.hostname=10.202.128.22 //O endereco usado e' apenas um exemplo
            */
            
            //Cria e lanca o servico 
            GetRemoteFileObserver observer = new GetRemoteFileObserver();
            System.out.println("Servico GetRemoteFileObserver criado e em execucao...");
            
            //Localiza o servico remoto nomeado "GetRemoteFile"
            String objectUrl = "rmi://127.0.0.1/servidor-ficheiros-pd"; //rmiregistry on localhost
            
            if(args.length > 0)
                objectUrl = "rmi://"+args[0]+"/servidor-ficheiros-pd"; 
                            
            GetRemoteFileServiceInterface getRemoteFileService = (GetRemoteFileServiceInterface)Naming.lookup(objectUrl);
            
            //adiciona observador no servico remoto
            getRemoteFileService.addObserver(observer);
            
            System.out.println("<Enter> para terminar...");
            System.out.println();
            System.in.read();
            
            getRemoteFileService.removeObserver(observer);
            UnicastRemoteObject.unexportObject(observer, true);
            
        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(IOException | NotBoundException e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }  
    }
}
