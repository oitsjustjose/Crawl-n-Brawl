package edu.utc.gamedev.Manager;

import edu.utc.gamedev.Entity.Projectile.EntityPotatoProjectile;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;

public class ProjectileManager
{
    private LinkedList<EntityPotatoProjectile> projectiles;

    public ProjectileManager()
    {
        projectiles = new LinkedList<>();
    }

    public void drawAll()
    {
        for (EntityPotatoProjectile e : projectiles)
        {
            e.draw();
        }
    }

    public void updateAll(float delta)
    {
        try
        {
            for (EntityPotatoProjectile e : projectiles)
            {
                e.update(delta);
            }
        }
        catch (ConcurrentModificationException e)
        {
            // We do nothing because nothing can be done ¯\_(ツ)_/¯
        }
    }

    public void addProjectile(EntityPotatoProjectile e)
    {
        projectiles.add(e);
    }

    public void removeProjectile(EntityPotatoProjectile e)
    {
        projectiles.remove(e);
    }

    public LinkedList<EntityPotatoProjectile> getProjectiles()
    {
        return this.projectiles;
    }
}
