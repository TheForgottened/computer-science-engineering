import java.rmi.*;

/**
 *
 * @author Jose'
 */
public interface GetRemoteFileObserverInterface extends Remote
{    
    public void notifyNewOperationConcluded(String description) throws RemoteException;    
    
}
