package com.ow.game.creature;

import squidpony.squidai.DijkstraMap;
import squidpony.squidmath.Coord;

import java.util.ArrayList;

/**
 * Created by lantra on 3/27/2017. all monsters will extend from this class, or justuse it if they are borin
 * slapped together very basic
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
        { //basic chase behavior

            dMap = new DijkstraMap(creature.getWorld().decoDungeon, DijkstraMap.Measurement.MANHATTAN); //this seems very inneffict not working in constructor though
             ArrayList<Coord> coordMap = dMap.findPath(1, creature.getWorld().getImpassable(), null, creature.getCoord(),
                    creature.getWorld().getPlayer().getCoord());

             Coord cord = coordMap.get(0);
             creature.move(cord.getX()-creature.getCoord().x,cord.getY()-creature.getCoord().y,creature.getWorld().dungeonGen);

        }
        else
        {
            int mover = rng.between(0,3); //no idea why but this works, need to fix diagonal movement has the monster wander aimlessly
            switch (mover) {
                case 0:
                {
                    if(creature.getWorld().creature(creature.getCoord().x+1,creature.getCoord().y) == null ||
                            creature.getWorld().creature(creature.getCoord().x+1,creature.getCoord().y) == creature.getWorld().getPlayer() ) //ugly way, also unlikey that a monster would randomly move onto a player but jic

                    {creature.move(1,0, creature.getWorld().dungeonGen);}
                    break;
                }
                case 1: {
                    if(creature.getWorld().creature(creature.getCoord().x-1,creature.getCoord().y) == null ||
                            creature.getWorld().creature(creature.getCoord().x-1,creature.getCoord().y) == creature.getWorld().getPlayer() )
                    creature.move(-1,0, creature.getWorld().dungeonGen);
                    break;
                }

                case 2:
                {   if(creature.getWorld().creature(creature.getCoord().x,creature.getCoord().y+1) == null ||
                        creature.getWorld().creature(creature.getCoord().x,creature.getCoord().y+1) == creature.getWorld().getPlayer() )
                    creature.move(0,1, creature.getWorld().dungeonGen);
                    break;
                }

                case 3:
                {
                    if(creature.getWorld().creature(creature.getCoord().x,creature.getCoord().y-1) == null ||
                            creature.getWorld().creature(creature.getCoord().x,creature.getCoord().y-1) == creature.getWorld().getPlayer() )
                    creature.move(0,-1, creature.getWorld().dungeonGen);
                    break;
                }
            }
        }

    }

    @Override
    public void doTurn()
    {
        moveTowardsOrAttackPlayer();
    }
}
