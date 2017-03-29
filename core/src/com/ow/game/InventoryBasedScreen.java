package com.ow.game;

import com.ow.game.creature.Creature;
import com.ow.game.items.Item;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.SquidLayers;
import squidpony.squidgrid.gui.gdx.SquidPanel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Lantra on 3/28/2017.
 */
public abstract class InventoryBasedScreen extends SquidPanel {

    protected Creature player;
    private String letters;
    private SquidInput input;

    protected abstract String getVerb();
    protected abstract boolean isAcceptable(Item item);


    public InventoryBasedScreen(Creature player, int gridHeight, int gridWidth){
        super(gridWidth,gridHeight);
        this.player = player;
        this.letters = "abcdefghijklmnopqrstuvwxyz";
    }

    public void displayOutput(SquidLayers display)
    {
        ArrayList<String> lines = getList();
        int y = 23 - lines.size();
        int x = 4;

        //if (lines.size() > 0)
           // terminal.clear(' ', x, y, 20, lines.size());

        char[] spaceArray = new char[gridWidth];
        Arrays.fill(spaceArray, ' ');
        String spaces = String.valueOf(spaceArray);

        for (int i =0; i < lines.size(); i++){
            display.putString(gridWidth/8, (gridHeight/2 - (lines.size() /2 ) + i), spaces, 0, 1);
            display.putString(gridWidth/8, (gridHeight/2 - (lines.size() /2 ) + i), lines.get(i), 0, 1);
        }

        display.putString(gridWidth/8, (gridHeight/2 - (lines.size() /2 ) - 1 ), spaces, 0 , 1);
        display.putString(gridWidth/8, (gridHeight/2 - (lines.size() /2 ) - 1 ), "What would you like to " + getVerb() + "?", 0 , 1);
    }

    private ArrayList<String> getList() {
        ArrayList<String> lines = new ArrayList<String>();
        Item[] inventory = player.getInventory().getItems();

        for (int i = 0; i < inventory.length; i++){
            Item item = inventory[i];

            if (item == null || !isAcceptable(item))
                continue;

            String line = letters.charAt(i) + " - " + item.getGlyph() + " " + item.getName();

            lines.add(line);
        }
        return lines;
    }

    /*public SquidInput respondToUserInput(KeyEvent key) {
        char c = key.getKeyChar();

        Item[] items = player.inventory().getItems();

        if (letters.indexOf(c) > -1
                && items.length > letters.indexOf(c)
                && items[letters.indexOf(c)] != null
                && isAcceptable(items[letters.indexOf(c)]))
            return use(items[letters.indexOf(c)]);
        else if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
            return null;
        else
            return this;
    }*/
}
