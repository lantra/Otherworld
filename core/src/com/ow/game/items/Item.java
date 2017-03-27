package com.ow.game.items;

/**
 * Created by lantra on 3/27/2017.
 */
public class Item
{
    private boolean isEquipable;
    private String name;
    private char glyph;

    public Item (String name, char glyph)
    {
        this.name = name;
        this.glyph = glyph;
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
}
