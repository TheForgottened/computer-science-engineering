package pt.isec.metapd.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkUtils {
    private static final int MIN_VALID_PORT = 1;
    private static final int MAX_VALID_PORT = 65535;

    private NetworkUtils() {}

    public static boolean isPortFree(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

    public static int getRandomFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    public static boolean isPortValid(int port) { return port >= MIN_VALID_PORT && port <= MAX_VALID_PORT; }

}
