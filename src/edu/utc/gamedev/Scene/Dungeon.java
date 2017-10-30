package edu.utc.gamedev.Scene;

import edu.utc.gamedev.Entity.Item.EntityItem;
import edu.utc.gamedev.Entity.Living.EntityMonster;
import edu.utc.gamedev.Entity.Living.EntityPlayer;
import edu.utc.gamedev.Game;
import edu.utc.gamedev.Item.*;
import edu.utc.gamedev.Item.Sword.*;
import edu.utc.gamedev.Manager.*;
import edu.utc.gamedev.Tile.*;
import edu.utc.gamedev.Tile.TileBackground;
import edu.utc.gamedev.Tile.Dungeon.TileDecoration;
import edu.utc.gamedev.Tile.Dungeon.TileExit;
import edu.utc.gamedev.Util.FileUtils;
import edu.utc.gamedev.Util.Helper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Dungeon extends SceneLevel
{
    private static Dungeon instance;
    public EntityPlayer player;
    public EntityManager entityManager;
    public ProjectileManager projectileManager;
    public TileManager tileManager;
    public Random rand = new Random();
    private TileBackground background;
    private TilePlatformGround ground;
    private int level, maxLevels;
    private TrueTypeFont levelFont;
    private TileExit exit;

    public Dungeon()
    {
        this(new EntityPlayer(), 1);
    }

    public Dungeon(EntityPlayer player, int level)
    {
        SceneManager.getInstance().setScene(this);

        // Update variables and such
        instance = this;

        // Managers
        this.entityManager = new EntityManager();
        this.projectileManager = new ProjectileManager();
        this.tileManager = new TileManager();

        // Player
        this.player = player;
        this.entityManager.addEntity(this.player);
        this.level = level;
        this.maxLevels = 1;

        initFont();
        initScene();
    }

//    public static Living getInstance()
//    {
//        return instance;
//    }

    public void initFont()
    {
        try
        {
            File font = new File("natives/justabit.ttf".replace("/", File.separator)).getAbsoluteFile();
            if (!font.exists())
            {
                System.out.println("Font did not exist; copying...");
                FileUtils.copyStream(ResourceLoader.getResourceAsStream("assets/justabit.ttf"), new File("natives/justabit.ttf".replace("/", File.separator)));
            }

            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, font).deriveFont(32F);
            levelFont = new TrueTypeFont(awtFont, false);
        }
        catch (IOException | FontFormatException e)
        {
            levelFont = new TrueTypeFont(new Font("Arial", Font.PLAIN, 32), false);
        }
    }

    public void initScene()
    {
        // Tiles
        Color c = Helper.getRandomColor();
        background = new TileBackground("wall").changeColor(c);
        ground = new TilePlatformGround();
        exit = new TileExit().setColor(c);
        tileManager.addTile(this.background);
        for (int i = 0; i < rand.nextInt(3) + 1; i++)
        {
            tileManager.addTile(new TileDecoration("wall_decor_", 4).changeColor(c));
        }
        if (this.level % 5 == 0)
        {
            if (getItemAtStage(level / 5) != null)
            {
                AudioManager.getInstance().play("level_up");
                entityManager.addEntity(new EntityItem(getItemAtStage(level / 5), Display.getWidth() / 2, Display.getHeight() - 32, -1));
            }
        }

        tileManager.addTile(ground);
        tileManager.addTile(new TilePlatformBase());

        spawnMonsters();
    }

    public Item getItemAtStage(int stage)
    {
        switch (stage)
        {
            case 1:
                return new ItemDagger();
            case 2:
                return new ItemWoodenSword();
            case 3:
                return new ItemRustySword();
            case 4:
                return new ItemRefinedSword();
            case 5:
                return new ItemBroadSword();
            case 6:
                return new ItemGreatSword();
            case 7:
                return new ItemPotatoGun();
            default:
                return null;
        }
    }

    @Override
    public Scene nextScene()
    {
        // Progress to the next level!
        if (entityManager.allMonstersDead() && player.getHitBox().intersects(exit.getHitBox()))
        {
            // If the next level is less than level
            if (level + 1 <= maxLevels)
            {
                return new Dungeon(player, level + 1);
            }
            else
            {
                return new Surface(player);
            }
        }

        Menu menu = new Menu();
        if (player == null || player.getState() == EntityPlayer.State.DEAD)
        {
            menu.addItem("Play Again", new Dungeon());
        }
        else
        {
            menu.addItem("Resume", this);
        }
        menu.addSpecial("Exit", Menu.DO_EXIT);

        return menu;

    }


    public TileBackground getBackground()
    {
        return background;
    }

    @Override
    public boolean drawFrame(float delta)
    {
        entityManager.updateAll(delta);
        projectileManager.updateAll(delta);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        tileManager.drawAll();
        if (entityManager.allMonstersDead())
        {
            exit.draw();
        }
        entityManager.drawAll();
        projectileManager.drawAll();


        // I use F2 for screenshots based on other games I've enjoyed
        if (Keyboard.isKeyDown(Keyboard.KEY_F2))
        {
            Game.getInstance().screenshotManager.takeScreenshot();
        }

        // I use F11 for fullscreen for the same reason!
        if (Keyboard.isKeyDown(Keyboard.KEY_F11))
        {
            if (Display.isFullscreen())
            {
                Game.getInstance().setDisplayMode(Game.getInstance().config.resolutionWidth, Game.getInstance().config.resolutionHeight, false);
            }
            else
            {
                Game.getInstance().setDisplayMode(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, true);
            }
            Display.setResizable(true);
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        }

        // Screen resize handler
        if (Display.wasResized())
        {
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        }

        TextureImpl.bindNone();
        int rem = (maxLevels - level) + 1;

        levelFont.drawString(1, 0, (rem + (rem == 1 ? " level" : " levels") + " from the surface"));

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            return false;
        }
        if (entityManager.allMonstersDead())
        {
            if (player.getHitBox().intersects(exit.getHitBox()))
            {
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                {
                    return false;
                }
            }
        }
        return player != null;
    }

    /**
     * Spawns a quantity of monsters directly corresponding to the level the player is at
     */
    public void spawnMonsters()
    {
        int variants = EntityMonster.Type.values().length;

        for (int i = 0; i < this.level; i++)
        {
            entityManager.addEntity(new EntityMonster(EntityMonster.Type.values()[rand.nextInt(variants)]));
        }
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return this.player;
    }

    @Override
    public void setPlayer(EntityPlayer p)
    {
        this.player = p;
    }

    @Override
    public TileManager getTileManager()
    {
        return this.tileManager;
    }

    @Override
    public EntityManager getEntityManager()
    {
        return this.entityManager;
    }

    @Override
    public ProjectileManager getProjectileManager()
    {
        return this.projectileManager;
    }
}