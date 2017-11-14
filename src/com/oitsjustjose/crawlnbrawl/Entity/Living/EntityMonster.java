package com.oitsjustjose.crawlnbrawl.Entity.Living;

import com.oitsjustjose.crawlnbrawl.Entity.Item.EntityHeart;
import com.oitsjustjose.crawlnbrawl.Manager.AudioManager;
import com.oitsjustjose.crawlnbrawl.Manager.SceneManager;
import com.oitsjustjose.crawlnbrawl.Scene.SceneLevel;
import com.oitsjustjose.crawlnbrawl.Tile.Dungeon.TilePlatform;
import com.oitsjustjose.crawlnbrawl.Util.Helper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import java.util.Random;

import static com.oitsjustjose.crawlnbrawl.Entity.Living.EntityMonster.State.HIT;

public class EntityMonster extends EntityLiving
{
    private final float GRAVITY = 3F;
    private State state;
    private Rectangle box;
    private Texture sprite;
    private boolean flip;
    private long lastDamagedTime;
    private Type variant;
    private float health;
    private float wr, hr;
    private float motionMultiplier;
    private StartingSide start;
    private State lastState;
    private SceneLevel scene = SceneManager.getInstance().getScene();

    public EntityMonster(Type variant)
    {
        this.sprite = Helper.loadTexture("entity/" + variant.getResourceName());
        Random rand = scene.rand();
        int xSpawn;
        if (rand.nextInt(2) == 0)
        {
            start = StartingSide.RIGHT;
            xSpawn = (Display.getWidth() + rand.nextInt(256));
        }
        else
        {
            start = StartingSide.LEFT;
            xSpawn = (0 - rand.nextInt(256));
        }
        this.box = new Rectangle(xSpawn, (Display.getHeight() - (sprite.getImageHeight())), (int) variant.getWidth(), (int) (variant.getWidth() * sprite.getImageHeight() / sprite.getImageWidth()));
        this.flip = (xSpawn <= 0);
        this.wr = 1.0f * sprite.getImageWidth() / sprite.getTextureWidth();
        this.hr = 1.0f * sprite.getImageHeight() / sprite.getTextureHeight();
        this.state = State.RIGHT;
        this.variant = variant;
        this.health = variant.getStartingHealth();
    }

    @Override
    public void update(float delta)
    {
        int motion = (int) (variant.getSpeed() * delta);
        int verticalMotion = (int) (motion * motionMultiplier);
        int horizontalMotion = (int) (verticalMotion * 1.25);
        // Handle directional punching properly
        if (SceneManager.getInstance().getScene().getPlayer() != null && SceneManager.getInstance().getScene().getPlayer().getHitBox().getX() > box.getX())
        {
            horizontalMotion *= -1;
        }
        box.setY(variant == Type.BAT ? Display.getHeight() - (box.getHeight() * 5) : Display.getHeight() - box.getHeight());

        switch (state)
        {
            case IDLE:

                if (start == StartingSide.RIGHT)
                {
                    state = State.RIGHT;
                }
                else
                {
                    state = State.LEFT;
                }
                break;
            case RIGHT:
                box.translate(motion, 0);
                if (box.getX() > (Display.getWidth() - box.getWidth()))
                {
                    flip = false;
                    state = State.LEFT;
                }
                break;
            case LEFT:
                box.translate(-motion, 0);
                if (box.getX() <= 0)
                {
                    state = State.RIGHT;
                    flip = true;
                }
                break;
            case HIT:
                // Make large creatures sink into the ground when hit, instead of fly into the air
                if (this.variant.width > 64F)
                {
                    box.translate(horizontalMotion, verticalMotion);
                }
                else
                {
                    box.translate(horizontalMotion, -verticalMotion);
                }
                // Normalize locations to prevent glitchy monsters
                if (box.getX() < 0)
                {
                    box.setX(0);
                }
                if (box.getX() + box.getWidth() > Display.getWidth())
                {
                    box.setX(Display.getWidth() - box.getWidth());
                }
                // Apply Motion!
                if (verticalMotion <= 0)
                {
                    state = State.FALLING;
                }
                else
                {
                    motionMultiplier -= 0.25F;
                }
                break;
            case FALLING:
                box.translate(horizontalMotion, verticalMotion);
                // Normalize locations to prevent glitchy monsters
                if (box.getX() < 0)
                {
                    box.setX(0);
                }
                if (box.getX() + box.getWidth() > Display.getWidth())
                {
                    box.setX(Display.getWidth() - box.getWidth());
                }
                // Apply Motion!
                if (getEntityPlatform() != null)
                {
                    // Fixes the player's feet being past the platform
                    box.setY(getEntityPlatform().getHitBox().getY() - (box.getHeight()));
                    state = lastState;
                }
                else
                {
                    if (motionMultiplier < GRAVITY)
                    {
                        motionMultiplier += 0.25F;
                    }
                }
                break;
        }
        if (scene.getPlayer() != null && box.intersects(scene.getPlayer().getHitBox()))
        {
            if (scene.getPlayer().getState() != EntityPlayer.State.DEAD && scene.getPlayer().getState() != EntityPlayer.State.STUNNED)
            {
                scene.getPlayer().damage(variant.getDamageToPlayer());
            }
        }
    }

