package edu.utc.gamedev.Tile.Surface;

import edu.utc.gamedev.Tile.ITile;
import edu.utc.gamedev.Util.Helper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import java.io.File;


public class TileGrass implements ITile
{
    private Texture sprite;
    private Rectangle box;
    public TileGrass(int x)
    {
        sprite = Helper.loadTexture("environment/grass".replace("/", File.separator));
        this.box = new Rectangle(x, 0, sprite.getTextureWidth(), sprite.getTextureHeight());
        this.box.setY(Display.getHeight() - this.box.getHeight());
    }

    @Override
    public void draw()
    {
        if (this.box.getY() != Display.getHeight() - this.box.getHeight())
        {
            this.box.setY(Display.getHeight() - this.box.getHeight());
        }

        float x = (float) box.getX();
        float y = (float) box.getY();
        float w = (float) box.getWidth();
        float h = (float) box.getHeight();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTextureID());

        GL11.glColor3f(255F, 255F, 255F);

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

    @Override
    public Rectangle getHitBox()
    {
        return this.box;
    }
}
