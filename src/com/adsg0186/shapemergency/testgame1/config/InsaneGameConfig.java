package com.adsg0186.shapemergency.testgame1.config;

import com.adsg0186.shapemergency.testgame1.BonusFactory;
import com.adsg0186.shapemergency.testgame1.TargetUtils;
import com.github.adsgray.gdxtry1.engine.velocity.BlobVelocity;
import com.github.adsgray.gdxtry1.engine.velocity.VelocityIF;

public class InsaneGameConfig extends BaseGameConfig implements GameConfigIF {

   public InsaneGameConfig() {
       super();
        difficultyLevel = 2; // Hard
        numEnemies = 8;
        initialShields = 0;
        initialHitPoints = 45;
        bossScoreIncrement = 900; 
        bonusDropperChance = 5;
        bonusDropperBossPointDiff = 250; 
        bonusDeathChance = 10;
        damageDefender = true; 
        bossFireRate = 3;
        bonusDropSpeed = -18 - bossesKilled; 
        bonusDropperLifeTime = 300;
        shieldLifeTime = 125;
        shieldsUpOverride = false; 
        shieldTickInterval = 175;
        defaultEnemyFireLoop = TargetUtils.fireAtDefenderLoop(800, TargetUtils.targetMissileSource, 1);
        angryEnemyFireLoop = TargetUtils.fireAtDefenderLoop(250, TargetUtils.angryTargetMissileSource, 2);
        defaultEnemyBombVel = new BlobVelocity(0, -19 - 2 * bossesKilled);
    }

    protected void initBonuses() {
        bonuses.add(BonusFactory.get().scoreBonus(5));
        bonuses.add(BonusFactory.get().hitPointBonus(5));
        bonuses.add(BonusFactory.get().shieldBonus(1));
    }

    @Override
    public VelocityIF angryEnemyBombVel() {
        return new BlobVelocity(0,-26 - 2 * bossesKilled); 
    }
}

