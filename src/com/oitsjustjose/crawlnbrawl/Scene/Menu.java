package com.oitsjustjose.crawlnbrawl.Scene;

import com.oitsjustjose.crawlnbrawl.Game;
import com.oitsjustjose.crawlnbrawl.Tile.TileBackground;
import com.oitsjustjose.crawlnbrawl.Util.FileUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Menu extends Scene
{
    private LinkedHashMap<Button, Scene> buttons;
    private Scene nextScene;
    private TrueTypeFont titleFont;
    private TileBackground bg;

    public Menu()
    {
        HashMap<TextAttribute, Integer> titleAttributes = new HashMap<>();
        titleAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        try
        {
            File font = new File("natives/justabit.ttf".replace("/", File.separator)).getAbsoluteFile();
            if (!font.exists())
            {
                System.out.println("Font did not exist; copying...");
                FileUtils.copyStream(ResourceLoader.getResourceAsStream("assets/justabit.ttf"), new File("natives/justabit.ttf".replace("/", File.separator)));
            }

            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, font).deriveFont(titleAttributes).deriveFont(64F);
            titleFont = new TrueTypeFont(awtFont, false);
            bg = new TileBackground("menu");
        }
        catch (IOException | FontFormatException e)
        {
            titleFont = new TrueTypeFont(new Font("Arial", Font.PLAIN, 64).deriveFont(titleAttributes).deriveFont(64F), false);
        }
        try
        {
            Mouse.setNativeCursor(null);
        }
        catch (LWJGLException e)
        {

        }
        buttons = new LinkedHashMap<>();
    }

    public void addButton(Button button, Scene scene)
    {
        buttons.put(button, scene);
    }

    public boolean drawFrame(float delta)
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(0.15F, 0.15F, 0.15F, 0F);
        // Screen resize handler
        if (Display.wasResized())
        {
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        bg.draw();
        titleFont.drawString((Display.getWidth() / 2) - (titleFont.getWidth("Crawl 'n Brawl") / 2), 64F, "Crawl 'n Brawl", new org.newdawn.slick.Color(0, 123, 124));
        updateButtons();
        drawButtons();

        if (Keyboard.isKeyDown(Keyboard.KEY_F2))
        {
            Game.getInstance().screenshotManager.takeScreenshot();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F11))
        {
            if (Display.isFullscreen())
            {
                Game.getInstance().setDisplayMode(Game.getInstance().config.resolutionWidth, Game.getInstance().config.resolutionHeight, false);
            }
            else
            {
                Game.getInstance().setDisplayMode(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, true);
            }
            Display.setResizable(true);
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
            adjustButtons();
        }

        if (Mouse.isButtonDown(0))
        {
            for (Button b : buttons.keySet())
            {
                if (b.isClicked())
                {
                    nextScene = buttons.get(b);
                    if (nextScene == null)
                    {
                        System.exit(0);
                    }
                    return false;
                }
            }
        }

        return true;
    }

    public void updateButtons()
    {
        for (Button b : buttons.keySet())
        {
            b.update();
            if (Display.wasResized())
            {
                adjustButtons();
            }
        }
    }

    public void drawButtons()
    {
        for (Button b : buttons.keySet())
        {
            b.draw();
        }
    }

    public void adjustButtons()
    {
        int buttonCount = buttons.keySet().size();
        for (Button b : buttons.keySet())
        {
            int x = (Display.getWidth() / 2) - (b.getHitBox().getWidth() / 2);
            int y = (Display.getHeight() / 2) - (b.getHitBox().getHeight() * buttonCount) + (b.getHitBox().getHeight() / buttonCount);
            b.getHitBox().setLocation(x, y);
            buttonCount -= 1;
        }
    }

    public Scene nextScene()
    {
        return nextScene;
    }
}