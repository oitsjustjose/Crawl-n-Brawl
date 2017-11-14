package com.oitsjustjose.crawlnbrawl.Tile;

import com.oitsjustjose.crawlnbrawl.Tile.Dungeon.TilePlatform;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public class TilePlatformBase extends TilePlatform
{
    public Rectangle box;
    int w, h, x, y;

    public TilePlatformBase()
    {
        super();

        this.w = 96;
        this.h = 8;
        this.x = Display.getWidth() / 2 - (w / 2);
        this.y = Display.getHeight() - 96;
        this.box = new Rectangle(x, y, w, h);
    }

    @Override
    public Rectangle getHitBox()
    {
        return this.box;
    }

    @Override
    public void update()
    {
        if (this.box.getX() != (Display.getWidth() / 2 - (w / 2)) || this.box.getY() != (Display.getHeight() - 96))
        {
            this.x = Display.getWidth() / 2 - (w / 2);
            this.y = Display.getHeight() - 96;
            this.box.setLocation(x, y);
        }
    }

    @Override
    public void draw()
    {
        float x = (float) box.getX();
        float y = (float) box.getY();
        float w = (float) box.getWidth();
        float h = (float) box.getHeight();

        GL11.glColor3f(1F, 1F, 1F);

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
}
