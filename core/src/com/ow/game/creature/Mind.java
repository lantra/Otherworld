package com.ow.game.creature;

import com.ow.game.creature.jobs.*;
import squidpony.squidgrid.LOS;
import squidpony.squidmath.RNG;

/**
 * This class and subclasses dictate how a creature behaves, uses or abilities. Some other funcationality not relating
 * could be passed through the mind (AI) subset
 */
public class Mind
{
    protected Creature creature;
    protected int av, dv;
    protected RNG rng;
    protected LOS los;

    public Mind (Creature c)
    {
        this.creature = c;
        this.creature.setAi(this);
        this.av = 0;
        this.dv = 0;
        this.rng = new RNG();
        los = new LOS();
    }

    public int getWeaponAVMod() { return 0;
    }


    public int getAv() {
        return av;
    }

    public void setAv(int av) {
        this.av = av;
    }

    public int getDv() {
        return dv;
    }

    public void setDv(int dv) {
        this.dv = dv;
    }

    public int getDamageV()
    {
        return (this.creature.getStr() / 4) + rng.between(1,4);
    }

    public boolean canSeeTarget(Creature c)
    {
        return los.isReachable(creature.getWorld().decoDungeon, creature.getCoord().x, creature.getCoord().y,
                c.getCoord().x, c.getCoord().y);
    }

    public void onNotify(String format)
    {

    }

    public void doTurn() //here monsters will decide what to do with their life
    {

    }
}
