package pt.isec.metapd.concurrentserver.runnables;

import pt.isec.metapd.communication.TinyRequest;
import pt.isec.metapd.resources.MetaPDConstants;
import pt.isec.metapd.resources.RequestType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import static pt.isec.metapd.Server.LOGGER;

public class LoadBalancerPinger implements Runnable {
    private final AtomicBoolean mustStop;
    private final DatagramSocket datagramSocket;

    private DatagramPacket datagramPacket;

    public LoadBalancerPinger(
            AtomicBoolean mustStop,
            DatagramSocket datagramSocket,
            int tcpPort,
            String lbAddress,
            int lbPort
    ) {
        this.mustStop = mustStop;
        this.datagramSocket = datagramSocket;
        prepareDatagramPacket(tcpPort, lbAddress, lbPort);
    }

    private void prepareDatagramPacket(int tcpPort, String lbAddress, int lbPort) {
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
                ) {
            objectOutputStream.writeObject(new TinyRequest(RequestType.PING, tcpPort));
            objectOutputStream.flush();

            datagramPacket = new DatagramPacket(
                    byteArrayOutputStream.toByteArray(),
                    byteArrayOutputStream.size(),
                    InetAddress.getByName(lbAddress),
                    lbPort
            );
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to create ping packet!\n{0}", e.toString());
            mustStop.set(true);
        }
    }

    @Override
    public void run() {
        LOGGER.info("LoadBalancerPinger thread started.");

        try {
            while (!mustStop.get()) {
                datagramSocket.send(datagramPacket);
                LOGGER.log(Level.INFO, "Loadbalancer was just pinged!");

                Thread.sleep(MetaPDConstants.MIN_PING_TIME * (long) 1000);
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.toString());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString());
        } finally {
            mustStop.set(true);
        }
    }
}
