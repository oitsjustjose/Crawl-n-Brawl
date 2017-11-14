package com.oitsjustjose.crawlnbrawl.Item;

import com.oitsjustjose.crawlnbrawl.Util.Helper;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.util.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

public class Item
{
    private final String iconString;
    public int textureWidth;
    public int textureHeight;

    public Item(String resourceFile)
    {
        this.iconString = resourceFile;
    }

    public int getCooldownTime()
    {
        return 40;
    }

    public String getIconString()
    {
        return this.iconString;
    }


    /**
     * @throws LWJGLException if the cursor cannot be created or changed
     * @throws IOException    if the cursor file cannot be found
     *                        <p>
     *                        Changes the cursor to a little sword!
     *                        SOURCE: https://goo.gl/Boi1AP
     */
    public Item setCursor()
    {

        InputStream stream = ResourceLoader.getResourceAsStream("assets/textures/items/".replace("/", File.separator) + iconString + ".png");
        BufferedImage cursorImg;
        try
        {
            cursorImg = ImageIO.read(stream);
            Mouse.create();
        }
        catch (IOException e)
        {
            try
            {
                cursorImg = ImageIO.read(ResourceLoader.getResourceAsStream(Helper.errTexture()));
            }
            catch (IOException whatTheFuck)
            {
                throw new RuntimeException(whatTheFuck);
            }
        }
        catch (LWJGLException e)
        {
            System.err.println("There was an issue creating the mouse");
            System.err.println(e.getMessage());
            return this;
        }

        textureWidth = cursorImg.getWidth();
        textureHeight = cursorImg.getHeight();

        int rgbData[] = new int[textureWidth * textureHeight];

        for (int i = 0; i < rgbData.length; i++)
        {
            int x = i % textureWidth;
            int y = textureHeight - 1 - i / textureWidth; // this will also flip the image vertically

            rgbData[i] = cursorImg.getRGB(x, y);
        }

        IntBuffer buffer = BufferUtils.createIntBuffer(textureWidth * textureHeight);
        buffer.put(rgbData);
        buffer.rewind();

        try
        {
            Cursor cursor = new Cursor(textureWidth, textureHeight, 2, textureHeight - 2, 1, buffer, null);
            Mouse.setNativeCursor(cursor);
        }
        catch (LWJGLException e)
        {
            System.err.println("There was an issue setting the custom mouse cursor");
            System.err.println(e.getMessage());
        }

        return this;
    }

    public void onUse()
    {
    }

    public boolean limitUseDistance()
    {
        return true;
    }
}
