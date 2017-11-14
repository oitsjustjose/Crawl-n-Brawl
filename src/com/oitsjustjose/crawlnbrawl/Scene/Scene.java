package com.oitsjustjose.crawlnbrawl.Scene;

import com.oitsjustjose.crawlnbrawl.Game;
import com.oitsjustjose.crawlnbrawl.Manager.AudioManager;
import com.oitsjustjose.crawlnbrawl.Util.Helper;
import org.lwjgl.opengl.Display;

public abstract class Scene
{
    private boolean doExit = false;

    // return false if the game should be quit
    public abstract boolean drawFrame(float delta);

    // null typically means Game should load menu
    public Scene nextScene()
    {
        return null;
    }

    protected void exit()
    {
        doExit = true;
    }

    // returns false when game should be exited
    public boolean go()
    {
        long lastloop = Helper.getTime();

        boolean keepGoing = true;
        do
        {
            Display.sync(Game.getInstance().config.targetFPS);
            long now = Helper.getTime();
            long delta = now - lastloop;
            lastloop = now;

            keepGoing = drawFrame(delta);

            // UPDATE DISPLAY
            Display.update();
            AudioManager.getInstance().update();

            if (Display.isCloseRequested() || doExit)
            {
                return false;
            }
        } while (keepGoing);

        return true;
    }
}
