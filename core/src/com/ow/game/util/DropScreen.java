package com.ow.game.util;

import com.ow.game.creature.Creature;
import com.ow.game.items.Item;
import squidpony.squidgrid.gui.gdx.SquidPanel;

/**
 * Created by HP User on 3/29/2017.
 */
public class DropScreen extends InventoryBasedScreen
{
    public DropScreen (Creature player, int gridHeight, int gridWidth)
    {
        super(player, gridHeight, gridWidth);
    }

    protected String getVerb() {
        return "drop";
    }

    protected boolean isAcceptable(Item item) {
        return true;
    }

    protected InventoryBasedScreen use(Item item) {
        player.drop(item);
        return null;
    }

}
