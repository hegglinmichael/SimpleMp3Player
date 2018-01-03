import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import javafx.util.Duration;


public class AudioGuts {

    //private static AudioGuts instance = null;
    private ArrayList<String> playlist = new ArrayList<String>();
    private MediaPlayer mp = null;

    private int index = 0;
    private Duration x = null;
    private boolean paused = true;

    public AudioGuts(){
        startFx();
    }

    public void play(){
        File f = new File(playlist.get(index));

        Media hit = new Media(f.toURI().toString());
        mp = new MediaPlayer(hit);
        if (x != null) {
            mp.seek(x);
            mp.play();
        }else{
            mp.play();
        }


        paused = false;
    }

    public void setIndexSong(int index){
        this.index = index;
    }

    public void pause(){
        paused = true;
        x = mp.getCurrentTime();
        mp.pause();
    }

    public void addSong(String songPath){
        playlist.add(songPath);
    }

    public void removeSong(int index){
        playlist.remove(index);
    }
    public boolean isPaused(){
        return paused;
    }

    public void prvSong(){
        if(playlist.size() == 0){
            return;
        }

        index = (index-1)%playlist.size();
        play();
    }

    public void startFx() {
        new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
