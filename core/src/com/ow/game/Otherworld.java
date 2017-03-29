package com.ow.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ow.game.creature.*;
import com.ow.game.roguelike.World;
import squidpony.squidgrid.gui.gdx.*;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.Arrays;

public class Otherworld extends ApplicationAdapter {
    SpriteBatch batch;

    private SquidLayers display;

    private int[][] colorIndices, bgColorIndices;
    private World world;
    private SquidInput input;
    private Color bgColor;
    private Stage stage;
    private ArrayList<Coord> toCursor;
    private ArrayList<Coord> awaitedMoves;
    private float secondsWithoutMoves;
    private Creature player;
    private SquidMessageBox msgbox;

    @Override
    public void create () {

        world = new World();
        player = world.getPlayer();
        //Some classes in SquidLib need access to a batch to render certain things, so it's a good idea to have one.
        batch = new SpriteBatch();
        //Here we make sure our Stage, which holds any text-based grids we make, uses our Batch.
        stage = new Stage(new StretchViewport(world.gridWidth * world.cellWidth, (world.gridHeight + 8) * world.cellHeight), batch);
        // the font will try to load Inconsolata-LGC-Custom as an embedded bitmap font with a distance field effect.
        // this font is covered under the SIL Open Font License (fully free), so there's no reason it can't be used.
        display = new SquidLayers(world.gridWidth, world.gridHeight + 8, world.cellWidth, world.cellHeight, DefaultResources.getStretchableFont());
        // a bit of a hack to increase the text height slightly without changing the size of the cells they're in.
        // this causes a tiny bit of overlap between cells, which gets rid of an annoying gap between vertical lines.
        // if you use '#' for walls instead of box drawing chars, you don't need this.
        display.getTextFactory().height(world.cellHeight + 1).initBySize();
        // this makes animations very fast, which is good for multi-cell movement but bad for attack animations.
        display.setAnimationDuration(0.03f);

        //These need to have their positions set before adding any entities if there is an offset involved.
        //There is no offset used here, but it's still a good practice here to set positions early on.
        display.setPosition(0, 0);

        //This is used to allow clicks or taps to take the player to the desired area.
        awaitedMoves = new ArrayList<Coord>(100);
        //playerToCursor = new DijkstraMap(decoDungeon, DijkstraMap.Measurement.MANHATTAN);
        bgColor = SColor.DARK_SLATE_GRAY;
        colorIndices = DungeonUtility.generatePaletteIndices(world.decoDungeon);
        bgColorIndices = DungeonUtility.generateBGPaletteIndices(world.decoDungeon);

        msgbox = new SquidMessageBox(world.gridWidth/2, world.gridHeight/4,world.cellWidth,world.cellHeight);
        msgbox.appendMessage("Welcome to Otherworld!");


        // this is a big one.
        // SquidInput can be constructed with a KeyHandler (which just processes specific keypresses), a SquidMouse
        // (which is given an InputProcessor implementation and can handle multiple kinds of mouse move), or both.
        // keyHandler is meant to be able to handle complex, modified key input, typically for games that distinguish
        // between, say, 'q' and 'Q' for 'quaff' and 'Quip' or whatever obtuse combination you choose. The
        // implementation here handles hjkl keys (also called vi-keys), numpad, arrow keys, and wasd for 4-way movement.
        // Shifted letter keys produce capitalized chars when passed to KeyHandler.handle(), but we don't care about
        // that so we just use two case statements with the same body, i.e. one for 'A' and one for 'a'.
        // You can also set up a series of future moves by clicking within FOV range, using mouseMoved to determine the
        // path to the mouse position with a DijkstraMap (called playerToCursor), and using touchUp to actually trigger
        // the event when someone clicks.
        input = new SquidInput(new SquidInput.KeyHandler()
        {
            @Override
            public void handle(char key, boolean alt, boolean ctrl, boolean shift) {
                switch (key) {
                    case SquidInput.UP_ARROW:
                    case 'w':
                    case 'W': {
                        //-1 is up on the screen
                        player.move(0, -1, world.dungeonGen);
                        creatureTurns();
                        break;
                    }
                    case SquidInput.DOWN_ARROW:
                    case 's':
                    case 'S': {
                        //+1 is down on the screen
                        player.move(0, 1, world.dungeonGen);
                        creatureTurns();
                        break;
                    }
                    case SquidInput.LEFT_ARROW:
                    case 'a':
                    case 'A': {
                        player.move(-1, 0, world.dungeonGen);
                        creatureTurns();
                        break;
                    }
                    case SquidInput.RIGHT_ARROW:
                    case 'd':
                    case 'D': {
                        player.move(1, 0, world.dungeonGen);
                        creatureTurns();

                        break;
                    }

                    case SquidInput.ESCAPE: {
                        Gdx.app.exit();
                        break;
                    }

                    case SquidInput.PAGE_UP:
                    {
                        msgbox.nudgeUp();
                        break;
                    }

                    case SquidInput.PAGE_DOWN:
                    {
                        msgbox.nudgeDown();
                        break;
                    }

                    case 'g' :
                    case ',' :
                    {
                        world.getPlayer().pickup();
                        break;
                    }
                }
            }
        });
                //The second parameter passed to a SquidInput can be a SquidMouse, which takes mouse or touchscreen
                //input and converts it to grid coordinates (here, a cell is 12 wide and 24 tall, so clicking at the
                // pixel position 15,51 will pass screenX as 1 (since if you divide 15 by 12 and round down you get 1),
                // and screenY as 2 (since 51 divided by 24 rounded down is 2)).
             /*   new SquidMouse(cellWidth, cellHeight, gridWidth, gridHeight, 0, 0, new InputAdapter() {

            // if the user clicks and there are no awaitedMoves queued up, generate toCursor if it
            // hasn't been generated already by mouseMoved, then copy it over to awaitedMoves.
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(awaitedMoves.isEmpty()) {
                    if (toCursor.isEmpty()) {
                        cursor = Coord.get(screenX, screenY);
                        //This uses DijkstraMap.findPath to get a possibly long path from the current player position
                        //to the position the user clicked on.
                        toCursor = playerToCursor.findPath(100, null, null, player, cursor);
                    }
                    awaitedMoves = new ArrayList<>(toCursor);
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return mouseMoved(screenX, screenY);
            }

            // causes the path to the mouse position to become highlighted (toCursor contains a list of points that
            // receive highlighting). Uses DijkstraMap.findPath() to find the path, which is surprisingly fast.
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                if(!awaitedMoves.isEmpty())
                    return false;
                if(cursor.x == screenX && cursor.y == screenY)
                {
                    return false;
                }
                cursor = Coord.get(screenX, screenY);
                toCursor = playerToCursor.findPath(100, null, null, player, cursor);
                return false;
            }
        }));*/
        //Setting the InputProcessor is ABSOLUTELY NEEDED TO HANDLE INPUT
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, input));
        //You might be able to get by with the next line instead of the above line, but the former is preferred.
        //Gdx.input.setInputProcessor(input);
        // and then add display, our one visual component, to the list of things that act in Stage.
        display.addActor(msgbox);
        stage.addActor(display);


    }



    /**
     * Draws the map, applies any highlighting for the path to the cursor, and then draws the player.
     */
    public void putMap()
    {
        for (int i = 0; i < world.gridWidth; i++) {
            for (int j = 0; j < world.gridHeight; j++) {
                display.put(i, j, world.lineDungeon[i][j], colorIndices[i][j], bgColorIndices[i][j], 40);
            }
        }
       /* for (Coord pt : toCursor)
        {
            // use a brighter light to trace the path to the cursor, from 170 max lightness to 0 min.
            display.highlight(pt.x, pt.y, 100);
        }*/

        for ( Creature creature : world.getCreatures()
             ) {
            display.put(creature.getCoord().x, creature.getCoord().y, creature.getGlyph(), creature.getColor());

        }
        for ( String s: world.messages )
        {
            msgbox.appendWrappingMessage(s);
        }
        world.messages.clear();
        //this helps compatibility with the HTML target, which doesn't support String.format()
        char[] spaceArray = new char[world.gridWidth];
        Arrays.fill(spaceArray, ' ');
        String spaces = String.valueOf(spaceArray);


        /* r (int i = 0; i < 6; i++) {
            display.putString(0, gridHeight + i + 1, spaces, 0, 1);
            display.putString(2, gridHeight + i + 1, lang[(langIndex + i) % 12], 0, 1);
        }*/
    }
    @Override
    public void render () {
        // standard clear the background routine for libGDX
        Gdx.gl.glClearColor(bgColor.r / 255.0f, bgColor.g / 255.0f, bgColor.b / 255.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // need to display the map every frame, since we clear the screen to avoid artifacts.
        putMap();
        // if the user clicked, we have a list of moves to perform.
        if(!awaitedMoves.isEmpty())
        {
            // this doesn't check for input, but instead processes and removes Points from awaitedMoves.
            secondsWithoutMoves += Gdx.graphics.getDeltaTime();
            if (secondsWithoutMoves >= 0.1) {
                secondsWithoutMoves = 0;
                Coord m = awaitedMoves.remove(0);
                toCursor.remove(0);
                player.move(m.x - player.getCoord().getX(), m.y - player.getCoord().getY(), world.dungeonGen);
            }
        }
        // if we are waiting for the player's input and get input, process it.
        else if(input.hasNext()) {
            input.next();
        }

        // stage has its own batch and must be explicitly told to draw(). this also causes it to act().

        stage.draw();
    }

    @Override
	public void resize(int width, int height) {
		super.resize(width, height);
        //very important to have the mouse behave correctly if the user fullscreens or resizes the game!
		input.getMouse().reinitialize((float) width / world.gridWidth,
                (float)height / (world.gridHeight + 8), world.gridWidth, world.gridHeight, 0, 0);
	}

	public void displayMessages(ArrayList<String> messages)
    {
        for (int i = 0; i < messages.size(); i++)
        {
            msgbox.appendWrappingMessage(messages.get(i));
        }
        messages.clear();
    }

    public void creatureTurns()
    {
        for ( int i = 0; i < world.getCreatures().size(); i++)
        {
            world.getCreatures().get(i).getAi().doTurn();
        }
    }
}
