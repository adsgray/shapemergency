package com.adsg0186.shapemergency.testgame1;

import android.content.Context;
import android.os.Vibrator;

public class Vibrate {

    public static interface VibrateIF {
        public void vibrate(long ms);
    }
    
    private static class realVibrate implements VibrateIF {
        private Vibrator vibrator;

        public realVibrate(Context context) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        @Override public void vibrate(long ms) {
            vibrator.vibrate(ms);
        }
    }

    private static class fakeVibrate implements VibrateIF {
        @Override public void vibrate(long ms) { }
    }

    private static VibrateIF realInstance;
    private static VibrateIF fakeInstance;
    private static VibrateIF instance;
    
    public static VibrateIF setRealInstance(Context context) {
        if (realInstance == null) {
            realInstance = new realVibrate(context);
        }
        instance = realInstance;
        return instance;
    }
    public static VibrateIF setFakeInstance() {
        if (fakeInstance == null) {
            fakeInstance = new fakeVibrate();
        }
        instance = fakeInstance;
        return instance;
    }
    public static VibrateIF get() { return instance; }
}
