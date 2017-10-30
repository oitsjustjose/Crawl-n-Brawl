package edu.utc.gamedev.Entity.Living;

import edu.utc.gamedev.Entity.Entity;
import edu.utc.gamedev.Manager.SceneManager;
import org.lwjgl.opengl.GL11;

public abstract class EntityLiving extends Entity
{
    public float getHealth()
    {
        return 0F;
    }

    public float getMaxHealth()
    {
        return 0F;
    }


    public void damage(float amount)
    {

    }

    public void drawHealth()
    {
        float amtToDraw = getHealth() / getMaxHealth();

        float x = (float) this.getHitBox().getX();
        float y = (float) this.getHitBox().getY() - 12;
        float w = (float) this.getHitBox().getWidth();
        float h = 6F;

        GL11.glColor3f(0F, 0F, 0F);
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);

        GL11.glEnd();

        w = this.getHitBox().getWidth() * amtToDraw;

        float rAmt = Math.abs((amtToDraw * 255F - 255F) / 255F);
        float gAmt = amtToDraw;

        GL11.glColor3f(rAmt, gAmt, 0);
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);

        GL11.glEnd();
    }

    public void kill()
    {
        SceneManager.getInstance().getScene().getEntityManager().removeEntity(this);
    }
}


