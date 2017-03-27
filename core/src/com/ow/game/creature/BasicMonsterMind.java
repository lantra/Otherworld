package com.ow.game.creature;

import squidpony.squidai.DijkstraMap;
import squidpony.squidmath.Coord;
import squidpony.squidmath.RNG;

import java.util.ArrayList;

/**
 * Created by lantra on 3/27/2017. all monsters will extend from this class, or justuse it if they are borin
 */
public class BasicMonsterMind  extends Mind
{
    private DijkstraMap dMap;


    public BasicMonsterMind(Creature c) {
        super(c);

    }

    public void moveTowardsOrAttackPlayer()
    {

        if (canSeeTarget(creature.getWorld().getPlayer()))
        {
            int newx, newy;
            dMap = new DijkstraMap(creature.getWorld().decoDungeon, DijkstraMap.Measurement.MANHATTAN);
             ArrayList<Coord> coordMap = dMap.findPath(1, creature.getWorld().getImpassable(), null, creature.getCoord(),
                    creature.getWorld().getPlayer().getCoord());

             Coord cord = coordMap.get(0);
             creature.move(cord.getX(),cord.getY(),creature.getWorld().dungeonGen);
        }
        else
        {
            creature.move(rng.between(-1,1), rng.between(-1,1), creature.getWorld().dungeonGen); //move randomly TODO: monsters kill each other while wandering
        }

    }

    @Override
    public void doTurn()
    {
        moveTowardsOrAttackPlayer();
    }
}
