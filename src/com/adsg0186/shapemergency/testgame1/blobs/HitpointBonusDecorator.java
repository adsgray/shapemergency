package com.adsg0186.shapemergency.testgame1.blobs;

import com.adsg0186.shapemergency.testgame1.GameSound;
import com.adsg0186.shapemergency.testgame1.TargetUtils;
import com.adsg0186.shapemergency.testgame1.BonusFactory.BonusCommandIF;
import com.adsg0186.shapemergency.testgame1.GameSound.SoundId;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF;
import com.github.adsgray.gdxtry1.engine.blob.TextBlobIF;
import com.github.adsgray.gdxtry1.engine.blob.decorator.BlobDecorator;
import com.github.adsgray.gdxtry1.engine.util.TriggerFactory;

public class HitpointBonusDecorator extends BlobDecorator implements BonusIF, EnemyIF {

    protected int hitPoints;
    protected TextBlobIF companionText;
    protected BonusCommandIF bonusCommand;

    public HitpointBonusDecorator(BlobIF component, TextBlobIF companionText, int hitPoints, BonusCommandIF bonusCommand) {
        super(component);
        this.hitPoints = hitPoints;
        this.companionText = companionText;
        this.bonusCommand = bonusCommand;
    }

    @Override
    public int getHitPoints() { return hitPoints; }
    
    protected void destroyCompanionBlobs() {
        companionText.setLifeTime(0);
    }

    @Override
    public BlobIF reactToMissileHit(BlobIF missile) {
        destroyCompanionBlobs();
        EnemyFactory.flashMessage(world, renderer, "Lost Bonus :-(", 30);
        GameSound.get().playSoundId(SoundId.defenderHit); // use this one for now...
        return TargetUtils.replaceWithBonusExplosion(this);
    }

    @Override public int getWeight() { return 1; }

    @Override
    public void grantBonus() {
        destroyCompanionBlobs();
        bonusCommand.execute();
    }

}
