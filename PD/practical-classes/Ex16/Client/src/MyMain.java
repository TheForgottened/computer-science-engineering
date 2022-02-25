import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MyMain {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("[ERROR] Invalid number of arguments.");
            return;
        }

        Hora hora = null;
        try {
            RemoteTimeInterface remote = (RemoteTimeInterface) Naming.lookup("rmi://" + args[0] + "/timeserver");
            hora = remote.getHora();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        System.out.println("rmi://" + args[0] + "/timeserver");
        System.out.println(hora);
    }
}
