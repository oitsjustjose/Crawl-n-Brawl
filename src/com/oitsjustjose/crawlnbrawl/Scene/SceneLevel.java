package com.oitsjustjose.crawlnbrawl.Scene;

import com.oitsjustjose.crawlnbrawl.Entity.Living.EntityPlayer;
import com.oitsjustjose.crawlnbrawl.Manager.EntityManager;
import com.oitsjustjose.crawlnbrawl.Manager.ProjectileManager;
import com.oitsjustjose.crawlnbrawl.Manager.SceneManager;
import com.oitsjustjose.crawlnbrawl.Manager.TileManager;

import java.util.Random;

public abstract class SceneLevel extends Scene
{
    public SceneLevel()
    {
        SceneManager.getInstance().setScene(this);
    }

    public abstract EntityPlayer getPlayer();

    public abstract void setPlayer(EntityPlayer player);

    public abstract TileManager getTileManager();

    public abstract EntityManager getEntityManager();

    public abstract ProjectileManager getProjectileManager();

    public Random rand()
    {
        return new Random();
    }
}
