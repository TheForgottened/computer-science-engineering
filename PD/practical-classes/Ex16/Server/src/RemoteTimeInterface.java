import java.util.List;

public interface RemoteTimeInterface extends java.rmi.Remote {
    Hora getHora() throws java.rmi.RemoteException;
    void depositaMsg(String msg) throws java.rmi.RemoteException;
    List<String> getMsgs() throws java.rmi.RemoteException;
}
