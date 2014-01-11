package com.adsg0186.shapemergency.testgame1.config;

import java.util.EnumMap;

import com.adsg0186.shapemergency.testgame1.config.GameConfigIF.Difficulty;

public class GameConfig {
    // exhausting to type this
    protected static EnumMap<GameConfigIF.Difficulty, GameConfigIF> configs = 
            new EnumMap<Difficulty, GameConfigIF>(GameConfigIF.Difficulty.class);
    
    protected static GameConfigIF instance;
    
    public static void set(GameConfigIF.Difficulty which) {
        instance = getConfig(which);
    }
    
    public static GameConfigIF get() { return instance; }

    protected static GameConfigIF getConfig(GameConfigIF.Difficulty which) {
        GameConfigIF config = configs.get(which);
        // must clobber this and create a new one every time getConfig is called.
        // otherwise on resume the old gameconfig instance is hanging around
        // and contains references to previous world/renderer
        // TODO: add destroyInstance() method which can be called
        // in game.stop() or something.
        config = null;
        if (config == null) {
            /// init config and put it in the map
            switch (which) {
                case easy:
                    config = new EasyGameConfig();
                    break;
                case insane:
                    config = new InsaneGameConfig();
                    break;
                case normal:
                default:
                    config = new BaseGameConfig();
                    break;
            }
           configs.put(which, config);
        }
        return config;
    }
}

