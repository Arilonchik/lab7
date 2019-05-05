import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer implements AutoCloseable {
    private boolean released = false;
    private AudioInputStream stream = null;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;

    public SoundPlayer(File f) {
        try {
            stream = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.addLineListener(new Listener());
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            released = true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
            released = false;

            close();
        }
    }

    // true если звук успешно загружен, false если произошла ошибка
    public boolean isReleased() {
        return released;
    }

    // проигрывается ли звук в данный момент
    public boolean isPlaying() {
        return playing;
    }


    public void play(boolean breakOld) {
        if (released) {
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    // То же самое, что и play(true)
    public void play() {
        play(true);
    }

    // Останавливает воспроизведение
    public void stop() {
        if (playing) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null)
            clip.close();

        if (stream != null)
            try {
                stream.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
    }

    // Дожидается окончания проигрывания звука
    public void join() {
        if (!released) return;
        synchronized(clip) {
            try {
                while (playing)
                    clip.wait();
            } catch (InterruptedException exc) {}
        }
    }

    // Статический метод, для удобства
    public static SoundPlayer playSound(String path) {
        File f = new File(path);
        SoundPlayer snd = new SoundPlayer(f);
        snd.play();
        return snd;
    }

    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                }
            }
        }
    }
}
