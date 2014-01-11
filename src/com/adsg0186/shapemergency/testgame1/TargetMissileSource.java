package com.adsg0186.shapemergency.testgame1;

import com.adsg0186.shapemergency.testgame1.blobs.EnemyBomb;
import com.adsg0186.shapemergency.testgame1.config.GameConfig;
import com.badlogic.gdx.graphics.Color;
import com.github.adsgray.gdxtry1.engine.WorldIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobSource;
import com.github.adsgray.gdxtry1.engine.blob.BlobPath;
import com.github.adsgray.gdxtry1.engine.output.Renderer;
import com.github.adsgray.gdxtry1.engine.output.Renderer.CircleConfig;
import com.github.adsgray.gdxtry1.engine.position.BlobPosition;
import com.github.adsgray.gdxtry1.engine.util.AccelFactory;
import com.github.adsgray.gdxtry1.engine.util.BlobFactory;
import com.github.adsgray.gdxtry1.engine.velocity.BlobVelocity;
import com.github.adsgray.gdxtry1.engine.velocity.VelocityIF;

public class TargetMissileSource extends BlobSource {

    protected int numHitPoints = 5;


    @Override
    protected BlobIF generate(BlobIF parent) {
        WorldIF w = parent.getWorld();
        Renderer r = parent.getRenderer();
        BlobPath path = new BlobPath(GameConfig.get().defaultEnemyBombVel(), AccelFactory.zeroAccel());
        CircleConfig rc = r.new CircleConfig(Color.CYAN, 20);
        BlobIF b = BlobFactory.circleBlob(new BlobPosition(parent.getPosition()), path, rc, r);
        b.setLifeTime(200);
        b = BlobFactory.addTriangleSmokeTrail(b);
        // This decorator must be last for casting purposes in collision triggers
        b = new EnemyBomb(b, numHitPoints);
        //b.setDebugStr("targetmissile");
        w.addTargetToWorld(b);
        GameSound.get().enemyFire();
        return b;
    }

}
