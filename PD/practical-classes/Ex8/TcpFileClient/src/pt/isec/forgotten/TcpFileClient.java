package pt.isec.forgotten;

import java.io.*;
import java.net.*;

public class TcpFileClient {
    private static final int TIMEOUT = 10;      // seconds
    private static final int MAX_SIZE = 4000;   // bytes

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

        try (
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                Socket socket = new Socket(serverAddress, serverPort)
        ) {
            socket.setSoTimeout(TIMEOUT);

            PrintStream printStreamOut = new PrintStream(socket.getOutputStream(), true);
            printStreamOut.println(fileToGet);

            InputStream socketInputStream = socket.getInputStream();
            byte[] byteBuffer = new byte[MAX_SIZE];
            int flag;
            while (true) {
                flag = socketInputStream.read(byteBuffer);

                if (flag <= 0) break;

                fileOutputStream.write(byteBuffer, 0, flag);
            }

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
