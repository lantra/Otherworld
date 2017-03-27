package com.ow.game.factories;

import com.ow.game.creature.*;
import com.ow.game.roguelike.World;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidmath.Coord;


/**
 * Simple version of creature factory, call by adding to array list o.O
 */
public class CreatureFactory
{
        private DungeonGenerator dungeonGen;

        public CreatureFactory (DungeonGenerator dungeonGen)
        {
            this.dungeonGen = dungeonGen;

        }

        public Creature createPlayer(Coord position, World world)
        {
            Creature player = new Creature("Player", 10,10,10,10,10,10,'@');
            PlayerMind ai = new PlayerMind(player);
            player.setDescription("This is you.");
            player.setColor(SColor.DARK_BLUE_LAPIS_LAZULI);
            player.setWorld(world);
            player.setCoord(position);
            return player;
        }

        public Creature createGoblin(Coord position, World world)
        {

            Creature goblin = new Creature ("Goblin", 4,4,4,4,4,4,'g');
            BasicMonsterMind ai = new BasicMonsterMind(goblin);
            goblin.setDescription("This is a goblin. Scary.");
            goblin.setColor(SColor.FERN_GREEN);
            goblin.setWorld(world);
            goblin.setCoord(position);
            return goblin;
        }






}
