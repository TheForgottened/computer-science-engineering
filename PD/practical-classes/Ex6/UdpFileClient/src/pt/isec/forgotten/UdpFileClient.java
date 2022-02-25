package pt.isec.forgotten;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpFileClient {
    private static final int TIMEOUT = 10;              // seconds
    private static final int MAX_PACKET_SIZE = 4000;    // bytes

    public static void main(String[] args) throws UnknownHostException {
        InetAddress serverAddress;
        int serverPort;

        String fileToGet;
        String localPath;

        File file;

        if (args.length != 4) {
            System.err.println("Usage: java <name> <server address> <server port> <file to get> <local path>");
            return;
        }

        serverAddress = InetAddress.getByName(args[0]);
        serverPort = Integer.parseInt(args[1]);
        fileToGet = args[2];
        localPath = args[3];

        try {
            file = createFile(new File(localPath), fileToGet);

            if (file == null) return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        DatagramPacket packet;
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                DatagramSocket socket = new DatagramSocket()
        ) {
            socket.setSoTimeout(TIMEOUT);

            byte[] buffer = new byte[MAX_PACKET_SIZE];

            packet = new DatagramPacket(fileToGet.getBytes(), fileToGet.length(), serverAddress, serverPort);
            socket.send(packet);

            packet = new DatagramPacket(buffer, buffer.length);
            do {
                socket.receive(packet);

                if (!packet.getAddress().equals(serverAddress) || packet.getPort() != serverPort) continue;

                fileOutputStream.write(buffer, 0, packet.getLength());
            } while (packet.getLength() > 0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File downloaded successfully!");
    }

    private static File createFile(File directory, String fileName) throws IOException {
        File file = new File(directory.getCanonicalPath() + File.pathSeparator + fileName);
        file.mkdirs();

        if (!file.canWrite()) return null;

        file.createNewFile();
        return file;
    }
}
