package com.ow.game.items;

import squidpony.squidmath.Coord;

/**
 * Created by lantra on 3/27/2017.
 */
public class Item
{
    private boolean isEquipable;
    private String name;
    private char glyph;
    private Coord coord;

    public Item (String name, char glyph)
    {
        this.name = name;
        this.glyph = glyph;
        this.coord =  coord.get(0,0);
    }

    public boolean isEquipable() {
        return isEquipable;
    }
    public void setEquipable(boolean equipable) {
        isEquipable = equipable;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public char getGlyph() {
        return glyph;
    }
    public void setGlyph(char glyph) {
        this.glyph = glyph;
    }
    public Coord getCoord () {return coord;}
}
