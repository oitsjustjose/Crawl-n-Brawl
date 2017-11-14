package com.oitsjustjose.crawlnbrawl;

import com.oitsjustjose.crawlnbrawl.Entity.Living.EntityMonster;
import com.oitsjustjose.crawlnbrawl.Manager.AudioManager;
import com.oitsjustjose.crawlnbrawl.Manager.SceneManager;
import com.oitsjustjose.crawlnbrawl.Manager.ScreenshotManager;
import com.oitsjustjose.crawlnbrawl.Scene.Button;
import com.oitsjustjose.crawlnbrawl.Scene.Dungeon;
import com.oitsjustjose.crawlnbrawl.Scene.Menu;
import com.oitsjustjose.crawlnbrawl.Scene.Scene;
import com.oitsjustjose.crawlnbrawl.Util.Config;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class Game
{
    private static Game instance;
    public Config config;
    public ScreenshotManager screenshotManager;
    public SceneManager sceneManager;
    private boolean fullscreen;

    public Game()
    {
        instance = this;
        this.config = new Config();
        this.screenshotManager = new ScreenshotManager();
        this.fullscreen = false;
        this.sceneManager = new SceneManager();
        initGL();
        initAudio();
        initGame();
    }

    public static Game getInstance()
    {
        return instance;
    }

    public void initGame()
    {
        Menu menu = new Menu();
        menu.addButton(new Button(256, 64, "Play", new Color(200, 200, 200), new Color(85, 124, 0)), new Dungeon());
        menu.addButton(new Button(256, 64, "Exit", new Color(200, 200, 200), new Color(85, 124, 0)), null);
        menu.adjustButtons();
        Scene currScene = menu;

        while (currScene.go())
        {
            currScene = currScene.nextScene();

            if (currScene == null)
            {
                currScene = menu;
            }
        }

        AudioManager.getInstance().destroy();
        Display.destroy();
    }

    public void initGL()
    {
        setDisplayMode(this.config.resolutionWidth, this.config.resolutionHeight, fullscreen);
        Display.setTitle("Crawl 'n Brawl");
        // Centers the window
        Display.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (this.config.resolutionWidth / 2),
                (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (this.config.resolutionHeight / 2));

        initIcon();

        try
        {
            Display.create();
        }
        catch (LWJGLException e)
        {
            System.out.println("Caught an LWJGL exception when creating the display. Quitting..");
            System.out.println(e.getMessage());
            System.exit(0);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(0F, 0F, 0F, 0.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glViewport(0, 0, this.config.resolutionWidth, this.config.resolutionHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, this.config.resolutionWidth, this.config.resolutionHeight, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * SOURCE: http://wiki.lwjgl.org/wiki/LWJGL_Basics_5_(Fullscreen).html
     *
     * @param width      The width of the display required
     * @param height     The height of the display required
     * @param fullscreen True if we want fullscreen mode
     */
    public void setDisplayMode(int width, int height, boolean fullscreen)
    {
        if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen))
            return;

        try
        {
            org.lwjgl.opengl.DisplayMode targetDisplayMode = null;

            if (fullscreen)
            {
                org.lwjgl.opengl.DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i = 0; i < modes.length; i++)
                {
                    org.lwjgl.opengl.DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height))
                    {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq))
                        {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))
                            {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))
                        {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            }
            else
            {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null)
            {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
            // Stupid LWJGL stuff. Thanks Ellpeck!
            Display.setResizable(false);
            Display.setResizable(true);
            Display.setVSyncEnabled(true);
        }
        catch (LWJGLException e)
        {
            System.err.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    /**
     * SOURCE: http://forum.lwjgl.org/index.php?topic=3422.0
     *
     * @return The ByteBuffer of the PNG located at the passed URL
     */
    private ByteBuffer loadIcon(URL url) throws IOException
    {
        System.out.println("Loaded icon " + url.getPath().replace("/C:/", "C:/"));
        InputStream is = url.openStream();
        try
        {
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer buffer = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.RGBA);
            buffer.flip();
            return buffer;
        }
        finally
        {
            is.close();
        }
    }

    public void initIcon()
    {
        try
        {
            Display.setIcon(new ByteBuffer[]{
                    loadIcon(ResourceLoader.getResource("assets/icon/icon16.png".replace("/", File.separator))),
                    loadIcon(ResourceLoader.getResource("assets/icon/icon32.png".replace("/", File.separator))),
                    });
        }
        catch (IOException e)
        {
            System.out.println("Icon file not found; using default.");
            Display.setIcon(new ByteBuffer[]{});
        }
    }

    public void initAudio()
    {
        try
        {
            AudioManager.getInstance().loadSample("sword_slash", "assets/sounds/sword_slash.wav".replace("/", File.separator));
            AudioManager.getInstance().loadSample("player_jump", "assets/sounds/player_jump.wav".replace("/", File.separator));
            AudioManager.getInstance().loadSample("player_death", "assets/sounds/death/player_death.wav".replace("/", File.separator));
            AudioManager.getInstance().loadSample("level_up", "assets/sounds/environment/level_up.wav".replace("/", File.separator));
            for (int i = 1; i <= 2; i++)
            {
                AudioManager.getInstance().loadSample("player_hit" + i, "assets/sounds/damage/player_hit" + i + ".wav".replace("/", File.separator));
            }
            for (int i = 1; i <= 10; i++)
            {
                AudioManager.getInstance().loadSample("damage_hit" + i, "assets/sounds/damage/damage_hit" + i + ".wav".replace("/", File.separator));
            }
            for (int i = 0; i < EntityMonster.Type.values().length; i++)
            {
                AudioManager.getInstance().loadSample(EntityMonster.Type.values()[i].getResourceName() + "_death", "assets/sounds/death/".replace("/", File.separator) + EntityMonster.Type.values()[i].getResourceName() + "_death.wav");
            }
            AudioManager.getInstance().loadSample("menu_move", "assets/sounds/menu/move.wav".replace("/", File.separator));
            AudioManager.getInstance().loadSample("menu_select", "assets/sounds/menu/select.wav".replace("/", File.separator));
        }
        catch (IOException e)
        {
            System.out.println("Audio file not found; some sounds may not work correctly");
            System.out.println(e.getMessage());
        }
    }
}
