package com.adsg0186.shapemergency.testgame1;

import android.util.Log;

import com.adsg0186.shapemergency.testgame1.GameSound.SoundId;
import com.adsg0186.shapemergency.testgame1.blobs.BonusIF;
import com.adsg0186.shapemergency.testgame1.blobs.DamagableIF;
import com.adsg0186.shapemergency.testgame1.blobs.DamagerIF;
import com.adsg0186.shapemergency.testgame1.blobs.EnemyBomb;
import com.adsg0186.shapemergency.testgame1.blobs.EnemyFactory;
import com.github.adsgray.gdxtry1.engine.WorldIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobTrigger;
import com.github.adsgray.gdxtry1.engine.util.BlobFactory;
import com.github.adsgray.gdxtry1.engine.util.GameCommand;
import com.github.adsgray.gdxtry1.engine.util.TriggerFactory;

public class DefenderCollisionTrigger extends BlobTrigger {

    protected GameCommand damageCommand;

    public DefenderCollisionTrigger(GameCommand gc) { 
        damageCommand = gc; 
    }

    // the defender is a missile
    // and the enemies rain down "targets"
    // this trigger inflicts damage on "source" (defender)
    // and makes secondary explode.
    @Override
    public BlobIF trigger(BlobIF source, BlobIF secondary) {
        if (secondary instanceof EnemyBomb) {
            TriggerFactory.replaceWithExplosion(secondary);
            GameSound.get().playSoundId(SoundId.defenderHit);
            Vibrate.get().vibrate(50);
        }
        
        // A BonusIF is also a DamagerIF so we have
        // to return early if it's a BonusIF
        if (secondary instanceof BonusIF) {
            // TODO: special explosion for bonuses
            // TODO: flash message "+5 HitPoints!"
            TargetUtils.replaceWithBonusExplosion(secondary);
            ((BonusIF)secondary).grantBonus();
            return source;
        }

        // could either be a bomb or a hitpoint bonus
        if (secondary instanceof DamagerIF) {
            DamagerIF bomb = (DamagerIF)secondary;
            damageCommand.execute(bomb.getHitPoints());
        }

        return source;
    }

}
