package com.ow.game.creature;


import com.ow.game.items.Inventory;
import com.ow.game.items.Item;
import com.ow.game.roguelike.World;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidmath.Coord;
import squidpony.squidmath.RNG;


/**
 * Controls the stats and basic interactions with world space and other creatures. The mind classes
 * will determine how the creature behaves and uses special abilities.
 */
public class Creature
{
    private String name, description;
    private Mind Ai;
    private int str, dex, agi, intel, con, will, hp, maxhp, focus, maxfocus;
    private char glyph;
    private Coord coord;
    private SColor color;
    private World world;
    private RNG rng;
    private Inventory inventory;



    public Creature(String name,  int str, int dex, int agi, int intel, int con, int will, char glyph) {

        this.name = name;
        this.str = str;
        this.dex = dex;
        this.agi = agi;
        this.intel = intel;
        this.con = con;
        this.will = will;
        this.glyph = glyph;

        this.maxhp = calcMaxHP();
        this.hp = this.maxhp;

        this.maxfocus = calcMaxFocus();
        this.focus = maxfocus;

        this.coord = Coord.get(0, 0); //TODO: wtf

        this.rng = new RNG();

        inventory = new Inventory(20);
    }


    public String getName() { return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public Mind getAi() {return Ai; }
    public void setAi(Mind ai) { Ai = ai; }
    public int getStr() {return str;}
    public void setStr(int str) {this.str = str;}
    public int getDex() {return dex;}
    public void setDex(int dex) {this.dex = dex;}
    public int getAgi() { return agi;  }
    public void setAgi(int agi) { this.agi = agi; }
    public int getIntel() {return intel;}
    public void setIntel(int intel) {this.intel = intel;}
    public int getCon() {return con;}
    public void setCon(int con) {this.con = con;}
    public int getWill() {return will;}
    public void setWill(int will) {this.will = will;}
    public int getHp() { return hp;}
    public void setHp(int hp) {this.hp = hp;}
    public int getMaxhp() {return maxhp;}
    public void setMaxhp(int maxhp) {this.maxhp = maxhp; }
    public int getFocus() {return focus;}
    public void setFocus(int focus) {this.focus = focus;}
    public int getMaxfocus() {return maxfocus;}
    public void setMaxfocus(int maxfocus) {this.maxfocus = maxfocus;}
    public char getGlyph() {
        return glyph;
    }
    public void setGlyph(char glyph) {
        this.glyph = glyph;
    }
    public Coord getCoord() {
        return coord;
    }
    public SColor getColor() {
        return color;
    }
    public void setColor(SColor color) {
        this.color = color;
    }
    public void setCoord (Coord coord){this.coord = coord;}
    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }
    public Inventory getInventory(){return this.inventory;}



    public int calcMaxHP ()
    {
        return (int) Math.round(con * 2.25);
    }

    public int calcMaxFocus ()
    {
        return (will * 3);
    }

    public void modifyHP(int amount)
    {

        this.hp -= amount;
        if (this.hp <= 0) {
            world.removeCreature(this);
            doAction("die");
        }
    }

    public void attack (Creature other)
    {

        if (this.Ai.getAv() + rng.between(1,100) > other.Ai.getDv() + 25)
        {
            int amount = this.Ai.getDamageV();
            other.modifyHP(amount);
            notify("You attack the '%s' for %d damage.", other.getName(), amount);
            other.notify("The '%s' attacks you for %d damage.", name, amount);
        }
        else
        {
            notify("Your attack missed the '%s'.", other.getName());
            other.notify("The '%s''s attack missed.", name);
        }


    }

    public void move(int xmod, int ymod, DungeonGenerator dungeon) {
        int newX = coord.x + xmod, newY = coord.y + ymod;
        if (newX >= 0 && newY >= 0 && newX < dungeon.getWidth() && newY < dungeon.getHeight()
                && dungeon.getBareDungeon()[newX][newY] != '#' && world.creature(newX,newY) == null)
            {
                coord = coord.translate(xmod, ymod);
            }
        else if (world.creature(newX,newY) != null)
        {

            attack(world.creature(newX,newY));
        }
    }

    public void notify(String message, Object ... params){
        Ai.onNotify(String.format(message, params));
    }

    public void doAction(String message, Object ... params){
        int r = 9;
        for (int ox = -r; ox < r+1; ox++){
            for (int oy = -r; oy < r+1; oy++){
                if (ox*ox + oy*oy > r*r)
                    continue;

                Creature other = world.creature(coord.x+ox, coord.y+oy);

                if (other == null)
                    continue;

                if (other == this)
                    other.notify("You " + message + ".", params);
                else
                    other.notify(String.format("The '%s' %s.", name, makeSecondPerson(message)), params);
            }
        }
    }

    private String makeSecondPerson(String text){
        String[] words = text.split(" ");
        words[0] = words[0] + "s";

        StringBuilder builder = new StringBuilder();
        for (String word : words){
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }

    public void pickup(){
        Item item = world.item(coord);

        if (inventory.isFull() || item == null){
            doAction("grab at the ground");
        } else {
            doAction("pickup a %s", item.getName());
            world.removeItem(coord);
            inventory.add(item);
        }
    }


    public void drop(Item item){
        doAction("drop a " + item.getName());
        inventory.remove(item);
        world.addItemAtEmptySpace(item, coord);
    }



}

