package com.adsg0186.shapemergency.testgame1;

import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobTrigger;
import com.github.adsgray.gdxtry1.engine.util.GameCommand;

// A workaround to the problem of an "empty world"
// Attach this as a tickDeathTrigger to missiles?
public class CreateEnemyTrigger extends BlobTrigger {

    protected GameCommand createEnemies;

    public CreateEnemyTrigger(GameCommand gc) {
        createEnemies = gc;
    }

    @Override
    public BlobIF trigger(BlobIF source, BlobIF secondary) {
        createEnemies.execute(0);
        return source;
    }
    
    // singleton:
    protected static BlobTrigger instance;
    public static void createInstance(GameCommand gc) {
        instance = new CreateEnemyTrigger(gc);
    }
    public static BlobTrigger get() {
        return instance;
    }
}
