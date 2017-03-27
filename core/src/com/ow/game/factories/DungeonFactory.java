package com.ow.game.factories;

import com.ow.game.creature.Creature;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidmath.RNG;

import java.util.ArrayList;

/**
 * place holder
 */
public class DungeonFactory
{
    public DungeonGenerator dungeonGen;
    public char[][] decoDungeon, bareDungeon, lineDungeon;
    private RNG rng;
    private CreatureFactory creatureFactory;
    private ArrayList<Creature> creatures = new ArrayList<Creature>();
    private Creature player;


    public CreatureFactory getCreatureFactory() {
        return creatureFactory;
    }

    public Creature creature(int x, int y){
        for (Creature c : creatures){
            if (c.getCoord().getX() == x && c.getCoord().getY() == y)
                return c;
        }
        return null;
    }

    public Creature getPlayer() {
        return player;
    }
}
