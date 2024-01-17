package bin;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Tools {

    //MediaPlayer player;
    //Media nextSong;
    public int iterator = 0;

    public void loadCommands() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }

        });
    }

    public void skip(ArrayList<File> songList, MediaPlayer player, Media nextSong) {
        player.stop();
        setIterator(true);
        nextSong = new Media(songList.get(iterator).toURI().toString());
        System.out.println(iterator);
        player = new MediaPlayer(nextSong);
        player.play();
    }

    public void reverse(ArrayList<File> songList, MediaPlayer player, Media nextSong) {
        player.stop();
        setIterator(false);
        System.out.println(iterator);
        nextSong = new Media(songList.get(iterator).toURI().toString());
        player = new MediaPlayer(nextSong);
        player.play();
    }

    public void seek(MediaPlayer player) {
        Duration songLength = player.getTotalDuration();
        double totalDuration = songLength.toMillis();
        Scanner in = new Scanner(System.in);
        System.out.println("Minute to seek to (mm)");
        int inputMinTime = Integer.parseInt(in.nextLine().replaceAll(" ", ""));
        System.out.println("Second to seek to (ss)");
        int inputSecTime = Integer.parseInt(in.nextLine().replaceAll(" ", ""));
        inputSecTime = (inputMinTime * 60) + inputSecTime;
        double inputInMillis = inputSecTime * 1000;
        if (inputInMillis <= totalDuration) {
            player.seek(new Duration(inputInMillis));
        }
    }

    public void setIterator(boolean isIncrementPos) {
        if (isIncrementPos) {
            iterator++;
        } else {
            iterator--;
        }
        System.out.println(iterator);
    }

}
