package com.ow.game.creature;


/**
 * Created by HP User on 3/27/2017.
 */
public class PlayerMind extends Mind {


    public PlayerMind(Creature c)
    {
        super(c);
    }

    @Override
    public void  onNotify(String format)
    {
        creature.getWorld().messages.add(format);
    }

    @Override
    public void doTurn()
    {
        //intetionally blank
    }

}
