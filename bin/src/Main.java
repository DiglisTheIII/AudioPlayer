import java.io.IOException;
import java.util.Scanner;

import Media.MP3Player;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;


public class Main {

    MediaPlayer player;
    public static void main(String[] args) {
        Platform.startup(() -> {
            MP3Player player = new MP3Player();
            try {
                player.initialize();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Scanner in = new Scanner(System.in);
            System.out.println("1. Play playlist \n2. Shuffle Playlist");

            switch(in.nextLine()) {
                case "1":
                    player.playNoShuffle(player.songList);
                    break;
                case "2":
                    player.shufflePlaylist();
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        });
    }

}