    public TilePlatform getEntityPlatform()
    {
        if (SceneManager.getInstance().getScene() instanceof SceneLevel)
        {
            for (TilePlatform t : SceneManager.getInstance().getScene().getTileManager().getPlatforms())
            {
                if (t.entityStanding(this))
                {
                    return t;
                }
            }
        }
        return null;
    }

    public long getLastDamagedTime()
    {
        return lastDamagedTime;
    }

    public void setInvulnerableTime()
    {
        lastDamagedTime = Helper.getTime();
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

        if (state == HIT)
        {
            GL11.glColor3f(180 / 255F, 0F, 3 / 255F);
        }
        if (flip)
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
    public float getMaxHealth()
    {
        return variant.startingHealth;
    }

    @Override
    public Rectangle getHitBox()
    {
        return box;
    }

    @Override
    public void kill()
    {
        // Monsters have a 25% chance of dropping a heart only if the player needs it
        if (scene.rand().nextInt(4) == 0 && scene.getPlayer().getHealth() <= 10F)
        {
            scene.getEntityManager().addEntity(new EntityHeart(box.getX() + (box.getWidth() / 2), box.getY()));
        }

        scene.getEntityManager().removeEntity(this);
    }

    @Override
    public void damage(float amount)
    {
        // Don't allow entities to be damaged multiple times
        if (state == State.HIT || state == State.FALLING)
        {
            return;
        }
        health -= amount;
        if (health > 0F)
        {
            AudioManager.getInstance().play("damage_hit" + (scene.rand().nextInt(10) + 1));
            lastState = state;
            motionMultiplier = GRAVITY;
            state = HIT;
        }
        else
        {
            kill();
            AudioManager.getInstance().play(variant.getResourceName() + "_death");
        }
    }

    public float getHealth()
    {
        return health;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State s)
    {
        state = s;
    }

    public enum State
    {
        IDLE, LEFT, RIGHT, HIT, FALLING
    }

    public enum StartingSide
    {
        LEFT, RIGHT
    }

    public enum Type
    {
        ORC(0.25F, 20F, 1F, 92F, "orc"),
        GUT_EATER(0.15F, 40F, 1.5F, 192F, "gut_eater"),
        BAT(0.75F, 1F, 0.25F, 48F, "bat"),
        ZOMBIE(0.15F, 18F, 0.75F, 64F, "zombie"),
        FLAME(1F, 2F, 0.5F, 30F, "flame"),
        ROCK_MONSTER(0.2F, 20F, 0.4F, 60F, "rock_monster"),
        ICE_MONSTER(0.65F, 10F, 0.5F, 48F, "ice_monster"),
        DARK_MAGE(0.5F, 14F, 0.75F, 64F, "dark_mage"),
        OOZER(0.15F, 32F, 0.8F, 40F, "oozer");

        private final float speed;
        private final float startingHealth;
        private final float damageToPlayer;
        private final float width;
        private final String resourceName;

        Type(float speed, float health, float damage, float width, String resourceName)
        {
            this.speed = speed;
            this.startingHealth = health;
            this.damageToPlayer = damage;
            this.resourceName = resourceName;
            this.width = width;
        }

        public float getSpeed()
        {
            return speed;
        }

        public float getStartingHealth()
        {
            return startingHealth;
        }

        public float getDamageToPlayer()
        {
            return damageToPlayer;
        }

        public String getResourceName()
        {
            return resourceName;
        }

        public float getWidth()
        {
            return width;
        }
    }
}


