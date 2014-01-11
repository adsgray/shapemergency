package com.adsg0186.shapemergency;

import com.adsg0186.blobmergency.R;
import com.adsg0186.shapemergency.testgame1.GameSound;
import com.adsg0186.shapemergency.testgame1.config.GamePreferences;
import com.adsg0186.shapemergency.testgame1.config.SavedGame;
import com.github.adsgray.gdxtry1.engine.util.LocalHighScore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
//import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    protected int difficultyLevel = 1;
    protected final static int START_GAME = 1;
    protected Typeface unispace;
    View optionalResumeButton;

    private void startGame() {
        Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
        myIntent.putExtra("DIFFICULTY_LEVEL", difficultyLevel);
        MainActivity.this.startActivityForResult(myIntent, START_GAME);       
    }

    private OnClickListener playButtonListener = new OnClickListener() {
        @Override public void onClick(View arg0) {
            Log.d("trace", "play button tapped");
            SavedGame.get().clearSavedGame();
            // remember this choice FOREVER
            GamePreferences.get().setDifficulty(difficultyLevel);
            GamePreferences.get().save();
            startGame();
        }
    };

    private OnClickListener resumeButtonListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Log.d("trace", "resume button tapped");
            startGame();
        }
    };
 
    private OnClickListener highScoreButtonListener = new OnClickListener() {
        @Override public void onClick(View arg0) {
            Log.d("trace", "high score button tapped");
            Intent myIntent = new Intent(MainActivity.this, HighScoreView.class);
            MainActivity.this.startActivity(myIntent);
        }
    };
  
    private OnClickListener settingsButtonListener = new OnClickListener() {
        @Override public void onClick(View arg0) {
            Log.d("trace", "high score button tapped");
            Intent myIntent = new Intent(MainActivity.this, SettingsView.class);
            MainActivity.this.startActivity(myIntent);
        }
    };
   
    private OnClickListener helpButtonListener = new OnClickListener() {
        @Override public void onClick(View arg0) {
            Log.d("trace", "help button tapped");
            Intent myIntent = new Intent(MainActivity.this, HelpView.class);
            MainActivity.this.startActivity(myIntent);
        }
    };
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case START_GAME:
                if (resultCode == RESULT_OK) {
                    int score = data.getIntExtra("score", 0);
                    // Show the score
                    Toast.makeText(getApplicationContext(), String.format("Score: %d", score), Toast.LENGTH_LONG).show();
                } 
            break;
        }
    }


    protected void setFontOnText() {
        TextView[] textIds = new TextView[] {
                (TextView) findViewById(R.id.instructions),
                (RadioButton) findViewById(R.id.difficulty_easy),
                (RadioButton) findViewById(R.id.difficulty_normal),
                (RadioButton) findViewById(R.id.difficulty_hard),
                (Button) findViewById(R.id.play_button),
                (Button) findViewById(R.id.high_score_button),
                (Button) findViewById(R.id.settings_button),
                (Button) findViewById(R.id.help_button)
        };

        unispace = Typeface.createFromAsset(getAssets(),"data/unispace.ttf");
        
        for (int ct = 0; ct < textIds.length; ct++) {
            textIds[ct].setTypeface(unispace);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("trace", "onCreate!");
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        LocalHighScore.createInstance(context);
        GamePreferences.createInstance(context).load();
        SavedGame.createInstance(context).load();
        GamePreferences.get().doInitFromPreferences();
        difficultyLevel = GamePreferences.get().getDifficulty();

        setContentView(R.layout.activity_main);

        // must do these after setContentView
        setDifficultyRadioButton(GamePreferences.get().getDifficulty());
        setFontOnText();
        
        Button playbutton = (Button)findViewById(R.id.play_button);
        playbutton.setOnClickListener(playButtonListener);
        Button highScorebutton = (Button)findViewById(R.id.high_score_button);
        highScorebutton.setOnClickListener(highScoreButtonListener);
        Button settingsbutton = (Button)findViewById(R.id.settings_button);
        settingsbutton.setOnClickListener(settingsButtonListener);
        Button helpButton = (Button)findViewById(R.id.help_button);
        helpButton.setOnClickListener(helpButtonListener);
        
        if (SavedGame.get().getSavedGamePresent()) {
            createOptionalResumeButton();
        }
    }

    protected void createOptionalResumeButton() {
        Button resumeButton = new Button(this);

        // clone properties of play_button
        Button playButton = (Button) findViewById(R.id.play_button);
        resumeButton.setPadding(playButton.getPaddingLeft(), 
                playButton.getPaddingTop(), 
                playButton.getPaddingRight(), 
                playButton.getPaddingBottom());

        resumeButton.setText(R.string.resume_button);
        resumeButton.setTypeface(unispace);

        // put it to the right of play_button
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(playButton.getLayoutParams());
        lp.addRule(RelativeLayout.RIGHT_OF, R.id.play_button);
        resumeButton.setLayoutParams(lp);

        // add it to the game_buttons RelativeLayout chunk
        RelativeLayout gamebuttons = (RelativeLayout) findViewById(R.id.game_buttons);
        gamebuttons.addView(resumeButton);

        resumeButton.setOnClickListener(resumeButtonListener);
        this.optionalResumeButton = resumeButton;
    }
    
    @Override
    public void onResume() {
        Log.d("trace", "mainactivity onResume");
        super.onResume();
        if (optionalResumeButton != null && !SavedGame.get().getSavedGamePresent()) {
            RelativeLayout gamebuttons = (RelativeLayout) findViewById(R.id.game_buttons);
            gamebuttons.removeView(optionalResumeButton);
            optionalResumeButton = null;
        } else if (optionalResumeButton == null && SavedGame.get().getSavedGamePresent()) {
            createOptionalResumeButton();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void setDifficultyRadioButton(int choice) {
        // first turn them all off
        int[] ids = new int[] {
                R.id.difficulty_easy,
                R.id.difficulty_normal,
                R.id.difficulty_hard,
        };
        
        for (int ct = 0; ct < ids.length; ct++) {
            RadioButton b = (RadioButton) findViewById(ids[ct]);
            b.setChecked(false);
        }
        
        // then enable the one we want
        // the ids match up with the choice...
        RadioButton b = (RadioButton) findViewById(ids[choice]);
        b.setChecked(true);
    }
    
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        
        switch (view.getId()) {
            case R.id.difficulty_easy:
                if (checked) {
                    difficultyLevel = 0;
                }
                break;
            case R.id.difficulty_normal:
                if (checked) {
                    difficultyLevel = 1;
                }
                break;
            case R.id.difficulty_hard:
                if (checked) {
                    difficultyLevel = 2;
                }
                break;
        }
    }
    
}
