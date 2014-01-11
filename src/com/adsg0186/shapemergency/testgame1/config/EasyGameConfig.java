package com.adsg0186.shapemergency.testgame1.config;

import com.adsg0186.shapemergency.testgame1.BonusFactory;
import com.adsg0186.shapemergency.testgame1.TargetUtils;
import com.github.adsgray.gdxtry1.engine.velocity.BlobVelocity;
import com.github.adsgray.gdxtry1.engine.velocity.VelocityIF;

public class EasyGameConfig extends BaseGameConfig implements GameConfigIF {

    public EasyGameConfig() {
        super();
        // now override some values:
        difficultyLevel = 0; // Easy
        numEnemies = 4;
        initialShields = 2;
        initialHitPoints = 75;
        bossScoreIncrement = 1000000000; // never? James doesn't like Bosses.
        bonusDropperChance = 5;
        bonusDeathChance = 100;
        bonusDropperBossPointDiff = bossScoreIncrement; // so always?
        damageDefender = true; // can't make it *too* easy
        bossFireRate = 2;
        bonusDropSpeed = -9; // make them drop slower
        bonusDropperLifeTime = 1000;
        shieldLifeTime = 500;
        shieldsUpOverride = true; // no limits on deploying shields
        defaultEnemyFireLoop = TargetUtils.fireAtDefenderLoop(2000, TargetUtils.targetMissileSource, 1);
        angryEnemyFireLoop = TargetUtils.fireAtDefenderLoop(500, TargetUtils.angryTargetMissileSource, 1);
        defaultEnemyBombVel = new BlobVelocity(0, -10);
    }

    protected void initBonuses() {
        bonuses.add(BonusFactory.get().scoreBonus(15));
        bonuses.add(BonusFactory.get().hitPointBonus(10));
        bonuses.add(BonusFactory.get().shieldBonus(2));
    }

    @Override
    public VelocityIF angryEnemyBombVel() {
        return new BlobVelocity(0,-15);
    }
}
