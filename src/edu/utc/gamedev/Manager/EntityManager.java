package edu.utc.gamedev.Manager;

import edu.utc.gamedev.Entity.Entity;
import edu.utc.gamedev.Entity.Living.EntityLiving;
import edu.utc.gamedev.Entity.Living.EntityMonster;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;

public class EntityManager
{
    private LinkedList<Entity> entities;

    public EntityManager()
    {
        entities = new LinkedList<>();
    }

    public void drawAll()
    {
        for (Entity e : entities)
        {
            e.draw();
            if (e instanceof EntityLiving)
            {
                ((EntityLiving) e).drawHealth();
            }
        }
    }

    public void updateAll(float delta)
    {
        try
        {
            for (Entity e : entities)
            {
                e.update(delta);
            }
        }
        catch (ConcurrentModificationException e)
        {
            // We do nothing because nothing can be done ¯\_(ツ)_/¯
        }
    }

    public void addEntity(Entity e)
    {
        entities.add(e);
    }

    public void removeEntity(Entity e)
    {
        entities.remove(e);
    }

    public LinkedList<Entity> getEntities()
    {
        return this.entities;
    }

    public boolean allMonstersDead()
    {
        for (Entity e : entities)
        {
            if (e instanceof EntityMonster)
            {
                return false;
            }
        }
        return true;
    }
}
