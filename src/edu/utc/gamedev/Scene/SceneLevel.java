package edu.utc.gamedev.Scene;

import edu.utc.gamedev.Entity.Living.EntityPlayer;
import edu.utc.gamedev.Manager.EntityManager;
import edu.utc.gamedev.Manager.ProjectileManager;
import edu.utc.gamedev.Manager.SceneManager;
import edu.utc.gamedev.Manager.TileManager;

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
