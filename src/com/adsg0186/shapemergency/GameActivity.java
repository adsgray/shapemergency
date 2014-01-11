package com.adsg0186.shapemergency;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.github.adsgray.gdxtry1.engine.util.GameCommand;

public class GameActivity extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        Log.d("trace", "GameActivity onCreate!");
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        int difficultyLevel =  extras.getInt("DIFFICULTY_LEVEL");
        // int resumeGame = extras.getInt("RESUME_GAME");
		initialize(new GameScreen(context, new ExitGame(), difficultyLevel), false);		// initialize a new instance of your Game class
    }
    
    private class ExitGame implements GameCommand {
        @Override
        public void execute(int arg) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("score",arg);
            setResult(RESULT_OK,returnIntent);     
            GameActivity.this.finish();
        }
    }

}
