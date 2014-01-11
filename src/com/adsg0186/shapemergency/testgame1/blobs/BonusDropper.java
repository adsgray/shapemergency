package com.adsg0186.shapemergency.testgame1.blobs;

import com.adsg0186.shapemergency.testgame1.GameSound;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.decorator.BlobDecorator;
import com.github.adsgray.gdxtry1.engine.util.TriggerFactory;


public class BonusDropper extends BlobDecorator implements DamagableIF, EnemyIF {

    protected int numBonuses;

    public BonusDropper(BlobIF component, int numBonuses) {
        super(component);
        this.numBonuses = numBonuses;
    }

    protected BlobIF died() {
        GameSound.get().explosionAll();
        return TriggerFactory.replaceWithExplosion(this);
    }

    @Override
    public BlobIF reactToMissileHit(BlobIF missile) {
        BlobIF bonus = EnemyFactory.hitPointBonusSource.get(this);

        numBonuses -= 1;
        
        if (numBonuses == 0) {
            return died();
        }
        return this;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    // you get no points for hitting this. you just get to catch the bonus it drops...
    @Override public int setHitPoints(int hp) { return 0; }
    @Override public int incHitPoints(int hp) { return 0; }
    @Override public int decHitPoints(int hp) { return 0; }
    @Override public int getHitPoints() { return 0; }

}
