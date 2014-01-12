package com.adsg0186.shapemergency.testgame1.blobs;

import android.util.Log;

import com.adsg0186.shapemergency.testgame1.GameSound;
import com.adsg0186.shapemergency.testgame1.MissileBlobSource;
import com.adsg0186.shapemergency.testgame1.MissileCollisionTrigger;
import com.adsg0186.shapemergency.testgame1.ShieldCollisionTrigger;
import com.adsg0186.shapemergency.testgame1.TargetUtils;
import com.adsg0186.shapemergency.testgame1.Vibrate;
import com.adsg0186.shapemergency.testgame1.GameSound.SoundId;
import com.adsg0186.shapemergency.testgame1.config.GameConfig;
import com.badlogic.gdx.graphics.Color;
import com.github.adsgray.gdxtry1.engine.WorldIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.decorator.BlobDecorator;
import com.github.adsgray.gdxtry1.engine.blob.decorator.ShowExtentDecorator;
import com.github.adsgray.gdxtry1.engine.extent.CircleExtent;
import com.github.adsgray.gdxtry1.engine.extent.ExtentIF;
import com.github.adsgray.gdxtry1.engine.input.Draggable;
import com.github.adsgray.gdxtry1.engine.input.Flingable;
import com.github.adsgray.gdxtry1.engine.input.SimpleDirectionGestureDetector.DirectionListener.FlingInfo;
import com.github.adsgray.gdxtry1.engine.input.Tappable;
import com.github.adsgray.gdxtry1.engine.output.Renderer;
import com.github.adsgray.gdxtry1.engine.output.Renderer.CircleConfig;
import com.github.adsgray.gdxtry1.engine.output.Renderer.RectConfig;
import com.github.adsgray.gdxtry1.engine.position.BlobPosition;
import com.github.adsgray.gdxtry1.engine.position.PositionComposeDecorator;
import com.github.adsgray.gdxtry1.engine.position.PositionIF;
import com.github.adsgray.gdxtry1.engine.util.BlobFactory;
import com.github.adsgray.gdxtry1.engine.util.GameCommand;
import com.github.adsgray.gdxtry1.engine.util.PathFactory;

public class FiringBlobDecorator extends BlobDecorator implements
        Flingable, Draggable, Tappable, DamagableIF {

    protected BlobSource missileSource;
    protected ExtentIF flingExtent;
    protected ExtentIF dragExtent;
    protected ExtentIF tapExtent;
    protected int hitPoints;
    protected int maxMissiles; // max missiles in the air at one time
    protected int numShields;
    protected int shieldTrackId;
    protected BlobTrigger shieldCollisionTrigger;
    GameCommand incShield;

    public FiringBlobDecorator(BlobIF component, GameCommand postKillCommand, GameCommand incShield) {
        super(component);
        missileSource = new MissileBlobSource(postKillCommand);
        // shield acts like a "missile"
        this.shieldCollisionTrigger = new ShieldCollisionTrigger(postKillCommand);
        CircleExtent ce = (CircleExtent)component.getExtent();
        flingExtent = new CircleExtent(ce.getRadius() * 3);
        dragExtent = new CircleExtent(ce.getRadius() * 3);
        tapExtent = new CircleExtent(ce.getRadius() * 3);
        hitPoints = 0; // set by the FiringGame class in setupGame
        maxMissiles = 3; // the defender/triangle counts as a missile, so this means
                         // you can launch up to 2 simultaneous missiles
        this.incShield = incShield;
        numShields = 0; // the controlling Game tells us how many shields we start with after creating us
    }

    @Override public void setWorld(WorldIF w) {
        super.setWorld(w);
        shieldTrackId = world.createTrackableBlobList();
    }

    public int incrementNumShields(int ct) {
        numShields += ct;
        return numShields;
    }

    @Override
    public void onFlingUp(FlingInfo f) {

    }

    @Override public void onFlingLeft(FlingInfo f) { }
    @Override public void onFlingRight(FlingInfo f) { }

    @Override public void onFlingDown(FlingInfo f) { 
        shieldsUp();
    }

    @Override
    public void onTap(PositionIF pos, int count) {
         if (world.getNumMissiles() < maxMissiles) {
            BlobIF missile = missileSource.get(this);
            GameSound.get().playSoundId(SoundId.shoot);
            Vibrate.get().vibrate(25);
        }
        // else make the defender flash/shake?
        // OK now set the missile's velocity based on FlingInfo       
    }

    @Override
    public void panStarted(PositionIF start) {
        // TODO Auto-generated method stub
    }

    @Override
    public void panInProgress(PositionIF cur) {
        // discard Y coord changes
        // what if position is a decorator?
        PositionIF p = component.getPosition();
        p.setX(cur.getX());
        // TODO: allow a small range of Y movement
        //p.setY(cur.getY());
    }

    @Override
    public void completePan(PositionIF start, PositionIF stop) {
        // TODO Auto-generated method stub
    }

    @Override public ExtentIF getFlingExtent() { return flingExtent; }
    @Override public ExtentIF getDragExtent() { return dragExtent; }
    @Override public ExtentIF getTapExtent() { return tapExtent; }

    // Damagable:
    @Override public int setHitPoints(int hp) { hitPoints = hp; return hitPoints; }
    @Override public int incHitPoints(int hp) { hitPoints += hp; return hitPoints; }
    @Override public int decHitPoints(int hp) { hitPoints -= hp; return hitPoints; }
    @Override public int getHitPoints() { return hitPoints; }
    public int getShields() { return numShields; }
    
    private int ticksWhenShieldsWentUp = 0;

    protected Boolean canDoShieldsUp() {
        // if config says yes, that overrides the later calculation
        boolean shieldUpAlready = world.trackableBlobListCount(shieldTrackId) > 0;

        if (shieldUpAlready) {
            return false;
        }

        return GameConfig.get().shieldsUpOverride() ||
                (numShields > 0 && ticks - ticksWhenShieldsWentUp >= GameConfig.get().shieldTickInterval());
    }

    public void shieldsUp() {
        if (!canDoShieldsUp()) {
            GameSound.get().playSoundId(SoundId.shieldDenied);
            EnemyFactory.flashMessage(world, renderer, "Shield Denied!", 20);
            return;
        }

        incShield.execute(-1);
        ticksWhenShieldsWentUp = ticks;

        // shield moves with us
        PositionIF p = new PositionComposeDecorator(component.getPosition(), new BlobPosition(0, 90));
        RectConfig rc = renderer.new RectConfig(Color.RED, 100, 15);
        BlobIF b = BlobFactory.rectangleBlob(p, PathFactory.stationary(), rc, renderer);
        b.setExtent(new CircleExtent(100));
        b.setLifeTime(GameConfig.get().shieldLifeTime());
        b = BlobFactory.flashColorCycler(b, 1);
        b = BlobFactory.throbber(b);
        b.setWorld(world);
        world.addBlobToTrackableBlobList(shieldTrackId, b);
        
        // behave like a missile
        b.registerCollisionTrigger(shieldCollisionTrigger);
        world.addMissileToWorld(b);
    }
    
    @Override public Boolean tick() {
        // TODO idea: ammo limit, you get 1 ammo every 100 ticks
        // and also get 2 ammos when you destroy an ememy object
        // would have to add ammo to score display
        ticks++;
        return component.tick();
    }


}
