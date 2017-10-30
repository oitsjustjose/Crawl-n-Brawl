package edu.utc.gamedev.Item;

public class ItemHeart extends Item
{
    public ItemHeart()
    {
        super("heart");
    }


    @Override
    public Item setCursor()
    {
        return this;
    }
}
