package pt.isec.angelopaiva.jogo.utils;

import pt.isec.angelopaiva.jogo.logica.JogoManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class UtilsFiles {
    public static final String REPLAY_PATH = "replays/";
    private static final int NR_REPLAYS = 5;
    public static final String SAVES_PATH = "saves/";

    private UtilsFiles() {}

    public static void prepareDirectories() {
        File file = new File(SAVES_PATH);

        if (!file.exists()) file.mkdirs();

        file = new File(REPLAY_PATH);

        if (!file.exists()) file.mkdirs();
    }

    public static void saveGameToSaveFile(JogoManager o, String fileName) {
        File file = new File(SAVES_PATH);
        file.mkdirs();

        saveJogoManagerToFile(o, SAVES_PATH + fileName + ".save");
    }

    public static void saveGameToReplayFile(JogoManager o) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = simpleDateFormat.format(date);

        File file = new File(REPLAY_PATH);
        file.mkdirs();

        saveJogoManagerToFile(o, REPLAY_PATH + dateStr + ".rsave");
    }

    public static void saveJogoManagerToFile(JogoManager jogoManager, File file) {
        if (file == null) return;

        try (
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(jogoManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveJogoManagerToFile(JogoManager jogoManager, String filePath) {
        File file = new File(filePath);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(jogoManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JogoManager loadReplayFromFile(String fileName) { return loadJogoManagerFromFile(REPLAY_PATH + fileName); }

    public static JogoManager loadSaveFromFile(String fileName) { return loadJogoManagerFromFile(SAVES_PATH + fileName); }

    private static JogoManager loadJogoManagerFromFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) return null;

        return loadJogoManagerFromFile(file);
    }

    public static JogoManager loadJogoManagerFromFile(File file) {
        if (file == null) return null;

        Object o = null;

        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ) {

            o = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (o == null) return null;

        if (!(o instanceof JogoManager)) return null;

        return (JogoManager) o;
    }

    public static void updateReplayList() {
        File folder = new File(REPLAY_PATH);

        if (!folder.exists()) return;

        String[] fileNames = folder.list();

        if (fileNames == null || fileNames.length == 0) return;

        ArrayList<String> replays = new ArrayList<>(Arrays.asList(fileNames));

        while (replays.size() > NR_REPLAYS) {
            String olderName = replays.get(0);
            long olderDate = new File(REPLAY_PATH + replays.get(0)).lastModified();

            for (String fileName : replays) {
                File file = new File(REPLAY_PATH + fileName);

                if (file.lastModified() < olderDate) {
                    olderDate = file.lastModified();
                    olderName = fileName;
                }
            }

            try {
                Files.delete(Path.of(REPLAY_PATH + olderName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            replays.remove(olderName);
        }
    }

    public static List<String> getReplays() { return getPathList(REPLAY_PATH); }

    public static List<String> getSaves() { return getPathList(SAVES_PATH); }

    private static List<String> getPathList(String dirPath) {
        File file = new File(dirPath);

        if (!file.exists()) return new ArrayList<>();

        String[] fileNames = file.list();

        if (fileNames == null) return new ArrayList<>();

        return new ArrayList<>(Arrays.asList(fileNames));
    }
}
