package com.oitsjustjose.crawlnbrawl.Manager;

import com.oitsjustjose.crawlnbrawl.Tile.Dungeon.TileDecoration;
import com.oitsjustjose.crawlnbrawl.Tile.Dungeon.TilePlatform;
import com.oitsjustjose.crawlnbrawl.Tile.ITile;

import java.util.LinkedList;

public class TileManager
{
    private LinkedList<ITile> tiles;
    private LinkedList<TileDecoration> decorations;
    private LinkedList<TilePlatform> platforms;

    public TileManager()
    {
        tiles = new LinkedList<>();
        decorations = new LinkedList<>();
        platforms = new LinkedList<>();
    }

    public void drawAll()
    {
        for (ITile t : tiles)
        {
            t.draw();
        }
        for (TileDecoration t : decorations)
        {
            t.draw();
        }
        for (TilePlatform t : platforms)
        {
            t.draw();
            t.update();
        }
    }

    public void addTile(ITile t)
    {
        if (t instanceof TileDecoration)
        {
            this.decorations.add((TileDecoration) t);
        }
        else if (t instanceof TilePlatform)
        {
            this.platforms.add((TilePlatform) t);
        }
        else
        {
            this.tiles.add(t);
        }
    }

    public void addAll(Iterable<ITile> tiles)
    {
        for (ITile t : tiles)
        {
            this.addTile(t);
        }
    }

    public void removeTile(ITile t)
    {
        this.tiles.remove(t);
    }

    public void removeDecoration(TileDecoration t)
    {
        this.decorations.remove(t);
    }

    public LinkedList<TileDecoration> getDecorations()
    {
        return (LinkedList<TileDecoration>) this.decorations.clone();
    }

    public void clearDecorations()
    {
        this.decorations.clear();
    }

    public LinkedList<TilePlatform> getPlatforms()
    {
        return (LinkedList<TilePlatform>) this.platforms.clone();
    }
}
