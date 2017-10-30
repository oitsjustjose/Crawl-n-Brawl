package edu.utc.gamedev.Manager;

/**
 * @author Craig Tanis
 * <p>
 * Code obtained from https://github.com/ctanis/cpsc3520_fa17/blob/master/pongy/src/AudioManager.java
 */

import edu.utc.gamedev.Game;
import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;


/**
 * A utility class for caching sounds from files and enabling the
 * straightforward triggering of those sounds (samples and loops)
 */
public class AudioManager
{
    private static AudioManager instance;
    private HashMap<String, AudioWrapper> sounds;

    private AudioManager()
    {
        sounds = new HashMap<String, AudioWrapper>();
    }

    /**
     * @return the singleton AudioManager instance
     */
    public static AudioManager getInstance()
    {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    /**
     * load a one-shot sample
     *
     * @param name identifier for loaded sound
     * @param path path to file
     */
    public void loadSample(String name, String path) throws IOException
    {
        // extract file extension
        String mode = path.substring(path.lastIndexOf('.') + 1).toUpperCase();

        Audio tmp = AudioLoader.getAudio(mode, ResourceLoader.getResourceAsStream(path));
        sounds.put(name, new AudioWrapper(AudioType.SAMPLE, tmp));
    }

    /**
     * load a looping music track
     *
     * @param name identifier for loaded sound
     * @param path path to file
     */
    public void loadLoop(String name, String path) throws IOException
    {
        // extract file extension
        String mode = path.substring(path.lastIndexOf('.') + 1).toUpperCase();

        Audio tmp = AudioLoader.getStreamingAudio(mode, ResourceLoader.getResource(path));
        sounds.put(name, new AudioWrapper(AudioType.LOOP, tmp));
    }

    /**
     * get sound associated with name identifier
     *
     * @param name the loaded sound identifier
     */
    public AudioWrapper get(String name)
    {
        return sounds.get(name);
    }

    /**
     * play sound associated with name identifier
     *
     * @param name the loaded sound identifier
     */
    public void play(String name)
    {
        sounds.get(name).play();
    }

    /**
     * play sound associated with name identifier
     *
     * @param name the loaded sound identifier
     * @param vol  volume of sound (0.0 to 1.0)
     */
    public void play(String name, float vol)
    {
        sounds.get(name).play(vol);
    }

    /**
     * call this once per frame
     */
    public void update()
    {
        SoundStore.get().poll(0);
    }

    /**
     * call this to clean up
     */
    public void destroy()
    {
        AL.destroy();
    }


    private enum AudioType
    {
        LOOP, SAMPLE
    }

    /**
     * A handle on a loaded audio object, to play the same sound repeatedly
     * without having to look it up
     */
    public class AudioWrapper
    {
        private Audio sound;
        private AudioType type;

        private AudioWrapper(AudioType t, Audio a)
        {
            type = t;
            sound = a;
        }

        /**
         * play this sound at config's volume
         */
        public void play()
        {
            play(Game.getInstance().config.volume);
        }

        /**
         * Play this sound
         *
         * @param vol value between 0.0 and 1.0 to specify relative volume of sound
         *            if volume is 0, sound is not played
         */
        public void play(float vol)
        {
            // prevents the sound from being played if sound is "off"
            if (vol <= 0F)
                return;

            switch (type)
            {
                case LOOP:
                    sound.playAsMusic(1.0f, vol, true);
                    break;

                case SAMPLE:
                    sound.playAsSoundEffect(1.0f, vol, false);
                    break;
            }
        }

    }
}
