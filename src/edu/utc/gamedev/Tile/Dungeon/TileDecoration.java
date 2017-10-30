package edu.utc.gamedev.Tile.Dungeon;

import edu.utc.gamedev.Manager.SceneManager;
import edu.utc.gamedev.Scene.Dungeon;
import edu.utc.gamedev.Scene.Scene;
import edu.utc.gamedev.Tile.ITile;
import edu.utc.gamedev.Util.Helper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import java.io.File;
import java.util.Random;


public class TileDecoration implements ITile
{
    private Texture sprite;
    private Rectangle box;
    private Color color;

    public TileDecoration(String spriteName, int numberOfVariants)
    {
        Random rand = new Random();
        if (numberOfVariants > 0)
        {

            int spriteVariant = numberOfVariants > 1 ? rand.nextInt(numberOfVariants) + 1 : 1;
            sprite = Helper.loadTexture("environment/".replace("/", File.separator) + spriteName + spriteVariant);
        }
        else
        {
            sprite = Helper.loadTexture("environment/".replace("/", File.separator) + spriteName);
        }
        int xLoc = rand.nextInt(Display.getWidth() / sprite.getTextureWidth()) * sprite.getTextureWidth();
        int yLoc = rand.nextInt(Display.getHeight() / sprite.getTextureHeight()) * sprite.getTextureHeight();
        this.box = new Rectangle(xLoc, yLoc, sprite.getTextureWidth(), sprite.getTextureHeight());
        this.color = new Color(255, 255, 255);
    }

    @Override
    public void draw()
    {
        Scene s = SceneManager.getInstance().getScene();
        if (!(s instanceof Dungeon))
        {
            return;
        }
        Dungeon dungeon = (Dungeon) s;
        int scaleFactor = (dungeon.getBackground().getHitBox().getWidth() / 1024);
        this.box.setWidth(sprite.getTextureWidth() * scaleFactor);
        this.box.setHeight(sprite.getTextureHeight() * scaleFactor);

        float x = (float) box.getX();
        float y = (float) box.getY();
        float w = (float) box.getWidth();
        float h = (float) box.getHeight();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTextureID());

        GL11.glColor3f(color.getRed() / 255F, (float) color.getGreen() / 255F, (float) color.getBlue() / 255F);

        // Allows the background to scale w/o blurring
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(0F, 0F);
        GL11.glVertex2f(x, y);

        GL11.glTexCoord2f(1F, 0F);
        GL11.glVertex2f(x + w, y);

        GL11.glTexCoord2f(1F, 1F);
        GL11.glVertex2f(x + w, y + h);

        GL11.glTexCoord2f(0F, 1F);
        GL11.glVertex2f(x, y + h);

        GL11.glEnd();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * @param c the color overlay to apply to the background
     *          <p>
     *          Changes the background GL color to the specified color
     */
    public TileDecoration changeColor(Color c)
    {
        this.color = c;
        return this;
    }

    @Override
    public Rectangle getHitBox()
    {
        return this.box;
    }
}
