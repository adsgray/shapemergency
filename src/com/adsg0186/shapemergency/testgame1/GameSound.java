package com.adsg0186.shapemergency.testgame1;

import java.util.EnumMap;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.adsg0186.blobmergency.R;
import com.github.adsgray.gdxtry1.engine.output.NullSound;
import com.github.adsgray.gdxtry1.engine.output.SoundIF;
import com.github.adsgray.gdxtry1.engine.output.SoundPoolPlayer;

public class GameSound {
    private SoundIF soundpool;
    private static GameSound instance;
    private static GameSound realInstance; // plays sound
    private static GameSound fakeInstance; // does not play any sound
    
    public static enum SoundId {
        welcome,
        
        shoot,
        
        enemyFire1,
        enemyFire2,
        enemyFire3,
        enemyFire4,
        enemyFire5,

        enemyCreated,
        enemyBecomeAngry,

        explosion1, // explosion.mp3
        explosion2, // explosion4.mp3
        explosion3, // explosion5.mp3
        explosion4, // explosion6.mp3
        explosionshort1, // explosionshort.mp3
        explosionshort2, // explosionshort2.mp3

        shieldDenied,
        
        bonusDrop,
        bonusDropperAppear,
        bonusReceive,
        bonusShieldReceive,
        
        defenderHit,
        shieldHit1,
        shieldHit2,

        bossDie,
    }
    
    private EnumMap<SoundId, Integer> sounds;
    private Random rnd;
    
    protected void initSoundsFromSoundpool() {
        sounds = new EnumMap<SoundId, Integer>(SoundId.class);
        rnd = new Random();
    
        sounds.put(SoundId.welcome, soundpool.load(R.raw.welcome));
        sounds.put(SoundId.shoot, soundpool.load(R.raw.shoot1));

        sounds.put(SoundId.bonusDropperAppear, soundpool.load(R.raw.bonusdropper));
        sounds.put(SoundId.bonusReceive, soundpool.load(R.raw.bonusreceive));
        sounds.put(SoundId.bonusShieldReceive, soundpool.load(R.raw.bonusshield));
        sounds.put(SoundId.defenderHit, soundpool.load(R.raw.defenderhit));
        
        sounds.put(SoundId.shieldHit1, soundpool.load(R.raw.shieldhit));
        sounds.put(SoundId.shieldHit2, soundpool.load(R.raw.shieldhit2));
        sounds.put(SoundId.bossDie, soundpool.load(R.raw.bossdie));
        sounds.put(SoundId.bonusDrop, soundpool.load(R.raw.bonusdrop));

        sounds.put(SoundId.enemyCreated, soundpool.load(R.raw.enemycreated));
        sounds.put(SoundId.enemyBecomeAngry, soundpool.load(R.raw.becomeangry));

        sounds.put(SoundId.shieldDenied, soundpool.load(R.raw.noshield));

        sounds.put(SoundId.enemyFire1, soundpool.load(R.raw.enemyfire1));
        sounds.put(SoundId.enemyFire2, soundpool.load(R.raw.enemyfire2));
        sounds.put(SoundId.enemyFire3, soundpool.load(R.raw.enemyfire3));
        sounds.put(SoundId.enemyFire4, soundpool.load(R.raw.enemyfire4));
        sounds.put(SoundId.enemyFire5, soundpool.load(R.raw.enemyfire5));

        sounds.put(SoundId.explosion1, soundpool.load(R.raw.explosion));
        sounds.put(SoundId.explosion2, soundpool.load(R.raw.explosion4));
        sounds.put(SoundId.explosion3, soundpool.load(R.raw.explosion5));
        sounds.put(SoundId.explosion4, soundpool.load(R.raw.explosion6));
        sounds.put(SoundId.explosionshort1, soundpool.load(R.raw.explosionshort));
        sounds.put(SoundId.explosionshort2, soundpool.load(R.raw.explosionshort2));
    }

    public GameSound(Context context) {
        soundpool = new SoundPoolPlayer(context);
        initSoundsFromSoundpool();
    }
    
    public GameSound() {
        soundpool = new NullSound();
        initSoundsFromSoundpool();
    }

    public static GameSound get() {
        return instance;
    }
    
    
    public static void setRealInstance(Context context) {
        if (realInstance == null) {
            realInstance = new GameSound(context);
        }
        instance = realInstance;
    }
    
    public static void setFakeInstance() {
        if (fakeInstance == null) {
            fakeInstance = new GameSound();
        }
        instance = fakeInstance;
    }
    
    // game specific stuff:

    public void playSoundId(SoundId sid) {
        soundpool.play((int)sounds.get(sid));
    }
 
    private void playRandom(SoundId[] soundlist) {
        int idx = rnd.nextInt(soundlist.length);
        playSoundId(soundlist[idx]);
    }
    
    private static SoundId[] explosionsLong = new SoundId[] {
            SoundId.explosion1,
            SoundId.explosion2,
            SoundId.explosion3,
            SoundId.explosion4,
    };
    public void explosionLong() {
        playRandom(explosionsLong);
    }
 
    private static SoundId[] explosionsShort = new SoundId[] {
            SoundId.explosionshort1,
            SoundId.explosionshort2,
    };
    public void explosionShort() {
        playRandom(explosionsShort);
    }
 
    private static SoundId[] explosionsAll = new SoundId[] {
            SoundId.explosionshort1,
            SoundId.explosionshort2,
            SoundId.explosion1,
            SoundId.explosion2,
            SoundId.explosion3,
            SoundId.explosion4,
    };
    public void explosionAll() {
        playRandom(explosionsAll);
    }

    private static SoundId[] enemyFire = new SoundId[] {
        SoundId.enemyFire1,
        SoundId.enemyFire2,
        SoundId.enemyFire3,
        SoundId.enemyFire4,
        SoundId.enemyFire5,
    };
    public void enemyFire() {
        playRandom(enemyFire);
    }

    private static SoundId[] shieldHit = new SoundId[] {
        SoundId.shieldHit1,
        SoundId.shieldHit2,
    };
    public void shieldHit() {
        playRandom(shieldHit);
    }
}
