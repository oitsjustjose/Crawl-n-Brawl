package edu.utc.gamedev.Item.Sword;

import edu.utc.gamedev.Entity.Living.EntityMonster;
import edu.utc.gamedev.Entity.Entity;
import edu.utc.gamedev.Entity.Living.EntityPlayer;
import edu.utc.gamedev.Item.Item;
import edu.utc.gamedev.Manager.AudioManager;
import edu.utc.gamedev.Manager.SceneManager;
import edu.utc.gamedev.Util.Helper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

public abstract class ItemSword extends Item
{
    private float damage;

    public ItemSword(float damage, String resourceFile)
    {
        super(resourceFile);
        this.damage = damage;
    }

    @Override
    public void onUse()
    {
        if (SceneManager.getInstance().getScene().getPlayer().getState() == EntityPlayer.State.STUNNED)
            return;
        int mouseX = Mouse.getX();
        int mouseY = Display.getHeight() - Mouse.getY();

        Rectangle attackRect = new Rectangle(mouseX, mouseY, textureWidth, textureHeight);

        for (Entity e : SceneManager.getInstance().getScene().getEntityManager().getEntities())
        {
            if (e instanceof EntityMonster)
            {
                EntityMonster m = (EntityMonster) e;
                if (m.getHitBox().intersects(attackRect) && m.getState() != EntityMonster.State.HIT && Helper.getTime() - m.getLastDamagedTime() > getCooldownTime())
                {
                    AudioManager.getInstance().play("sword_slash");
                    m.damage(damage);
                    m.setState(EntityMonster.State.HIT);
                }
            }
        }
    }

    @Override
    public int getCooldownTime()
    {
        return 800;
    }
}
