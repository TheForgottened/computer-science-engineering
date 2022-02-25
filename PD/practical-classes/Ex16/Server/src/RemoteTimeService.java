import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.spi.CalendarDataProvider;

public class RemoteTimeService extends UnicastRemoteObject implements RemoteTimeInterface {
    private List<String> msgs = new ArrayList<>();

    protected RemoteTimeService() throws RemoteException { }

    protected RemoteTimeService(int port) throws RemoteException { super(port); }

    protected RemoteTimeService(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    public Hora getHora() throws RemoteException {
        Calendar calendar = new GregorianCalendar();
        return new Hora(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    @Override
    public void depositaMsg(String msg) throws RemoteException {
        msgs.add(msg);
    }

    @Override
    public List<String> getMsgs() throws RemoteException {
        return msgs;
    }
}
