package edu.utc.gamedev.Tile;

import org.lwjgl.util.Rectangle;

public interface ITile
{
    void draw();

    Rectangle getHitBox();
}
