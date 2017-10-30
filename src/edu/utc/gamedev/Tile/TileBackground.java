package edu.utc.gamedev.Tile;

import edu.utc.gamedev.Util.Helper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.File;
import java.io.IOException;

public class TileBackground implements ITile
{
    private Texture sprite;
    private Rectangle box;
    private Color color;

    public TileBackground(String spriteName)
    {
        sprite = Helper.loadTexture("environment/" + spriteName);
        this.box = new Rectangle(0, 0, sprite.getImageWidth(), sprite.getImageHeight());
        this.color = new Color(255, 255, 255);
    }

    @Override
    public void draw()
    {
        this.box.setWidth(normalizeDimension(Display.getWidth()));
        this.box.setHeight(box.getWidth());

        float x = (float) box.getX();
        float y = (float) box.getY();
        float w = (float) box.getWidth();
        float h = (float) box.getHeight();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTextureID());
        GL11.glColor3f(color.getRed() / 255F, (float) color.getGreen() / 255F, (float) color.getBlue() / 255F);

        // Allows the background to scale w/o blurring
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(0F, 0F);
        GL11.glVertex2f(x, y);

        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex2f(x + w, y);

        GL11.glTexCoord2f(1F, 1F);
        GL11.glVertex2f(x + w, y + h);

        GL11.glTexCoord2f(0F, 1F);
        GL11.glVertex2f(x, y + h);

        GL11.glEnd();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void setSprite(String spriteName)
    {
        Texture temp = sprite;
        try
        {
            temp = TextureLoader.getTexture("png", ResourceLoader.getResourceAsStream("assets/textures/entity/".replace("/", File.separator) + spriteName + ".png"));
        }
        catch (IOException e)
        {
            System.err.println("Image not found, not changing");
        }
        sprite = temp;
    }

    public int normalizeDimension(float dirty)
    {
        int i = 0;
        while (Math.pow(2, i) < dirty)
        {
            i++;
        }
        return (int) Math.pow(2, i);
    }

    @Override
    public Rectangle getHitBox()
    {
        return this.box;
    }

    /**
     * @param c the color overlay to apply to the background
     *          <p>
     *          Changes the background GL color to the specified color
     */
    public TileBackground changeColor(Color c)
    {
        this.color = c;
        return this;
    }

}
