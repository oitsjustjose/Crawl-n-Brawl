package edu.utc.gamedev.Item;

import edu.utc.gamedev.Entity.Living.EntityPlayer;
import edu.utc.gamedev.Entity.Projectile.EntityPotatoProjectile;
import edu.utc.gamedev.Manager.SceneManager;
import edu.utc.gamedev.Util.Helper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

public class ItemPotatoGun extends Item
{
    private long lastShot;

    public ItemPotatoGun()
    {
        super("potato_gun");
    }

    @Override
    public void onUse()
    {
        if (SceneManager.getInstance().getScene().getPlayer().getState() == EntityPlayer.State.STUNNED)
            return;

        if (Helper.getTime() - lastShot > getCooldownTime())
        {
            Rectangle r = SceneManager.getInstance().getScene().getPlayer().getHitBox();
            int mx = Mouse.getX();
            int my = Display.getHeight() - Mouse.getY();

            int bx = r.getX() + (r.getWidth() / 2);
            int by = r.getY() + 8;

            Vector2f vel = new Vector2f(mx - bx, my - by);
            SceneManager.getInstance().getScene().getProjectileManager().addProjectile(new EntityPotatoProjectile(bx, by, vel, (my < r.getY())));
            lastShot = Helper.getTime();
        }
    }

    @Override
    public int getCooldownTime()
    {
        return 240;
    }

    @Override
    public boolean limitUseDistance()
    {
        return false;
    }
}
