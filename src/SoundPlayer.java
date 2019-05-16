import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.*;

public class SoundPlayer extends Thread {
    private boolean released = false;
    private AudioInputStream stream = null;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;
    private File f;
    private String path;
    private AdvancedPlayer ap;
    private int d = 0;

    public SoundPlayer( String path){
        this.path = path;
        start();
    }

    public SoundPlayer( String path, int dur){
        this.path = path;
        this.d = dur;
        start();
    }

    @Override
    public void run() {
        if(d == 0){
        myPlay(path);}else{myPlay(path,d);}
    }
        public void myPlay(String path){
            try{
                InputStream is=new FileInputStream(path);
                AudioDevice device=new JavaSoundAudioDevice();
                ap=new AdvancedPlayer(is,device);
                ap.play();//Можно указать начало и конец проигрывания - ap.play(int start,int end);
                //ap.stop();
                //ap.close();
            }catch(Exception e){}

        }

    public void myPlay(String path, int dur){
        try{
            InputStream is=new FileInputStream(path);
            AudioDevice device=new JavaSoundAudioDevice();
            ap=new AdvancedPlayer(is,device);
            ap.play(0,dur);//Можно указать начало и конец проигрывания - ap.play(int start,int end);
            //ap.stop();
            ap.close();
        }catch(Exception e){}

    }
        public void stopPlay(){
        ap.stop();

        }

        public void close(){
        ap.close();
        }
    }


