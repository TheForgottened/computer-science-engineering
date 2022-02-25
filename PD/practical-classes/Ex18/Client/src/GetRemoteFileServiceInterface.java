import java.rmi.*;

public interface GetRemoteFileServiceInterface extends Remote
{
    byte [] getFileChunk(String fileName, long offset) throws RemoteException, java.io.IOException;

    void getFile(String fileName, GetRemoteFileClientInterface cliRef) throws java.io.IOException;
}
