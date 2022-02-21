package pt.isec.angelopaiva.jogo.iu.gui.controls;

import javafx.scene.input.MouseButton;
import pt.isec.angelopaiva.jogo.iu.gui.resources.SoundPlayer;
import pt.isec.angelopaiva.jogo.iu.gui.resources.sounds.ProjectSounds;

public class CustomButton extends javafx.scene.control.Button {
    public CustomButton(String s) {
        super(s);
        setListeners();
    }

    private void setListeners() {
        setOnMouseEntered(e -> SoundPlayer.playSound(ProjectSounds.MENU_HOVER));
        setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;

            SoundPlayer.playSound(ProjectSounds.MENU_CLICK);
        });
    }
}
