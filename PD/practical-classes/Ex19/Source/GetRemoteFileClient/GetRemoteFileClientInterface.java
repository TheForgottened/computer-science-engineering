/**
 *
 * @author Jose'
 */
public interface GetRemoteFileClientInterface extends java.rmi.Remote
{
    
    void writeFileChunk(byte [] fileChunk, int nbytes) throws java.rmi.RemoteException, java.io.IOException;
    
}
