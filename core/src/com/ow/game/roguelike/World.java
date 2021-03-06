package com.ow.game.roguelike;

/**
 * Created by HP User on 3/27/2017.
 */
import com.ow.game.creature.Creature;
import com.ow.game.factories.CreatureFactory;
import com.ow.game.factories.ItemFactory;
import com.ow.game.items.Item;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidmath.Coord;
import squidpony.squidmath.CoordPacker;
import squidpony.squidmath.RNG;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * world class to handle each level of the game
 */
public class World {
    public DungeonGenerator dungeonGen;
    public char[][] decoDungeon, bareDungeon, lineDungeon;
    private RNG rng;
    private CreatureFactory creatureFactory;
    private ArrayList<Creature> creatures = new ArrayList<Creature>();
    private ItemFactory itemFactory;
    private ArrayList<Item> items = new ArrayList<Item>();
    private Creature player;
    public int gridWidth, gridHeight,cellWidth, cellHeight;
    public ArrayList<String> messages = new ArrayList<String>();

    public World ()
    {
        //These variables, corresponding to the screen's width and height in cells and a cell's width and height in
        //pixels, must match the size you specified in the launcher for input to behave.
        //This is one of the more common places a mistake can happen.
        //In our desktop launcher, we gave these arguments to the configuration:
        //	config.width = 80 * 8;
        //  config.height = 40 * 18;
        //Here, config.height refers to the total number of rows to be displayed on the screen.
        //We're displaying 32 rows of dungeon, then 8 more rows of text generation to show some tricks with language.
        //gridHeight is 32 because that variable will be used for generating the dungeon and handling movement within
        //the upper 32 rows. Anything that refers to the full height, which happens rarely and usually for things like
        //screen resizes, just uses gridHeight + 8. Next to it is gridWidth, which is 80 because we want 80 grid spaces
        //across the whole screen. cellWidth and cellHeight are 8 and 18, and match the multipliers for config.width and
        //config.height, but in this case don't strictly need to because we soon use a "Stretchable" font. While
        //gridWidth and gridHeight are measured in spaces on the grid, cellWidth and cellHeight are the pixel dimensions
        //of an individual cell. The font will look more crisp if the cell dimensions match the config multipliers
        //exactly, and the stretchable fonts (technically, distance field fonts) can resize to non-square sizes and
        //still retain most of that crispness.
        gridWidth = 80;
        gridHeight = 32;
        cellWidth = 8;
        cellHeight = 18;
        // gotta have a random number generator. We can seed an RNG with any long we want, or even a String.
        rng = new RNG();

        //This uses the seeded RNG we made earlier to build a procedural dungeon using a method that takes rectangular
        //sections of pre-drawn dungeon and drops them into place in a tiling pattern. It makes good "ruined" dungeons.
        dungeonGen = new DungeonGenerator(gridWidth, gridHeight, rng);
        //uncomment this next line to randomly add water to the dungeon in pools.
        //dungeonGen.addWater(15);
        //decoDungeon is given the dungeon with any decorations we specified. (Here, we didn't, unless you chose to add
        //water to the dungeon. In that case, decoDungeon will have different contents than bareDungeon, next.)
        decoDungeon = dungeonGen.generate();
        //getBareDungeon provides the simplest representation of the generated dungeon -- '#' for walls, '.' for floors.
        bareDungeon = dungeonGen.getBareDungeon();
        //When we draw, we may want to use a nicer representation of walls. DungeonUtility has lots of useful methods
        //for modifying char[][] dungeon grids, and this one takes each '#' and replaces it with a box-drawing character.
        lineDungeon = DungeonUtility.hashesToLines(decoDungeon);
        // it's more efficient to get random floors from a packed set containing only (compressed) floor positions.
        short[] placement = CoordPacker.pack(bareDungeon, '.');
        //Coord is the type we use as a general 2D point, usually in a dungeon.
        //Because we know dungeons won't be incredibly huge, Coord performs best for x and y values less than 256.
        creatureFactory = new CreatureFactory(dungeonGen);
        player = creatureFactory.createPlayer(dungeonGen.utility.randomCell(placement), this);
        creatures.add(player);
        for (int i = 0; i < 5; i++)
        {
            Creature goblin = creatureFactory.createGoblin(dungeonGen.utility.randomCell(placement), this);
            creatures.add(goblin);
        }

        itemFactory = new ItemFactory();

        for (int i = 0; i < 5; i++)
        {
            Item rubbish = itemFactory.createJunk(dungeonGen.utility.randomCell(placement));
            items.add(rubbish);
        }



    }

    public CreatureFactory getCreatureFactory() {
        return creatureFactory;
    }
    public ArrayList<Item> getItems(){return items;}
    public Creature getPlayer() {
        return player;
    }


    public Creature creature(int x, int y) {
        for (Creature c : creatures) {
            if (c.getCoord().getX() == x && c.getCoord().getY() == y)
                return c;
        }
        return null;
    }
    public HashSet<Coord> getImpassable()
    {
        HashSet<Coord> cords = new HashSet<Coord>();
        for ( Creature c: creatures)
        {
                cords.add(c.getCoord());
        }

        return cords;
    }
    public void removeCreature(Creature other) {
        creatures.remove(other);
    }

    public ArrayList<Creature> getCreatures(){return creatures;}


    public Item item(Coord coord){
        for ( Item i: items) {
            if (coord.x == i.getCoord().x && coord.y == i.getCoord().y) {
                return i;
            }

        } return null;
    }

    public void removeItem(Coord coord) {
        items.remove(item(coord));
    }

    public void addItemAtEmptySpace(Item item, Coord coord)
    { //palce holder
        if (item == null)
            return;
        else removeItem(coord);
        /*
        List<Coord> coords = new ArrayList<Coord>();
        List<Coord> checked = new ArrayList<Coord>();

        coords.add(coord);

        while (!coords.isEmpty()){
            Coord p = coords.removeItem(0);
            checked.add(p);

            if ()
                continue;

            if (items[p.x][p.y][p.z] == null){
                items[p.x][p.y][p.z] = item;
                Creature c = this.creature(p.x, p.y, p.z);
                if (c != null)
                    c.notify("A %s lands between your feet.", item.name());
                return;
            } else {
                List<Point> neighbors = p.neighbors8();
                neighbors.removeAll(checked);
                points.addAll(neighbors);
            }
        } */
    }


}
