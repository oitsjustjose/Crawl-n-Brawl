package edu.utc.gamedev.Entity.Item;

import edu.utc.gamedev.Item.ItemHeart;
import edu.utc.gamedev.Manager.SceneManager;
import edu.utc.gamedev.Scene.SceneLevel;
import edu.utc.gamedev.Util.Helper;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

public class EntityHeart extends EntityItem
{
    private SceneLevel scene = SceneManager.getInstance().getScene();

    public EntityHeart(int x, int y)
    {
        super(new ItemHeart(), x, y);
        this.box = new Rectangle(x, y, sprite.getTextureWidth(), sprite.getTextureHeight());
        state = State.IDLE;
    }

    @Override
    public void update(float delta)
    {
        // Handle hitbox intersection
        if (scene.getPlayer() != null && this.box.intersects(scene.getPlayer().getHitBox()))
        {
            scene.getPlayer().damage(-10F);
            scene.getEntityManager().removeEntity(this);
        }
        if (Helper.getTime() - spawnTime >= 9000)
        {
            scene.getEntityManager().removeEntity(this);
        }

        int motion = 1;
        if (state != State.IDLE && Helper.getTime() - lastMoved < 256)
            return;
        lastMoved = Helper.getTime();
        switch (state)
        {
            case FALLING:
                box.translate(0, motion);
                if (box.getY() >= (Display.getHeight() - (box.getHeight())))
                {
                    box.setY(Display.getHeight() - (box.getHeight()));
                    state = State.RISING;
                }
                break;
            case RISING:
                box.translate(0, -motion);
                if (box.getY() <= (Display.getHeight() - (box.getHeight() * 1.05)))
                {
                    state = State.FALLING;
                }
                break;
            case IDLE:
                box.translate(0, motion * 4);
                if (box.getY() >= (Display.getHeight() - (box.getHeight())))
                {
                    box.setY(Display.getHeight() - (box.getHeight()));
                    state = State.RISING;
                }
                break;
        }
    }
}