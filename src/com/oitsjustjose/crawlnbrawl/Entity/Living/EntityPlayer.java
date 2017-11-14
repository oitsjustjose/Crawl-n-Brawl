package com.oitsjustjose.crawlnbrawl.Entity.Living;

import com.oitsjustjose.crawlnbrawl.Item.Item;
import com.oitsjustjose.crawlnbrawl.Item.ItemFist;
import com.oitsjustjose.crawlnbrawl.Manager.AudioManager;
import com.oitsjustjose.crawlnbrawl.Manager.SceneManager;
import com.oitsjustjose.crawlnbrawl.Scene.SceneLevel;
import com.oitsjustjose.crawlnbrawl.Tile.Dungeon.TilePlatform;
import com.oitsjustjose.crawlnbrawl.Util.Helper;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class EntityPlayer extends EntityLiving
{
    private final float GRAVITY = 3.5F;
    private Rectangle box;
    private Texture sprite;
    private float speed;
    private State state;
    private boolean flip;
    private long stunTime;
    private float health;
    private float maxHealth;
    private float motionMultiplier;
    private float wr, hr;
    private Item heldItem;

    public EntityPlayer(int x, int y)
    {
        sprite = Helper.loadTexture("entity/player");
        box = new Rectangle(x, y, 32, (32 * sprite.getImageHeight() / sprite.getImageWidth()));
        this.wr = 1.0f * sprite.getImageWidth() / sprite.getTextureWidth();
        this.hr = 1.0f * sprite.getImageHeight() / sprite.getTextureHeight();
        this.state = State.FALLING;
        this.flip = false;
        this.health = 20F;
        this.maxHealth = 20F;
        this.speed = .6F;
        this.heldItem = new ItemFist().setCursor();
    }

    public EntityPlayer()
    {
        this(0, 0);
    }


    @Override
    public float getMaxHealth()
    {
        return this.maxHealth;
    }

    @Override
    public void update(float delta)
    {
        int motion = (int) (this.speed * delta);
        int verticalMotion = (int) (motion * motionMultiplier);
        if (Keyboard.isKeyDown(Keyboard.KEY_A) && this.box.getX() > 0 && !(this.state == State.STUNNED))
        {
            this.flip = true;
            box.translate(-motion, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D) && this.box.getX() <= (Display.getWidth() - box.getWidth()) && !(this.state == State.STUNNED))
        {
            this.flip = false;
            box.translate(motion, 0);
        }

        // Fix the player from being off the screen at any time
        if (this.box.getX() < 0 || this.box.getX() > Display.getWidth())
        {
            this.box.setX(0);
        }
        if (this.box.getY() < 0 || this.box.getY() > Display.getHeight())
        {
            this.box.setY(Display.getHeight() - this.box.getHeight());
        }


        switch (state)
        {
            case IDLE:
                motionMultiplier = 0.0F;
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                {
                    AudioManager.getInstance().play("player_jump");
                    motionMultiplier = GRAVITY;
                    state = State.JUMPING;
                }
                // Makes our player fall off of platforms
                else if (getPlayerPlatform() == null)
                {
                    state = State.FALLING;
                }
                break;
            case JUMPING:
                box.translate(0, -verticalMotion);
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
                box.translate(0, verticalMotion);
                if (getPlayerPlatform() != null)
                {
                    // Fixes the player's feet being past the platform
                    box.setY(getPlayerPlatform().getHitBox().getY() - (box.getHeight()));
                    state = State.IDLE;
                }
                else
                {
                    if (motionMultiplier < GRAVITY)
                    {
                        motionMultiplier += 0.25F;
                    }
                }
                break;
            case STUNNED:
                if (Helper.getTime() - this.stunTime > (40 * delta))
                {
                    if (getPlayerPlatform() == null)
                    {
                        this.state = State.FALLING;
                    }
                    else
                    {
                        this.state = State.IDLE;
                    }
                }
                break;
            case DEAD:
                kill();
                break;
        }
        if (!Mouse.isCreated() || this.heldItem == null)
        {
            return;
        }
        while (Mouse.next())
        {

            if ((!this.heldItem.limitUseDistance() && Mouse.getEventButtonState()))
            {
                this.heldItem.onUse();
            }
            if (Mouse.getEventButtonState() && Math.abs(this.box.getX() - Mouse.getX()) < 384)
            {
                if (Math.abs(this.box.getY() - (Display.getHeight() - Mouse.getY())) < 192)
                {
                    this.heldItem.onUse();
                }
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
        GL11.glColor3f(255F, 255F, 255F);
        GL11.glBegin(GL11.GL_QUADS);

        // So that the player renders red when hurt:
        if (this.state == State.STUNNED)
        {
            GL11.glColor3f(.8F, 0F, 0F);
        }
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
    public void kill()
    {
        if (SceneManager.getInstance().getScene() instanceof SceneLevel)
        {
            ((SceneLevel) SceneManager.getInstance().getScene()).setPlayer(null);
        }
    }

    @Override
    public Rectangle getHitBox()
    {
        return this.box;
    }

    public State getState()
    {
        return this.state;
    }

    @Override
    public float getHealth()
    {
        return this.health;
    }

    @Override
    public void damage(float amount)
    {
        this.health -= amount;

        // Normalize health; player should have no more than 20 health
        if (this.health > maxHealth)
            this.health = maxHealth;

        // Do nothing else if we're just "healing"
        if (amount <= 0)
            return;

        if (this.health > 0F)
        {
            this.stunTime = Helper.getTime();
            this.state = State.STUNNED;
            AudioManager.getInstance().play("player_hit" + (SceneManager.getInstance().getScene().rand().nextInt(2) + 1));
        }
        else
        {
            this.state = State.DEAD;
            AudioManager.getInstance().play("player_death");
        }
    }

    public Item getHeldItem()
    {
        return this.heldItem;
    }

    public void setHeldItem(Item item)
    {
        if (item == null)
        {
            this.heldItem = null;
            try
            {
                Mouse.setNativeCursor(null);
            }
            catch (LWJGLException lwjglEx)
            {
                return;
            }
        }
        this.heldItem = item;
    }

    public TilePlatform getPlayerPlatform()
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

    public enum State
    {
        IDLE, JUMPING, FALLING, STUNNED, DEAD
    }
}
