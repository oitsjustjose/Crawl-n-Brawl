package com.oitsjustjose.crawlnbrawl.Scene;

import com.oitsjustjose.crawlnbrawl.Manager.AudioManager;
import com.oitsjustjose.crawlnbrawl.Util.FileUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class Button
{
    private String buttonText;
    private Rectangle box;
    private Color color;
    private Color textColor;
    private TrueTypeFont font;
    private boolean hovering;

    public Button(int x, int y, int w, int h, String text)
    {
        this(x, y, w, h, text, new Color(0, 0, 0), new Color(0.9F, 0.9F, 0.9F));
    }

    public Button(int x, int y, int w, int h, String text, Color textColor, Color color)
    {
        this.box = new Rectangle(x, y, w, h);
        this.buttonText = text;
        this.color = color;
        this.textColor = textColor;
        initFont();
    }

    public void initFont()
    {
        try
        {
            File fontFile = new File("natives/justabit.ttf".replace("/", File.separator)).getAbsoluteFile();
            if (!fontFile.exists())
            {
                System.out.println("Font did not exist; copying...");
                FileUtils.copyStream(ResourceLoader.getResourceAsStream("assets/justabit.ttf"), new File("natives/justabit.ttf".replace("/", File.separator)));
            }

            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(32F);
            font = new TrueTypeFont(awtFont, false);
        }
        catch (IOException | FontFormatException e)
        {
            font = new TrueTypeFont(new Font("Arial", Font.PLAIN, 32), false);
        }
    }

    public boolean isClicked()
    {
        Rectangle mouse = new Rectangle(Mouse.getX(), Display.getHeight() - Mouse.getY(), 1, 1);
        if (box.intersects(mouse) && Mouse.isButtonDown(0))
        {
            AudioManager.getInstance().play("menu_select");
            // Sleep before processing the action
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
            }
            return true;
        }
        return false;
    }

    public void update()
    {
        Rectangle mouse = new Rectangle(Mouse.getX(), Display.getHeight() - Mouse.getY(), 1, 1);
        this.hovering = box.intersects(mouse);
    }

    public void draw()
    {
        TextureImpl.bindNone();
        float x = (float) box.getX();
        float y = (float) box.getY();
        float w = (float) box.getWidth();
        float h = (float) box.getHeight();

        if (this.hovering)
        {
            GL11.glColor3f(color.r + (0.1F), color.g + (0.1F), color.b + (0.1F));
        }
        else
        {
            GL11.glColor3f(color.r, color.g, color.b);
        }
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);
        GL11.glEnd();

        font.drawString(x + (w / 2) - (font.getWidth(buttonText) / 2), y + (h / 2) - (font.getHeight(buttonText) / 2), buttonText, textColor);
    }

    public Rectangle getHitBox()
    {
        return box;
    }
}
