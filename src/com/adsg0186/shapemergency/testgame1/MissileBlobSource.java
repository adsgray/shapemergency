package com.adsg0186.shapemergency.testgame1;

import com.adsg0186.shapemergency.testgame1.blobs.EnemyBomb;
import com.badlogic.gdx.graphics.Color;
import com.github.adsgray.gdxtry1.engine.WorldIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobSource;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobTrigger;
import com.github.adsgray.gdxtry1.engine.blob.BlobPath;
import com.github.adsgray.gdxtry1.engine.output.Renderer;
import com.github.adsgray.gdxtry1.engine.output.Renderer.CircleConfig;
import com.github.adsgray.gdxtry1.engine.output.Renderer.TriangleConfig;
import com.github.adsgray.gdxtry1.engine.position.BlobPosition;
import com.github.adsgray.gdxtry1.engine.util.BlobFactory;
import com.github.adsgray.gdxtry1.engine.util.GameCommand;
import com.github.adsgray.gdxtry1.engine.util.PathFactory;

public class MissileBlobSource extends BlobSource {
    GameCommand postKillCommand;
    BlobTrigger collisionTrigger;
    
    public MissileBlobSource(GameCommand gc) {
        postKillCommand = gc;
        collisionTrigger = new MissileCollisionTrigger(postKillCommand);
    }
    
    @Override protected BlobIF generate(BlobIF parent) {
        WorldIF w = parent.getWorld();
        Renderer r = parent.getRenderer();
        BlobPosition p = new BlobPosition(parent);
        //CircleConfig rc = r.new CircleConfig(Color.RED, 25f);
        TriangleConfig rc = r.new TriangleConfig(Color.RED, 25f);

        // create a missile blob whose initial position
        // is based on parent
        BlobIF m = BlobFactory.circleBlob(p, PathFactory.launchUp(100,-4), rc, r);
        m.setLifeTime(75);
        m.registerCollisionTrigger(collisionTrigger);
        m = BlobFactory.addSmokeTrail(m);
        m = BlobFactory.flashColorCycler(m, 7);
        m.setDebugStr("missile");
        m.registerTickDeathTrigger(CreateEnemyTrigger.get());
        w.addMissileToWorld(m);
        return m;
    }
}
