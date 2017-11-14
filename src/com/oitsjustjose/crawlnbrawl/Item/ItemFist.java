package com.oitsjustjose.crawlnbrawl.Item;

import com.oitsjustjose.crawlnbrawl.Entity.Entity;
import com.oitsjustjose.crawlnbrawl.Entity.Living.EntityMonster;
import com.oitsjustjose.crawlnbrawl.Entity.Living.EntityPlayer;
import com.oitsjustjose.crawlnbrawl.Manager.SceneManager;
import com.oitsjustjose.crawlnbrawl.Util.Helper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

public class ItemFist extends Item
{

    public ItemFist()
    {
        super("fist");
    }

    public int getCooldownTime()
    {
        return 1000;
    }

    @Override
    public void onUse()
    {
        if (SceneManager.getInstance().getScene().getPlayer() == null || SceneManager.getInstance().getScene().getPlayer().getState() == EntityPlayer.State.STUNNED)
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
                    m.damage(2F);
                    ((EntityMonster) e).setState(EntityMonster.State.HIT);
                }
            }
        }
    }
}
