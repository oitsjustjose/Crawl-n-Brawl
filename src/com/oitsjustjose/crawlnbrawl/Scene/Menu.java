package com.oitsjustjose.crawlnbrawl.Scene;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.LinkedHashMap;

public class Menu extends Scene
{
    private LinkedHashMap<GUIButton, Scene> buttons;
    private Scene nextScene;

    public Menu()
    {
        try
        {
            Mouse.setNativeCursor(null);
        }
        catch (LWJGLException e)
        {

        }
        buttons = new LinkedHashMap<>();
    }

    public void addButton(GUIButton button, Scene scene)
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

        updateButtons();
        drawButtons();

        if (Mouse.isButtonDown(0))
        {
            for (GUIButton b : buttons.keySet())
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
        int buttonCount = buttons.keySet().size();

        for (GUIButton b : buttons.keySet())
        {
            b.update();
            if (Display.wasResized())
            {
                System.out.println("fixing buttons");
                int x = (Display.getWidth() / 2) - (b.getHitBox().getWidth() / 2);
                int y = (Display.getHeight() / 2) - (b.getHitBox().getHeight() * buttonCount) - ((buttonCount - buttons.keySet().size()) * 16);
                b.getHitBox().setLocation(x, y);
                buttonCount -= 1;
            }
        }
    }

    public void drawButtons()
    {
        for (GUIButton b : buttons.keySet())
        {
            b.draw();
        }
    }

    public Scene nextScene()
    {
        return nextScene;
    }
}