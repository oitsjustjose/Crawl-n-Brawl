package com.oitsjustjose.crawlnbrawl.Manager;


import com.oitsjustjose.crawlnbrawl.Scene.SceneLevel;

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
