package com.ow.game.items;

/**
 * Created by lantra on 3/27/2017.
 */
public class Weapon extends Item {

    public Weapon (String name, char glyph)
    {
        super(name, glyph);
        setEquipable(true);
    }
}
