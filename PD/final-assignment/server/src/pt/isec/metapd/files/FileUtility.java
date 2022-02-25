package pt.isec.metapd.files;

import pt.isec.metapd.communication.TinyMessageReceived;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileUtility {
    public static final int MAX_CHUNK_SIZE = 4000;
    public static final String LOCAL_FILE_PATH = ".\\local_files\\";

    public static String getFileName(TinyMessageReceived tinyMessageReceived) {
        return tinyMessageReceived.id() + "_" + tinyMessageReceived.fileName();
    }
    public static String getFileName(int messageId, String fileName) { return messageId + "_" + fileName; }

    public static List<String> getAllLocalFileNames() {
        File file = new File(LOCAL_FILE_PATH);

        return Arrays.asList(file.list());
    }

    public static void downloadFile(File file, InputStream inputStream) throws IOException {
        prepareDirectory(file);

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

    public static void deleteIfExists(File file) throws IOException {
        if (!file.exists()) return;

        file.delete();
    }

    public static void prepareDirectory(File file) throws IOException {
        File localPath = new File(LOCAL_FILE_PATH);

        if (!localPath.exists()) localPath.mkdirs();

        // if (file.exists()) return;

        if (!file.canWrite()) throw new IOException("Can't write to file");
        // file.createNewFile();
    }

    private FileUtility() {}
}
