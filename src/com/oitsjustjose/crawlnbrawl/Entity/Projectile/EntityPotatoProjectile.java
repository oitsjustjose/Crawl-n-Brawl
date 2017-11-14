package com.oitsjustjose.crawlnbrawl.Entity.Projectile;

import com.oitsjustjose.crawlnbrawl.Entity.Entity;
import com.oitsjustjose.crawlnbrawl.Entity.Living.EntityMonster;
import com.oitsjustjose.crawlnbrawl.Manager.SceneManager;
import com.oitsjustjose.crawlnbrawl.Scene.SceneLevel;
import com.oitsjustjose.crawlnbrawl.Util.Helper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class EntityPotatoProjectile extends Entity
{
    private Vector2f vel;
    private Rectangle box;
    private float speed = 4f;
    private boolean flip;
    private float wr, hr;
    private Texture sprite;
    private SceneLevel scene = SceneManager.getInstance().getScene();

    public EntityPotatoProjectile(int x, int y, Vector2f vel, boolean flip)
    {
        sprite = Helper.loadTexture("entity/projectile/potato");
        this.box = new Rectangle(x, y, 32, (32 * sprite.getImageHeight() / sprite.getImageWidth()));
        this.wr = 1.0f * sprite.getImageWidth() / sprite.getTextureWidth();
        this.hr = 1.0f * sprite.getImageHeight() / sprite.getTextureHeight();
        this.vel = new Vector2f();
        vel.normalise(this.vel);
        this.flip = flip;
    }

    @Override
    public void update(float delta)
    {
        // Handle off-screen projectiles
        if (speed <= 0 || box.getX() < 0 || box.getX() > Display.getWidth() || box.getY() < 0 || box.getY() > Display.getHeight())
        {
            scene.getProjectileManager().removeProjectile(this);
            return;
        }
        if (speed > 0)
        {
            box.translate((int) (delta * speed * vel.getX()), (int) ((delta * speed * vel.getY())));
            Vector2f.add(vel, new Vector2f(0, .1f), vel);
        }
        // Collision for potatoes
        for (Entity e : scene.getEntityManager().getEntities())
        {
            if (!(e instanceof EntityMonster))
                continue;

            if (this.box.intersects(e.getHitBox()))
            {
                EntityMonster m = (EntityMonster) e;
                m.damage(10F);
                m.setState(EntityMonster.State.HIT);
                scene.getProjectileManager().removeProjectile(this);
                break;
            }
        }
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
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glBegin(GL11.GL_QUADS);

        if (this.flip)
        {
            GL11.glTexCoord2f(0F, 0F);
            GL11.glVertex2f(x + w, y);

            GL11.glTexCoord2f(wr, 0F);
            GL11.glVertex2f(x, y);

            GL11.glTexCoord2f(wr, hr);
            GL11.glVertex2f(x, y + h);

            GL11.glTexCoord2f(0F, hr);
            GL11.glVertex2f(x + w, y + h);
        }
        else
        {
            GL11.glTexCoord2f(0F, 0F);
            GL11.glVertex2f(x, y);

            GL11.glTexCoord2f(wr, 0F);
            GL11.glVertex2f(x + w, y);

            GL11.glTexCoord2f(wr, hr);
            GL11.glVertex2f(x + w, y + h);

            GL11.glTexCoord2f(0F, hr);
            GL11.glVertex2f(x, y + h);
        }

        GL11.glEnd();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public Rectangle getHitBox()
    {
        return this.box;
    }
}
