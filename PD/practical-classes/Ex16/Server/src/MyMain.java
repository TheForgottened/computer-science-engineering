import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MyMain {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            RemoteTimeService remoteTimeService = new RemoteTimeService();
            Naming.rebind("rmi://localhost/timeserver", remoteTimeService);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
