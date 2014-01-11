package com.adsg0186.shapemergency.testgame1.blobs;

import com.github.adsgray.gdxtry1.engine.blob.BlobIF;

public interface EnemyIF {
    
    public BlobIF reactToMissileHit(BlobIF missile);
    public int getWeight(); // how many enemies is this enemy worth?kj

}
