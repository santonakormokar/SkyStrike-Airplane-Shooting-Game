package skystrike;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static SoundManager instance;
    private final Map<String, Clip> clips = new HashMap<>();
    private boolean muted = false;

    private SoundManager() { }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private Clip loadClip(String name) {
        if (clips.containsKey(name)) {
            return clips.get(name);
        }
        try {
            File file = new File("resources/sounds/" + name + ".wav");
            if (!file.exists()) {
                clips.put(name, null);
                return null;
            }
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clips.put(name, clip);
            return clip;
        } catch (Exception e) {
            System.err.println("SoundManager: could not load '" + name + "' -> " + e.getMessage());
            clips.put(name, null);
            return null;
        }
    }

    public void play(String name) {
        if (muted) return;
        Clip clip = loadClip(name);
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void setMuted(boolean muted) { this.muted = muted; }
    public boolean isMuted() { return muted; }
}
