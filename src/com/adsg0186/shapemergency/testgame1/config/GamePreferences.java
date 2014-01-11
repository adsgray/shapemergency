package com.adsg0186.shapemergency.testgame1.config;

import com.adsg0186.shapemergency.testgame1.GameSound;
import com.adsg0186.shapemergency.testgame1.Vibrate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GamePreferences {

    protected int sound;
    protected int vibrate;
    protected int difficulty;
    protected Context context;
    protected SharedPreferences store;
    
    public GamePreferences(Context context) { 
        this.context = context;
        store = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public int setSound(int sound) { this.sound = sound; return sound; }
    public int setVibrate(int vib) { this.vibrate = vib; return vib; }
    public int setDifficulty(int dif) { this.difficulty = dif; return dif; }
    
    private static String createKey(String pref) {
        return String.format("pref_%s", pref);
    }

    // call before getting
    public void load() {
        sound = store.getInt(createKey("sound"), 1);
        vibrate = store.getInt(createKey("vibrate"), 1);
        difficulty = store.getInt(createKey("difficulty"), 1); // default to Normal
    }
    
    public int getSound() { return sound; }
    public int getVibrate() { return vibrate; }
    public int getDifficulty() { return difficulty; }
    
    // call after setting
    public void save() {
        SharedPreferences.Editor editor = store.edit();
        editor.putInt(createKey("sound"), sound);
        editor.putInt(createKey("vibrate"), vibrate);
        editor.putInt(createKey("difficulty"), difficulty);
        editor.commit();
    }
   
    // set up singletons based on preferences
    public void doInitFromPreferences() {
        switch(sound) {
            case 0:
                GameSound.setFakeInstance();
                break;
            case 1:
                GameSound.setRealInstance(context);
                break;
        }

        switch(vibrate) {
            case 0:
                Vibrate.setFakeInstance();
                break;
            case 1:
                Vibrate.setRealInstance(context);
                break;
        }
    }
         
    protected static GamePreferences instance;
    public static GamePreferences createInstance(Context context) {
        instance = new GamePreferences(context);
        return instance;
    }
    public static GamePreferences get() { return instance; }
}
