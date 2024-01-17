package Media;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import bin.Tools;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MP3Player {

    public static MediaPlayer player;

    public String playerSongFolder;

    public ArrayList<File> songList;

    public static Media nextSong;

    boolean isPaused = false;

    private static int iterator = 0;

    public MP3Player() {

    }

    public void shufflePlaylist() {
        ArrayList<File> preShuffle = this.songList;
        Collections.shuffle(songList);

        try {
            nextSong = new Media(songList.get(iterator).toURI().toString());
            player = new MediaPlayer(nextSong);
            String songName = songList.get(iterator).getName().substring(0,
                    songList.get(iterator).getName().length() - 4);
            System.out.println("Now playing: " + songName);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Scanner sc = new Scanner(System.in);
                        System.out.println("Skip, Seek, Pause, Unpause, Reverse, Unshuffle");
                        String choice = sc.nextLine().toLowerCase();
                        boolean isBreakLoop = false;
                        switch (choice) {
                            case "skip":
                                player.stop();
                                iterator++;
                                nextSong = new Media(songList.get(iterator).toURI().toString());
                                System.out.println(iterator);
                                player = new MediaPlayer(nextSong);
                                player.play();
                                break;
                            case "reverse":
                                player.stop();
                                iterator--;
                                nextSong = new Media(songList.get(iterator).toURI().toString());
                                System.out.println(iterator);
                                player = new MediaPlayer(nextSong);
                                player.play();
                                break;
                            case "seek":
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
                                break;
                            case "pause":
                                player.pause();
                                isPaused = true;
                                break;
                            case "unpause":
                                if (isPaused) {
                                    player.play();
                                }
                                break;
                            case "unshuffle":
                                player.pause();
                                songList = preShuffle;
                                isBreakLoop = true;
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }
                        if(isBreakLoop) {
                            playNoShuffle(songList);
                            break;
                        }
                    }
                }
            }).start();
            player.play();
            player.setOnEndOfMedia(new Runnable() {

                @Override
                public void run() {
                    player.stop();
                    iterator++;
                    shufflePlaylist();
                }

            });
        } catch (Exception e) {
            player.stop();
            System.out.println(e.getMessage());
        }
    }

    public void playNoShuffle(ArrayList<File> songList) {
        try {
            nextSong = new Media(songList.get(iterator).toURI().toString());
            player = new MediaPlayer(nextSong);
            String songName = songList.get(iterator).getName().substring(0,
                    songList.get(iterator).getName().length() - 4);
            System.out.println("Now playing: " + songName);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Scanner sc = new Scanner(System.in);
                        System.out.println("Skip, Seek, Pause, Unpause, Reverse, Shuffle");
                        String choice = sc.nextLine().toLowerCase();
                        boolean isShuffle = false;
                        try {
                            if (choice.equals("skip")) {
                                player.stop();
                                iterator++;
                                nextSong = new Media(songList.get(iterator).toURI().toString());
                                System.out.println(iterator);
                                player = new MediaPlayer(nextSong);
                                player.play();
                            } else if (choice.equals("reverse")) {
                                player.stop();
                                iterator--;
                                nextSong = new Media(songList.get(iterator).toURI().toString());
                                System.out.println(iterator);
                                player = new MediaPlayer(nextSong);
                                player.play();
                            } else if (choice.equals("seek")) {
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
                                // in.close();

                            } else if (choice.equals("pause")) {
                                player.pause();
                                isPaused = true;
                            } else if (choice.equals("unpause")) {
                                if (isPaused) {
                                    player.play();
                                }
                            } else if(choice.equals("shuffle")) {
                                player.stop();
                                isShuffle = true;
                                
                            }
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("At end or beginning of playlist!");
                        }
                        if(isShuffle) {
                            shufflePlaylist();
                            break;
                        }
                    }
                }
            }).start();
            player.play();
            player.setOnEndOfMedia(new Runnable() {

                @Override
                public void run() {
                    player.stop();
                    iterator++;
                    playNoShuffle(songList);
                }

            });
        } catch (Exception e) {
            player.stop();
            System.out.println(e.getMessage());
        }
    }

    public void initialize() throws IOException, InterruptedException {
        File paths = new File("UserFiles\\Paths.txt");
        if (!paths.exists()) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter folder path to songs (eg. C:/Users/)");
            playerSongFolder = sc.nextLine();
            playerSongFolder = playerSongFolder.replace(" ", "");
            PrintWriter writer = new PrintWriter(paths, "UTF-8");
            paths.createNewFile();
            Thread.sleep(400);
            writer.println(playerSongFolder);
            writer.close();
            try {
                File f = new File(playerSongFolder);
                songList = new ArrayList<File>(Arrays.asList(f.listFiles()));
            } catch (Exception ex) {
                System.out.println("Invalid folder path");
            }
        } else {
            try {
                File f = paths;
                String songFolder = Files.readAllLines(Paths.get(f.getPath())).get(0);
                File songFolderPath = new File(songFolder);
                songList = new ArrayList<File>(Arrays.asList(songFolderPath.listFiles()));
                System.out.println("You exist");
            } catch (Exception ex) {
                System.out.println("Invalid folder path");
            }
        }
    }
}