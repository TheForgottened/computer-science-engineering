package pt.isec.metapd.logic.files;

import pt.isec.metapd.communication.TinyMessageReceived;

import java.io.*;

public class FileUtility {
    public static final int MAX_CHUNK_SIZE = 4000;

    public static String getFileName(TinyMessageReceived tinyMessageReceived) {
        return tinyMessageReceived.id() + "_" + tinyMessageReceived.fileName();
    }
    public static String getFileName(int messageId, String fileName) { return messageId + "_" + fileName; }

    public static void downloadFile(File file, InputStream inputStream) throws IOException {
        // prepareDirectory(file);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] byteBuffer = new byte[MAX_CHUNK_SIZE];
            int length;

            while (true) {
                length = inputStream.read(byteBuffer);

                if (length <= 0) break;

                fileOutputStream.write(byteBuffer, 0, length);
            }
        } catch (IOException e) {
            throw new IOException(e.getCause());
        }
    }

    public static void uploadFile(File file, OutputStream outputStream) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int nBytes = 0;
            byte[] fileChunk = new byte[MAX_CHUNK_SIZE];

            do {
                nBytes = fileInputStream.read(fileChunk);

                if (nBytes == -1) break;

                outputStream.write(fileChunk, 0, nBytes);
                outputStream.flush();
            } while (nBytes > 0);
        } catch (IOException e) {
            throw new IOException(e.getCause());
        }
    }

    public static Object loadFile(File fileName) throws IOException, ClassNotFoundException {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!
        return true;
    }

    public static Object loadFile(String fileName) throws IOException, ClassNotFoundException {
        //!!!!!!!!!!!!!!!!!!
        return true;
    }

    private FileUtility() {}
}
