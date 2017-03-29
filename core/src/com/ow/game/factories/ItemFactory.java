package com.ow.game.factories;

import com.ow.game.items.Item;

/**
 * Created by Lantra on 3/29/2017.
 */
public class ItemFactory
{
    public ItemFactory()
    {

    }

    public Item createJunk()
    {
        Item item = new Item("Rubbish", '*');
        item.setDescription("It's useless");
        return item;
    }
}
