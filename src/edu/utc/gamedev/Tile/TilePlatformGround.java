package edu.utc.gamedev.Tile;

import edu.utc.gamedev.Tile.Dungeon.TilePlatform;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

public class TilePlatformGround extends TilePlatform
{
    private Rectangle box;

    public TilePlatformGround()
    {
        this.box = new Rectangle(0, Display.getHeight(), Display.getWidth(), 4);
    }

    @Override
    public void update()
    {
        // Update the bounding box so that it stays aligned with the display as it moves / resizes
        this.box.setY(Display.getHeight());
        this.box.setWidth(Display.getWidth());
    }

    @Override
    public void draw()
    {
        // Nothing to do here; no sense in drawing the ground, honestly... we don't see it
    }

    @Override
    public Rectangle getHitBox()
    {
        return this.box;
    }
}
