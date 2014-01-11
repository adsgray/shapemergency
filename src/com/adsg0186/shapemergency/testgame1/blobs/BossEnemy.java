package com.adsg0186.shapemergency.testgame1.blobs;

import com.adsg0186.shapemergency.testgame1.BossTargetMissileSource;
import com.adsg0186.shapemergency.testgame1.GameSound;
import com.adsg0186.shapemergency.testgame1.TargetUtils;
import com.adsg0186.shapemergency.testgame1.config.GameConfig;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.decorator.BlobDecorator;
import com.github.adsgray.gdxtry1.engine.position.BlobPosition;
import com.github.adsgray.gdxtry1.engine.position.PositionIF;
import com.github.adsgray.gdxtry1.engine.util.PositionFactory;
import com.github.adsgray.gdxtry1.engine.util.TriggerFactory;
import com.github.adsgray.gdxtry1.engine.velocity.BlobVelocity;
import com.github.adsgray.gdxtry1.engine.velocity.VelocityIF;

public class BossEnemy extends BlobDecorator implements DamagerIF, DamagableIF, EnemyIF {

    // TODO: put all of this into GameConfig
    protected int hitPoints = 75;
    protected PositionIF aimTarget;
    protected int bonusAfterHitChance = 25; 
    protected int goLowerTickCount = 400;
    BlobSource missileSource;

    public BossEnemy(BlobIF component, PositionIF aimTarget) {
        super(component);
        this.aimTarget = aimTarget;
        hitPoints += 5 * GameConfig.get().getNumBossesKilled();
        missileSource = new BossTargetMissileSource(aimTarget);
    }
    
    protected void sendAimedBombs(int howMany) {
        for (int i = 0; i < howMany; i++) {
            // send an aimed bomb
            BlobIF bomb = missileSource.get(this);
            bomb.setWorld(world);
            world.addTargetToWorld(bomb);
        }
    }
    
    protected void sendBonuses(int howMany) {
         for (int i = 0; i < howMany; i++) {
            BlobIF b = EnemyFactory.hitPointBonusSource.get(this);
            b.setTickPause(TargetUtils.rnd.nextInt(20));
        }       
    }
    
    // called when this enemy has died
    protected BlobIF died() {
        // throw out a bunch of bombs
        // TODO: put these in GameConfig
        sendAimedBombs(4);
        sendBonuses(1);
        // two for good measure
        GameSound.get().explosionLong();
        GameSound.get().explosionLong();
        GameConfig.get().incBossesKilled();
        return TriggerFactory.replaceWithExplosion(this);
    }

    @Override
    public BlobIF reactToMissileHit(BlobIF missile) {
        int hpToDeduct = 5;

        // TODO: make missiles into DamagerIFs??
        // is that possible with the way that
        // collision triggers work?
        if (missile instanceof DamagerIF) {
            hpToDeduct = ((DamagerIF)missile).getHitPoints();
        }

        hitPoints -= hpToDeduct;
        
        if (hitPoints <= 0) {
            return died();
        }
        
        // send some aimed bombs 
        // any number more than 1 makes it waaaay too difficult
        sendAimedBombs(1);
        
        if (TargetUtils.rnd.nextInt(100) < bonusAfterHitChance) {
            sendBonuses(1);
        }
        
        return this;
    }

    // DamagableIF
    @Override public int setHitPoints(int hp) { hitPoints = hp; return hitPoints; }
    @Override public int incHitPoints(int hp) { return setHitPoints(hp + hitPoints); }
    @Override public int decHitPoints(int hp) { return setHitPoints(hitPoints - hp); }
    // this is how much each hit is worth in terms of points...
    @Override public int getHitPoints() { return 15; }
    
    @Override public Boolean tick() {
        ticks++;
        if (ticks % goLowerTickCount == 0) {
            PositionIF p = getPosition();
            p.setY(p.getY() - 5);
        }
        return component.tick();
    }

    @Override public int getWeight() { return 10; } 
}
