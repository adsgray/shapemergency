package com.adsg0186.shapemergency.testgame1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.github.adsgray.gdxtry1.engine.WorldIF;
import com.github.adsgray.gdxtry1.engine.accel.LinearAccel;
import com.github.adsgray.gdxtry1.engine.blob.BaseTextBlob;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobSource;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobTransform;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobTrigger;
import com.github.adsgray.gdxtry1.engine.blob.BlobPath;
import com.github.adsgray.gdxtry1.engine.blob.ExplosionBlob;
import com.github.adsgray.gdxtry1.engine.blob.TextBlobIF;
import com.github.adsgray.gdxtry1.engine.output.Renderer;
import com.github.adsgray.gdxtry1.engine.output.Renderer.CircleConfig;
import com.github.adsgray.gdxtry1.engine.output.Renderer.TextConfig;
import com.github.adsgray.gdxtry1.engine.position.BlobPosition;
import com.github.adsgray.gdxtry1.engine.position.PositionIF;
import com.github.adsgray.gdxtry1.engine.util.AccelFactory;
import com.github.adsgray.gdxtry1.engine.util.BlobFactory;
import com.github.adsgray.gdxtry1.engine.util.GameFactory;
import com.github.adsgray.gdxtry1.engine.util.PathFactory;
import com.github.adsgray.gdxtry1.engine.util.TriggerFactory;
import com.github.adsgray.gdxtry1.engine.velocity.BlobVelocity;
import com.github.adsgray.gdxtry1.engine.velocity.VelocityIF;

public class TargetUtils {

    public static Random rnd = new Random();
    public static BlobSource targetMissileSource = new TargetMissileSource();
    
    public enum Difficulty {
        easy,
        normal
    }

    public static Difficulty difficulty = Difficulty.normal;

    public static BlobTransform displaceBomb = new BlobTransform() {
        @Override public BlobIF transform(BlobIF b) {
            b.setTickPause(TargetUtils.rnd.nextInt(20));
            VelocityIF vel = b.getVelocity();
            vel.setXVelocity(rnd.nextInt(8) - 4);
            //PositionIF pos = b.getPosition();
            //pos.setX(pos.getX() + TargetUtils.rnd.nextInt(50) - 25);
            return b;
        }
    };
    public static BlobSource angryTargetMissileSource = new AngryTargetMissileSource(displaceBomb);
    
    public static BlobTransform disarmMissile = new BlobTransform() {
        @Override public BlobIF transform(BlobIF b) {
            VelocityIF v = b.getVelocity();
            b.setVelocity(new BlobVelocity(-v.getXVelocity(), v.getYVelocity()/4));
            b.setAccel(new LinearAccel(0, -2));
            b = BlobFactory.shrinker(b, 2);
            b.setLifeTime(50);
            return b;
        }
    };

    private static class missileTransform extends BlobTransform {

        private int maxLifeTime;
        private BlobSource missileSource;
        int num;

        public missileTransform(int maxLifeTime, BlobSource missileSource, int num) {
            this.maxLifeTime = maxLifeTime;
            this.missileSource = missileSource;
            this.num = num;
        }

        @Override public BlobIF transform(BlobIF b) {
            b.setLifeTime(rnd.nextInt(maxLifeTime));
            for (int i = 0; i < num; i++) {
                BlobIF thing = missileSource.get(b);
            }
            return b;
        }
        
    }
    // function to generate a sequence of tickDeathTriggers
    // which cause a 'target' to shoot 'targets' down at the defender
    public static BlobTrigger fireAtDefenderLoop(int maxLifeTime, BlobSource missileSource, int num) {
        BlobTransform fireAtDefender = new missileTransform(maxLifeTime, missileSource, num);
        List<BlobTransform> transforms = new ArrayList<BlobTransform>();
        transforms.add(fireAtDefender);
        
        return TriggerFactory.createTransformSequence(transforms, true);
    }
    
    public static BlobTrigger defaultEnemyFireLoop = TargetUtils.fireAtDefenderLoop(1000, TargetUtils.targetMissileSource, 1);
    public static BlobTrigger angryEnemyFireLoop = TargetUtils.fireAtDefenderLoop(350, TargetUtils.angryTargetMissileSource, 1);
    
    public static BlobIF becomeAngryExplosion(BlobIF source) {
        //PositionIF p = new BlobPosition(source.getPosition());
        // have the explosion follow the parent around!
        PositionIF p = source.getPosition();
        Renderer r = source.getRenderer();
        BlobPath path = PathFactory.jigglePath(2);
        ExplosionBlob ex = new ExplosionBlob(0, p, path.vel, path.acc, r);
        
        BlobSource bs = new BlobSource() {
            @Override protected BlobIF generate(BlobIF parent) {
                Renderer r = parent.getRenderer();
                WorldIF w = parent.getWorld();
                // ugh, always forget to clone the position...
                PositionIF p = new BlobPosition(parent.getPosition());
                BlobPath path = new BlobPath(BlobFactory.randomVelocity(), AccelFactory.explosionAccel());
                CircleConfig rc = r.new CircleConfig(BlobFactory.randomColor(), 6);
                BlobIF b = BlobFactory.circleBlob(p, path, rc, r);
                b.setLifeTime(15);
                w.addBlobToWorld(b);
                return b;
            }
        };
        ex.setLifeTime(20);
        ex.setBlobSource(bs);
        return ex;
    }
    
    public static BlobPath chooseBackAndForthPath(PositionIF p, int speed, int interval) {
        // if on the left, go the the right
        // if on the right, go to the left
        if (p.getX() < GameFactory.BOUNDS_X / 2) {
            return PathFactory.backAndForth(speed,interval);
        } else {
            return PathFactory.backAndForthLeft(speed,interval);
        }       
    }

    public static BlobPath chooseBackAndForthPath(PositionIF p) {
        //return chooseBackAndForthPath(p, 15, 5);
        return chooseBackAndForthPath(p, 20, 5);
    }
    
    public static BlobIF replaceWithBonusExplosion(BlobIF b) {
        WorldIF w = b.getWorld();
        Renderer r = b.getRenderer();
        
        CircleConfig rc = r.new CircleConfig(Color.WHITE, 5);
        BlobIF whiteBurst = BlobFactory.circleBlob(new BlobPosition(b.getPosition()), PathFactory.stationary(), rc, r);
        whiteBurst = BlobFactory.grower(whiteBurst, 1);
        whiteBurst.setLifeTime(25);

        w.removeBlobFromWorld(b);
        w.addBlobToWorld(whiteBurst);

        return whiteBurst;
    }
    
    public static TextBlobIF flashMessage(String txt, WorldIF w, Renderer r) {
        PositionIF p = new BlobPosition(25,500);
        BlobPath path = PathFactory.stationary();
        TextConfig rc = r.new TextConfig(Color.WHITE, 2.5f);
        TextBlobIF t = new BaseTextBlob(p, path.vel, path.acc, r, rc);
        t.setText(txt);
        t.setLifeTime(100);
        t.setWorld(w);
        t = (TextBlobIF) BlobFactory.grower(t, 5);
        w.addBlobToWorld(t);
        return t;
    }
}
