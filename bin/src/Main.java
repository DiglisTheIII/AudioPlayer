import java.io.IOException;

import PreSetup.SetupUI;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;
import UI.UI;
import UI.UIFuncs;
import java.io.File;

public class Main {

    MediaPlayer player;

    public static void main(String[] args) {
        Platform.startup(() -> {         
            try {
                File f = new File("UserFiles//Paths.txt");
                if (!f.exists()) {
                    SetupUI ui = new SetupUI();
                } else {
                    UI ui = new UI();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

}