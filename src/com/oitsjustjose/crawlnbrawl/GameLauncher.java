package com.oitsjustjose.crawlnbrawl;

import com.oitsjustjose.crawlnbrawl.Util.FileUtils;
import org.newdawn.slick.util.ResourceLoader;

import java.io.File;

public class GameLauncher
{
    public static void main(String[] args)
    {
        // Copies the natives folder FROM the compiled jar if it doesn't exist
        if (!(new File("natives").exists()))
        {
            FileUtils.copyResourcesRecursively(ResourceLoader.getResource("natives"), new File("natives"));
        }

        // Sets the LWJGL Libraries path to the natives folder
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        new Game();
    }
}
