package edu.utc.gamedev.Manager;


import edu.utc.gamedev.Scene.SceneLevel;

public class SceneManager
{
    private static SceneManager instance;
    private SceneLevel currentSceneLevel;

    public SceneManager()
    {
        this.currentSceneLevel = null;
        instance = this;
    }

    public static SceneManager getInstance()
    {
        return instance;
    }

    public SceneLevel getScene()
    {
        return this.currentSceneLevel;
    }

    public void setScene(SceneLevel s)
    {
        this.currentSceneLevel = s;
    }

}
