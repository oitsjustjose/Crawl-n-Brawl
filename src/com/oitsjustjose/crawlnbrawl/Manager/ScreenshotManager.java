package com.oitsjustjose.crawlnbrawl.Manager;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotManager
{
    private File screenshotsFolder;

    public ScreenshotManager()
    {
        screenshotsFolder = new File(System.getProperty("user.dir") + File.separator + "screenshots");
    }


    public File getScreenshotsFolder()
    {
        return this.screenshotsFolder;
    }

    public void setScreenshotsFolder(File newFolder)
    {
        this.screenshotsFolder = newFolder;
    }

    /**
     * SOURCE: http://wiki.lwjgl.org/wiki/Taking_Screen_Shots.html
     */
    public void takeScreenshot()
    {
        GL11.glReadBuffer(GL11.GL_FRONT);
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();
        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        if (!this.screenshotsFolder.exists())
        {
            this.screenshotsFolder.mkdir();
        }

        String format = "png";
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy HH mm ss");
        Date resultdate = new Date(timestamp);
        File file = new File(this.screenshotsFolder.getAbsolutePath() + File.separator + dateFormat.format(resultdate) + "." + format);
        System.out.println(file.getAbsolutePath());
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        try
        {
            ImageIO.write(image, format, file);
        }
        catch (IOException e)
        {
            System.out.println("Error capturing screenshot");
        }
    }
}
