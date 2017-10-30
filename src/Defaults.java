/**
 * @author Jose Stovall
 * A launcher class in the default package which runs the main game
 * Also handles copying and setting LWJGL Natives
 */

import edu.utc.gamedev.Game;
import edu.utc.gamedev.Util.FileUtils;
import org.newdawn.slick.util.ResourceLoader;

import java.io.File;

public class Defaults
{
    public static void main(String[] args)
    {
        // Copies the natives folder FROM the compiled jar if it doesn't exist
        if (!(new File("natives").exists()))
        {
            FileUtils.copyResourcesRecursively(ResourceLoader.getResource("natives"), new File("natives"));
        }

        // Sets the LWJGL Libraries path to the natives folder
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        new Game();
    }
}
