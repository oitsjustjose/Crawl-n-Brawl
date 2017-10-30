package edu.utc.gamedev.Tile.Dungeon;

import edu.utc.gamedev.Entity.Entity;
import edu.utc.gamedev.Tile.ITile;
import org.lwjgl.util.Rectangle;

public abstract class TilePlatform implements ITile
{
    @Override
    public abstract void draw();

    @Override
    public abstract Rectangle getHitBox();

    public abstract void update();

    public boolean entityStanding(Entity e)
    {
        Rectangle entityHitBox = new Rectangle(e.getHitBox().getX(), e.getHitBox().getY(), e.getHitBox().getWidth(), e.getHitBox().getHeight());
        entityHitBox.translate(0, 1);
        return entityHitBox.intersects(getHitBox());
    }
}
