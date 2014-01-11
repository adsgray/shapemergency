package com.adsg0186.shapemergency.testgame1.config;

import java.util.ArrayList;
import java.util.List;

import com.adsg0186.shapemergency.testgame1.BonusFactory;
import com.adsg0186.shapemergency.testgame1.BossTargetMissileSource;
import com.adsg0186.shapemergency.testgame1.TargetUtils;
import com.adsg0186.shapemergency.testgame1.BonusFactory.BonusCommandIF;
import com.github.adsgray.gdxtry1.engine.blob.BlobIF.BlobTrigger;
import com.github.adsgray.gdxtry1.engine.position.PositionIF;
import com.github.adsgray.gdxtry1.engine.velocity.BlobVelocity;
import com.github.adsgray.gdxtry1.engine.velocity.VelocityIF;

// this is the "normal" config
public class BaseGameConfig implements GameConfigIF {

    protected int difficultyLevel;
    protected int numEnemies;
    protected int initialShields;
    protected int initialHitPoints;
    protected int bonusDropperChance;
    protected int bossScoreIncrement;
    protected Boolean damageDefender;
    protected int bossesKilled;
    protected int bonusDeathChance;
    protected BlobTrigger defaultEnemyFireLoop;
    protected BlobTrigger angryEnemyFireLoop;
    protected int bossFireRate;
    protected int bonusDropSpeed;
    protected int bonusDropperLifeTime;
    protected int bonusDropperBossPointDiff; // when you're within this many points of a boss, you might get a bonusDropper
    protected int bonusDestroyPenaltyHitPoints;
    protected int shieldLifeTime;
    // this is how long you have to wait until you can put shields up again.
    protected int shieldTickInterval;
    protected Boolean shieldsUpOverride;
    protected VelocityIF defaultEnemyBombVel;
    protected List<BonusCommandIF> bonuses;


    public BaseGameConfig() {
        difficultyLevel = 1; // Normal
        numEnemies = 6;
        initialShields = 1;
        initialHitPoints = 50;
        bossScoreIncrement = 1500; // you'll meet a boss every 1500 points
        bonusDropperChance = 5;
        damageDefender = true;
        bossesKilled = 0;
        defaultEnemyFireLoop = TargetUtils.defaultEnemyFireLoop;
        angryEnemyFireLoop = TargetUtils.angryEnemyFireLoop;
        bossFireRate = 2;
        bonusDropSpeed = -15 - bossesKilled;
        bonusDropperLifeTime = 400;
        bonusDropperBossPointDiff = 500;
        bonusDestroyPenaltyHitPoints = -5; // negative number means you lose hitpoints for destroying a bonus
        bonusDeathChance = 25;
        shieldLifeTime = 150;
        shieldTickInterval = 150;
        shieldsUpOverride = false;
        defaultEnemyBombVel = new BlobVelocity(0, -15 - 2 * bossesKilled);

        bonuses = new ArrayList<BonusCommandIF>();
        initBonuses();
    }
    
    protected void initBonuses() {
        bonuses.add(BonusFactory.get().scoreBonus(10));
        bonuses.add(BonusFactory.get().hitPointBonus(5));
        bonuses.add(BonusFactory.get().shieldBonus(1));
    }

    @Override public int initialShields() { return initialShields; }
    @Override public int initialHitPoints() { return initialHitPoints; }

    @Override public int numEnemies() { return numEnemies + 2 * bossesKilled; }
    @Override public int bonusDropperChance() { return bonusDropperChance; }
    @Override public int bossScoreIncrement() { return bossScoreIncrement; }
    @Override public Boolean damageDefender() { return damageDefender; }
    @Override public void setBossesKilled(int num) { bossesKilled = num; } // for SavedGame restore
    @Override public void incBossesKilled() { bossesKilled += 1; }
    @Override public int getNumBossesKilled() { return bossesKilled; }

    @Override
    public Boolean dropBonusOnDeath() {
        return (TargetUtils.rnd.nextInt(100) < bonusDeathChance);
    }

    @Override
    public BlobTrigger defaultEnemyFireLoop() {
        return defaultEnemyFireLoop;
    }

    @Override
    public BlobTrigger angryEnemyFireLoop() {
        return angryEnemyFireLoop;
    }

    @Override
    public BlobTrigger bossEnemyFireLoop(PositionIF target) {
        int delay = 200 - 5 * bossesKilled;
        if (delay <= 0) delay = 10;
        return TargetUtils.fireAtDefenderLoop(delay, new BossTargetMissileSource(target), bossFireRate);
    }

    @Override public int bonusDropSpeed() { return bonusDropSpeed; }
    @Override public int bonusDropperLifeTime() { return bonusDropperLifeTime; }
    @Override public int bonusDropperBossPointDiff() { return bonusDropperBossPointDiff; }
    @Override public int shieldLifeTime() { return shieldLifeTime; }
    @Override public int shieldTickInterval() { return shieldTickInterval; }
    @Override public Boolean shieldsUpOverride() { return shieldsUpOverride; }

    @Override public VelocityIF defaultEnemyBombVel() { return defaultEnemyBombVel; }

    @Override
    public VelocityIF angryEnemyBombVel() {
        return new BlobVelocity(0,-21 - 2 * bossesKilled);
    }

    @Override
    public int bonusDestroyPenaltyHitPoints() {
        return bonusDestroyPenaltyHitPoints - 2 * bossesKilled;
    }

    @Override
    public BonusCommandIF bonusCommand() {
        int choice = TargetUtils.rnd.nextInt(bonuses.size());
        return bonuses.get(choice);
    }

    @Override
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

}
