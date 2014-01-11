package com.adsg0186.shapemergency.testgame1.config;

import com.github.adsgray.gdxtry1.engine.position.BlobPosition;
import com.github.adsgray.gdxtry1.engine.position.PositionIF;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SavedGame  {

    protected Context context;
    protected SharedPreferences store;
    protected boolean savedGamePresent; // set to false as soon as a game completes
    protected GameState state;
    
    private static String createKey(String pref) {
        return String.format("savedgame_%s", pref);
    }

    // C struct
    public static class GameState {
        public int hitPoints;
        public int shields;
        public int bossesKilled;
        public int difficulty;
        public int score;
        public PositionIF defenderPos;
    }
    
    public SavedGame(Context context) { 
        this.context = context;
        store = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SavedGame setGameState(GameState state) {
        this.state = state;
        savedGamePresent = true;
        return this;
    }

    public boolean getSavedGamePresent() { return savedGamePresent; }

    public void clearSavedGame() {
        SharedPreferences.Editor editor = store.edit();
        savedGamePresent = false;
        editor.putInt(createKey("gamepresent"), 0);
        editor.commit();
    }
    
    public GameState getState() {
        return state;
    }

    // call before getting
    public GameState load() {
        savedGamePresent = store.getInt(createKey("gamepresent"), 0) == 1;

        if (!savedGamePresent) {
            return null;
        }

        if (state == null) {
            state = new GameState();
        }

        state.hitPoints = store.getInt(createKey("hitPoints"), 0);
        state.bossesKilled = store.getInt(createKey("bossesKilled"), 0);
        state.shields = store.getInt(createKey("shields"), 0);
        state.difficulty = store.getInt(createKey("difficulty"), 0);
        state.score = store.getInt(createKey("score"), 0);
        state.defenderPos = new BlobPosition(store.getInt(createKey("xpos"), 400), store.getInt(createKey("ypos"), 100)); 

        return state;
    }
    
    
    // call after setting
    public void save() {
        if (state == null) return;

        SharedPreferences.Editor editor = store.edit();
        editor.putInt(createKey("hitPoints"), state.hitPoints);
        editor.putInt(createKey("shields"), state.shields);
        editor.putInt(createKey("bossesKilled"), state.bossesKilled);
        editor.putInt(createKey("difficulty"), state.difficulty);
        editor.putInt(createKey("score"), state.score);
        editor.putInt(createKey("xpos"), state.defenderPos.getX());
        editor.putInt(createKey("ypos"), state.defenderPos.getY());
        editor.putInt(createKey("gamepresent"), 1);
        editor.commit();
    }
   
    protected static SavedGame instance;
    public static SavedGame createInstance(Context context) {
        instance = new SavedGame(context);
        return instance;
    }
    public static SavedGame get() { return instance; }
}
