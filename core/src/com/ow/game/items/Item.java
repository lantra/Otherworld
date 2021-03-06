package com.ow.game.items;

import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.Coord;

/**
 * Created by lantra on 3/27/2017.
 */
public class Item
{
    protected boolean isEquipable;
    private String name, description;
    private char glyph;
    private Coord coord;
    private SColor color;

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
    public void setCoord(Coord coord) {this.coord = coord;}
    public void setDescription(String description) {this.description = description;}
    public String getDescription(){return description;}

    public SColor getColor() {
        return color;
    }

    public void setColor(SColor color) {
        this.color = color;
    }
}
