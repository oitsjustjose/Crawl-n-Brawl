package edu.utc.gamedev.Scene;

import edu.utc.gamedev.Game;
import edu.utc.gamedev.Manager.AudioManager;
import edu.utc.gamedev.Tile.TileBackground;
import edu.utc.gamedev.Util.FileUtils;
import edu.utc.gamedev.Util.Helper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Menu extends Scene
{
    public static final int DO_EXIT = 0;
    private List<Item> items;
    private int currItem;
    private long lastChanged;
    private TileBackground bg;
    private TrueTypeFont menuFont;
    private TrueTypeFont selectedFont;
    private TrueTypeFont titleFont;

    public Menu()
    {
        items = new LinkedList<>();
        lastChanged = Helper.getTime();
        bg = new TileBackground("menu");
        HashMap<TextAttribute, Integer> titleAttributes = new HashMap<>();
        titleAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

        HashMap<TextAttribute, Integer> selectedAttributes = new HashMap<>();
        selectedAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

        try
        {
            File font = new File("natives/justabit.ttf".replace("/", File.separator)).getAbsoluteFile();
            if (!font.exists())
            {
                System.out.println("Font did not exist; copying...");
                FileUtils.copyStream(ResourceLoader.getResourceAsStream("assets/justabit.ttf"), new File("natives/justabit.ttf".replace("/", File.separator)));
            }

            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, font).deriveFont(titleAttributes).deriveFont(64F);
            titleFont = new TrueTypeFont(awtFont, false);

            awtFont = Font.createFont(Font.TRUETYPE_FONT, font).deriveFont(selectedAttributes).deriveFont(Font.BOLD).deriveFont(52F);
            selectedFont = new TrueTypeFont(awtFont, false);

            awtFont = Font.createFont(Font.TRUETYPE_FONT, font).deriveFont(40F);
            menuFont = new TrueTypeFont(awtFont, false);
        }
        catch (IOException | FontFormatException e)
        {
            titleFont = new TrueTypeFont(new Font("Arial", Font.PLAIN, 64).deriveFont(titleAttributes).deriveFont(64F), false);
            selectedFont = new TrueTypeFont(new Font("Arial", Font.BOLD, 52).deriveFont(selectedAttributes).deriveFont(Font.BOLD).deriveFont(52F), false);
            menuFont = new TrueTypeFont(new Font("Arial", Font.PLAIN, 40).deriveFont(40F), false);
        }
    }

    // reset menu
    public void clear()
    {
        items.clear();
    }

    public void addItem(String label, Scene s)
    {
        items.add(new Item(label, s));
    }

    public void addSpecial(String label, int tag)
    {
        items.add(new Special(label, tag));
    }

    public Scene nextScene()
    {
        return items.get(currItem).scene;
    }

    public boolean drawFrame(float delta)
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        bg.draw();

        while (Keyboard.next())
        {
            if (Keyboard.getEventKeyState()) // key was pressed
            {
                switch (Keyboard.getEventKey())
                {
                    case Keyboard.KEY_W:
                        AudioManager.getInstance().play("menu_move");
                        currItem--;
                        if (currItem < 0)
                        {
                            currItem += items.size(); // go to end
                        }
                        break;
                    case Keyboard.KEY_UP:
                        AudioManager.getInstance().play("menu_move");
                        currItem--;
                        if (currItem < 0)
                        {
                            currItem += items.size(); // go to end
                        }
                        break;
                    case Keyboard.KEY_S:
                        AudioManager.getInstance().play("menu_move");
                        currItem = (currItem + 1) % items.size();
                        break;
                    case Keyboard.KEY_DOWN:
                        AudioManager.getInstance().play("menu_move");
                        currItem = (currItem + 1) % items.size();
                        break;
                    case Keyboard.KEY_RETURN:
                        AudioManager.getInstance().play("menu_select");
                        Item item = items.get(currItem);
                        if (item instanceof Special)
                        {
                            switch (((Special) item).tag)
                            {
                                case DO_EXIT:
                                    exit();
                                    break;
                            }
                        }
                        return false;
                    case Keyboard.KEY_F2:
                        Game.getInstance().screenshotManager.takeScreenshot();
                        break;
                    case Keyboard.KEY_F11:
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
                        break;
                }
            }
        }

        // Screen resize handler
        if (Display.wasResized())
        {
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        }

        float spacing = Display.getHeight() / (items.size() + 4);
        float offset = 2 * spacing;

        TextureImpl.bindNone();
        titleFont.drawString((Display.getWidth() / 2) - (titleFont.getWidth("Crawl 'n Brawl") / 2), 64F, "Crawl 'n Brawl", new Color(0, 123, 124));

        for (int i = 0; i < items.size(); i++)
        {
            if (i == currItem)
            {
                TextureImpl.bindNone();

                selectedFont.drawString(Display.getWidth() / 2 - (selectedFont.getWidth(items.get(i).label) / 2), offset, items.get(i).label, new Color(85, 124, 0));
            }
            else
            {
                TextureImpl.bindNone();
                menuFont.drawString(Display.getWidth() / 2 - (menuFont.getWidth(items.get(i).label) / 2), offset, items.get(i).label, new Color(74, 91, 37));
            }

            offset += spacing;
        }

        // font binds a texture, so let's turn it off..
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        return true;
    }


    // a menu item: label and associated Scene to jump to
    private static class Item
    {
        public String label;
        public Scene scene;

        public Item(String label, Scene s)
        {
            this.label = label;
            this.scene = s;
        }
    }

    private static class Special extends Item
    {
        public int tag;

        public Special(String label, int tag)
        {
            super(label, null);
            this.tag = tag;
        }
    }
}
