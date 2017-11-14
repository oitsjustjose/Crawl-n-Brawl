package com.oitsjustjose.crawlnbrawl.Entity.Item;

import com.oitsjustjose.crawlnbrawl.Entity.Entity;
import com.oitsjustjose.crawlnbrawl.Item.Item;
import com.oitsjustjose.crawlnbrawl.Manager.SceneManager;
import com.oitsjustjose.crawlnbrawl.Util.Helper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class EntityItem extends Entity
{
    protected Item actualItem;
    protected Rectangle box;
    protected Texture sprite;
    protected float wr, hr;
    protected State state;
    protected long spawnTime;
    protected long lastMoved;
    protected int lifespan;

    public EntityItem(Item item, int x, int y, int lifespan)
    {
        this.actualItem = item;
        sprite = Helper.loadTexture("items/" + item.getIconString());
        this.box = new Rectangle(x, y, 64, (64 * sprite.getImageHeight() / sprite.getImageWidth()));
        this.box.setY(y - box.getHeight());
        this.wr = 1.0f * sprite.getImageWidth() / sprite.getTextureWidth();
        this.hr = 1.0f * sprite.getImageHeight() / sprite.getTextureHeight();
        this.state = State.IDLE;
        this.spawnTime = Helper.getTime();
        this.lifespan = lifespan;
    }

    public EntityItem(Item item, int x, int y)
    {
        this(item, x, y, 9000);
    }

    @Override
    public void draw()
    {
        float x = (float) box.getX();
        float y = (float) box.getY();
        float w = (float) box.getWidth();
        float h = (float) box.getHeight();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTextureID());
        GL11.glColor3f(1F, 1F, 1F);
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(0F, 0F);
        GL11.glVertex2f(x, y);

        GL11.glTexCoord2f(wr, 0F);
        GL11.glVertex2f(x + w, y);

        GL11.glTexCoord2f(wr, hr);
        GL11.glVertex2f(x + w, y + h);

        GL11.glTexCoord2f(0F, hr);
        GL11.glVertex2f(x, y + h);

        GL11.glEnd();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void update(float delta)
    {
        // Handle hitbox intersection
        if (SceneManager.getInstance().getScene().getPlayer() != null && this.box.intersects(SceneManager.getInstance().getScene().getPlayer().getHitBox()))
        {
            SceneManager.getInstance().getScene().getPlayer().setHeldItem(this.actualItem.setCursor());
            SceneManager.getInstance().getScene().getEntityManager().removeEntity(this);
        }
        if (this.lifespan > 0 && Helper.getTime() - spawnTime >= this.lifespan)
        {
            SceneManager.getInstance().getScene().getEntityManager().removeEntity(this);
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

    public enum State
    {
        FALLING, RISING, IDLE
    }
}
