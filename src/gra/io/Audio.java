package gra.io;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.*;

/**
 * Dziwięki
 */
public class Audio {
    /**
     * Klip muzyczny
     */
    public static Clip music;
    /**
     * Głośność muzyki
     */
    public static float volumeMusic = -20f;
    /**
     * Głośność dźwięku
     */
    public static float volumeSound = -20f;

    /**
     * Odtwarzanie dźwięku
     * @param filePath ścieżka do pliku dźwiękowego
     */
    public static void playAudio(String filePath){
        try
        {
            File file = new File(filePath);
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volumeSound);
            clip.start();
            stream.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Odtwarza standardową muzykę
     */
    public static void playMusic(){
        playMusic("audio/music/music1.wav");
    }

    /**
     * Oddtwarzanie muzyki
     * @param filepath ścieżka do pliku z muzyką.
     */
    public static void playMusic(String filepath){
        try
        {
            File file = new File(filepath);
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            music = AudioSystem.getClip();
            music.open(stream);
            music.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volumeMusic);
            music.start();
            stream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Zatrzymuje muzykę.
     */
    public static void stopMusic(){
        music.stop();
    }

    /**
     * Zmiana głośności dźwięków. Utrzymuje głośność w zakresie od -80 do 0.
     * @param amount o ile zmniejszyć lub zwiększyć głośność
     */
    public static void changeSoundVolume(float amount){
        if (volumeSound >= -80f && volumeSound <= 0){
            if (amount < 0){
                float ceiling = -80f -volumeSound;
                if (amount >= ceiling){
                    volumeSound += amount;
                } else {
                    volumeSound =-80f;
                }
            } else if (amount > 0){
                float ceiling = -volumeSound;
                if (amount <= ceiling){
                    volumeSound += amount;
                } else {
                    volumeSound =0f;
                }
            }
        }
    }

    /**
     * Zmiana głośności muzyki. Utrzymuje głośność w zakresie od -80 do 0.
     * @param amount o ile zmniejszyć lub zwiększyć głośność
     */
    public static void changeMusicVolume(float amount){
        if (volumeMusic >= -80f && volumeMusic <= 0){
            if (amount < 0){
                float ceiling = -80f -volumeMusic;
                if (amount >= ceiling){
                    volumeMusic += amount;
                } else {
                    volumeMusic =-80f;
                }
            } else if (amount > 0){
                float ceiling = -volumeMusic;
                if (amount <= ceiling){
                    volumeMusic += amount;
                } else {
                    volumeMusic =0f;
                }
            }
        }
        FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volumeMusic);
    }


}
