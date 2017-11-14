package com.oitsjustjose.crawlnbrawl.Util;

/**
 * @author: Jose Stovall
 * <p>
 * Config files were learned on my own back when I was Googling stuff previously for my own personal projects
 * That being said: this file was written on my own, but how I learned it previous was via Google.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config
{
    public int resolutionWidth;
    public int resolutionHeight;
    public int targetFPS;
    public float volume;

    public Config()
    {
        loadConfig();
    }

    public void loadConfig()
    {
        try
        {
            File configFile = new File("settings.properties");
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String[] resolutions = props.getProperty("windowed_resolution", "800x600").split("x");
            this.resolutionWidth = Integer.parseInt(resolutions[0]);
            this.resolutionHeight = Integer.parseInt(resolutions[1]);
            this.targetFPS = Integer.parseInt(props.getProperty("target_FPS", "100"));
            this.volume = Integer.parseInt(props.getProperty("volume", "100")) / 100F; // divide by one hundred because users understand 0 -> 100 better than 0.0 -> 1.0

            reader.close();
        }
        catch (FileNotFoundException fnfex)
        {
            try
            {
                createConfig();
            }
            catch (IOException e)
            {
                System.out.println("Everything went wrong with the config. Do you have read/write perms for this folder?");
            }
        }
        // TODO: Properly handle these exceptions..
        catch (IOException ioEx)
        {
        }
        catch (NullPointerException npe)
        {
        }
    }

    public void createConfig() throws IOException
    {
        File configFile = new File("settings.properties");

        Properties props = new Properties();
        props.setProperty("windowed_resolution", "800x600");
        props.setProperty("target_FPS", "100");
        props.setProperty("volume", "100");

        String[] resolutions = props.getProperty("windowed_resolution", "800x600").split("x");
        this.resolutionWidth = Integer.parseInt(resolutions[0]);
        this.resolutionHeight = Integer.parseInt(resolutions[1]);
        this.targetFPS = Integer.parseInt(props.getProperty("target_FPS", "100"));
        this.volume = Integer.parseInt(props.getProperty("volume", "100")) / 100F; // divide by one hundred because users understand 0->100 better than 0.0 to 1.0

        FileWriter writer = new FileWriter(configFile);
        props.store(writer, "game settings");
        writer.close();
    }
}