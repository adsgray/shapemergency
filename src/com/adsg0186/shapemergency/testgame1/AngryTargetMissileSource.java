package com.adsg0186.shapemergency.testgame1;

import com.adsg0186.shapemergency.testgame1.blobs.EnemyBomb;
import com.adsg0186.shapemergency.testgame1.config.GameConfig;
import com.badlogic.gdx.graphics.Color;
import com.github.adsgray.gdxtry1.engine.WorldIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobSource;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobTransform;
import com.github.adsgray.gdxtry1.engine.blob.BlobPath;
import com.github.adsgray.gdxtry1.engine.output.Renderer;
import com.github.adsgray.gdxtry1.engine.output.Renderer.CircleConfig;
import com.github.adsgray.gdxtry1.engine.position.BlobPosition;
import com.github.adsgray.gdxtry1.engine.position.PositionIF;
import com.github.adsgray.gdxtry1.engine.util.AccelFactory;
import com.github.adsgray.gdxtry1.engine.util.BlobFactory;
import com.github.adsgray.gdxtry1.engine.util.GameFactory;
import com.github.adsgray.gdxtry1.engine.velocity.BlobVelocity;
import com.github.adsgray.gdxtry1.engine.velocity.VelocityIF;
import com.badlogic.gdx.graphics.Color;
public class AngryTargetMissileSource extends BlobSource {

    private BlobTransform transform;
    protected int numHitPoints = 10; // these damage more than the regular bombs

    public AngryTargetMissileSource(BlobTransform transform) {
        this.transform = transform;
    }
    
    // TODO: make these have hitpoints to inflict damage on defender
    @Override
    protected BlobIF generate(BlobIF parent) {
        WorldIF w = parent.getWorld();
        Renderer r = parent.getRenderer();
        BlobPath path = new BlobPath(GameConfig.get().angryEnemyBombVel(), AccelFactory.zeroAccel());
        CircleConfig rc = r.new CircleConfig(Color.CYAN, 14);
        BlobIF b = BlobFactory.circleBlob(new BlobPosition(parent.getPosition()), path, rc, r);
        b = BlobFactory.rainbowColorCycler(b, 1);
        b.setLifeTime(100);
        b = BlobFactory.addTriangleSmokeTrail(b);
        if (transform != null) {
            b = transform.transform(b);
        }
        // This decorator must be last for casting purposes in collision triggers
        b = new EnemyBomb(b, numHitPoints);
        //b.setDebugStr("angryTargetMissile");
        w.addTargetToWorld(b);
        
        GameSound.get().enemyFire();
        
        return b;
    }

}
