import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jose'
 */

public interface GetRemoteFileInterface extends Remote {
    byte[] getFileChunk(String fileName, long offset) throws RemoteException;
}
