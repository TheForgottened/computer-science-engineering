/**
 *
 * @author Jose'
 */

import java.rmi.*;

public interface GetRemoteFileClientInterface extends Remote
{
    void writeFileChunk(byte [] fileChunk, int nbytes) throws RemoteException, java.io.IOException;
}
