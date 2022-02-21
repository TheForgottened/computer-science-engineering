package pt.isec.angelopaiva.jogo.iu.gui.resources;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundPlayer {
    static MediaPlayer mp;

    public static void playSound(String name) {
        String path = Resources.getResourceFilename("sounds/" + name);

        if (path == null) return;

        Media music = new Media(path);
        mp = new MediaPlayer(music);
        mp.setStartTime(Duration.ZERO);
        mp.setStopTime(music.getDuration());
        mp.setAutoPlay(true);
    }

    private SoundPlayer() {}
}
