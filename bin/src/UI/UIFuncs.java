package UI;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalTime;
import java.util.Collections;

public class UIFuncs {
    public static ArrayList<File> songList = new ArrayList<File>();
    public static ArrayList<File> temp = new ArrayList<File>();
    private static int iterator = 0;
    public static boolean isShuffled = false;
    static MediaPlayer player;
    static Media nextSong;
    Thread play;
    Thread shuffle;
    String songName;
    String songFolder;
    String durationRemaining;
    String totalDuration;

    public UIFuncs() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("UserFiles\\Paths.txt"));
            songFolder = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String finalSongFolder = songFolder + "\\";
        File folder = new File(finalSongFolder);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                File temp = new File(files[i].getAbsolutePath());
                songList.add(temp);
            }
        }

        temp.addAll(songList);

    }

    public void play(ArrayList<File> songList, boolean isPaused) {

        if (iterator < songList.size()) {
            String songName = songList.get(iterator).getName().substring(0,
                    songList.get(iterator).getName().length() - 4);

            nextSong = new Media(songList.get(iterator).toURI().toString());

            player = new MediaPlayer(nextSong);
            UI.updateSongName(songName);

            if(isPaused) {
                //player.play();
                player.setOnReady(new Runnable() {
    
                    @Override
                    public void run() {
                        int totalTimeInMinutes = (int) player.getTotalDuration().toSeconds();
                        String totalDuration = LocalTime.MIN.plusSeconds(totalTimeInMinutes).toString().substring(3);
                        UI.updateTotalTimeLeft(totalDuration);
                        player.play();
    
                        new Thread(() -> {
                            while (true) {
                                try {
                                    int remainingTime = (int) player.getCurrentTime().toSeconds();
                                    durationRemaining = LocalTime.MIN.plusSeconds(remainingTime).toString().substring(3);
                                    UI.updateRemainingSongTime(durationRemaining + " / ");
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
    
                });
            } else {
                try {
                    player.play();
                    Thread.sleep(1);
                    player.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
            player.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    player.stop();
                    iterator++;
                    UI.updateSongName(songName);
                    play(UIFuncs.songList, false);
                }

            });
        }

    }

    public void pause() {
        player.stop();
    }

    public void skip(ArrayList<File> songList, boolean isShuffled) {
        if (iterator >= songList.size()) {
            player.stop();
        } else {
            if (!isShuffled) {
                player.stop();
                iterator++;
                play(UIFuncs.songList, false);
            } else {
                player.stop();
                iterator++;
                shuffle();
            }
        }
    }

    public void setShuffle(boolean isShuffled) {
        UIFuncs.isShuffled = isShuffled;
    }

    public void reverse(ArrayList<File> songList) {
        if (iterator < 0) {
            player.stop();
        } else {
            if (!isShuffled) {
                player.stop();
                iterator--;
                play(UIFuncs.songList, false);
            } else {
                player.stop();
                iterator--;
                shuffle();
            }
        }
    }

    public void shuffle() {
        player.stop();
        ArrayList<File> shuffle = new ArrayList<File>();
        shuffle.addAll(songList);
        Collections.shuffle(shuffle);
        setShuffle(true);
        if (iterator < shuffle.size()) {
            String songName = shuffle.get(iterator).getName().substring(0,
                    shuffle.get(iterator).getName().length() - 4);

            nextSong = new Media(shuffle.get(iterator).toURI().toString());

            player = new MediaPlayer(nextSong);
            UI.updateSongName(songName);

            player.play();
            player.setOnReady(new Runnable() {

                @Override
                public void run() {
                    int totalTimeInMinutes = (int) player.getTotalDuration().toSeconds();
                    String totalDuration = LocalTime.MIN.plusSeconds(totalTimeInMinutes).toString().substring(3);
                    UI.updateTotalTimeLeft(totalDuration);
                    player.play();

                    new Thread(() -> {
                        while (true) {
                            try {
                                int remainingTime = (int) player.getCurrentTime().toSeconds();
                                durationRemaining = LocalTime.MIN.plusSeconds(remainingTime).toString().substring(3);
                                UI.updateRemainingSongTime(durationRemaining + " / ");
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
            player.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    player.stop();
                    iterator++;
                    UI.updateSongName(songName);
                    play(shuffle, false);
                }
            });
        }
    }
}
