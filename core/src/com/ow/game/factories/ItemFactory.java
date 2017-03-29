package com.ow.game.factories;

import com.ow.game.items.Item;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.Coord;

/**
 * Created by Lantra on 3/29/2017.
 */
public class ItemFactory
{
    public ItemFactory()
    {

    }

    public Item createJunk(Coord position)
    {
        Item item = new Item("Rubbish", '*');
        item.setDescription("It's useless");
        item.setCoord(position);
        item.setColor(SColor.BROWN);
        return item;
    }
}
