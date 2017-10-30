package edu.utc.gamedev.Scene;

import edu.utc.gamedev.Entity.Living.EntityPlayer;
import edu.utc.gamedev.Game;
import edu.utc.gamedev.Manager.EntityManager;
import edu.utc.gamedev.Manager.ProjectileManager;
import edu.utc.gamedev.Manager.SceneManager;
import edu.utc.gamedev.Manager.TileManager;
import edu.utc.gamedev.Tile.ITile;
import edu.utc.gamedev.Tile.Surface.TileGrass;
import edu.utc.gamedev.Tile.Surface.TileHouse;
import edu.utc.gamedev.Tile.Surface.TileWell;
import edu.utc.gamedev.Tile.TileBackground;
import edu.utc.gamedev.Tile.TilePlatformGround;
import edu.utc.gamedev.Util.FileUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Surface extends SceneLevel
{

    private EntityPlayer player;
    private TileManager tileManager;
    private TileBackground background;
    private TilePlatformGround ground;
    private TrueTypeFont levelFont;
    private EntityManager entityManager;
    private ITile house, well;
    private ProjectileManager projectileManager;

    public Surface(EntityPlayer player)
    {
        SceneManager.getInstance().setScene(this);

        // Managers
        this.tileManager = new TileManager();
        this.entityManager = new EntityManager();
        this.projectileManager = new ProjectileManager();

        // Player
        this.player = player;

        // House
        this.house = new TileHouse();
        this.well = new TileWell();
        this.player.getHitBox().setX(well.getHitBox().getX() + well.getHitBox().getWidth() + 32);
        this.player.getHitBox().setY(Display.getHeight() - this.player.getHitBox().getHeight());

        initFont();
        initScene();
    }


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
        background = new TileBackground("sky");
        ground = new TilePlatformGround();
        tileManager.addTile(ground);
        tileManager.addTile(background);
        tileManager.addAll(populateGrass());
        tileManager.addTile(house);
        tileManager.addTile(well);
        entityManager.addEntity(player);
        player.setHeldItem(null);
    }

    private ArrayList<ITile> populateGrass()
    {
        int x = 0;
        ArrayList<ITile> ret = new ArrayList<>();
        TileGrass grassTemp;
        while (x < Toolkit.getDefaultToolkit().getScreenSize().width)
        {
            grassTemp = new TileGrass(x);
            ret.add(grassTemp);
            x += grassTemp.getHitBox().getWidth();
        }
        return ret;
    }

    @Override
    public Scene nextScene()
    {
        if (player != null && player.getHitBox().intersects(well.getHitBox()))
        {
            return new Dungeon();
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


    @Override
    public boolean drawFrame(float delta)
    {
        entityManager.updateAll(delta);
        projectileManager.updateAll(delta);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        tileManager.drawAll();
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
        levelFont.drawString(1, 0, ("Surface"));

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            return false;
        }
        if (player.getHitBox().intersects(house.getHitBox()))
        {
            entityManager.removeEntity(player);
            player = null;
            return false;
        }
        if (player.getHitBox().intersects(well.getHitBox()))
        {
            return false;
        }
        return true;
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
