package com.adsg0186.shapemergency.testgame1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import android.util.Log;

import com.adsg0186.shapemergency.testgame1.GameSound.SoundId;
import com.adsg0186.shapemergency.testgame1.blobs.EnemyFactory;
import com.github.adsgray.gdxtry1.engine.WorldIF;
import com.github.adsgray.gdxtry1.engine.output.Renderer;
import com.github.adsgray.gdxtry1.engine.util.GameCommand;

/*
 */
public class BonusFactory {

    protected GameCommand incShield;
    protected GameCommand incHitPoints;
    protected GameCommand incScore;
    // required for creating FlashMessages
    protected WorldIF world;
    protected Renderer renderer;
    

    public BonusFactory(FiringGameTest game, WorldIF world, Renderer renderer) {
        this.world = world;
        this.renderer = renderer;

        incShield = game.new IncShield();
        incHitPoints = game.new IncHitPoints();
        incScore = game.new IncScore();
    }
    
    public static interface BonusCommandIF {
        public void execute();
    }

    public static class BonusCommand implements BonusCommandIF {
        protected GameCommand cmd;
        protected int num;

        public BonusCommand(GameCommand cmd, int num) {
            this.cmd = cmd;
            this.num = num;
        }

        @Override
        public void execute() {
            cmd.execute(num);
        }
        
    }
    
    protected void postBonusExecute(String msg) {
        EnemyFactory.flashMessage(world, renderer, msg, 70);
        GameSound.get().playSoundId(SoundId.bonusReceive);
    }

    public BonusCommandIF shieldBonus(int num) {
        GameCommand bonus = new GameCommand() {
            @Override public void execute(int num) {
                incShield.execute(num);
                postBonusExecute(String.format("%d Shield Bonus!", num));
            }
        };
        
        return new BonusCommand(bonus, num);
    } 
    
    public BonusCommandIF hitPointBonus(int num) {
        GameCommand bonus = new GameCommand() {
            @Override public void execute(int num) {
                incHitPoints.execute(num);
                postBonusExecute(String.format("%d Health Bonus!", num));
            }
        };
        
        return new BonusCommand(bonus, num);
    }
   
    public BonusCommandIF scoreBonus(int num) {
        GameCommand bonus = new GameCommand() {
            @Override public void execute(int num) {
                incScore.execute(num);
                postBonusExecute(String.format("%d Score Bonus!", num));
            }
        };
        
        return new BonusCommand(bonus, num);
    }
    
    // singleton
    protected static BonusFactory instance;
    public static BonusFactory createInstance(FiringGameTest game, WorldIF world, Renderer renderer) {
        // clobber instance. Anything that references world/renderer has to
        // be clobbered on creation.
        instance = null;
        if (instance == null) {
            instance = new BonusFactory(game, world, renderer);
        }
        return instance;
    }
    public static BonusFactory get() { return instance; }
}
