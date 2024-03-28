import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private Clip clip;

    public void playMusic(String filePath) {
        try {
            // Open an audio input stream.
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get a sound clip resource.
            clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            clip.open(audioStream);

            // Start playing the audio clip
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Call this method when you want to stop the music
    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
