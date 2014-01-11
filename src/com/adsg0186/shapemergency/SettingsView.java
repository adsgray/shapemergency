package com.adsg0186.shapemergency;

import com.adsg0186.blobmergency.R;
import com.adsg0186.shapemergency.testgame1.config.GamePreferences;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsView extends Activity {

    Typeface unispace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("trace", "SettingsView onCreate!");

        setContentView(R.layout.activity_settings);
        initFont();
        setFontOnText();
        makeLinksClickable();
        populateCheckBoxesFromPreferences();
        setupCheckboxListeners();
    }

    protected void initFont() {
        unispace = Typeface.createFromAsset(getAssets(),"data/unispace.ttf");
    }

    protected void populateCheckBoxesFromPreferences() {
        GamePreferences pref = GamePreferences.get();
        CheckBox cb;
        
        cb = (CheckBox) findViewById(R.id.soundcheckbox);
        cb.setChecked(pref.getSound() == 1);

        cb = (CheckBox) findViewById(R.id.vibratecheckbox);
        cb.setChecked(pref.getVibrate() == 1);
    }
    
    // persiste preferences, make them reality, then show the user a little message.
    protected void savePreferences() {
        GamePreferences.get().save();
        GamePreferences.get().doInitFromPreferences();
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

    protected void setupCheckboxListeners() {
        CheckBox sound = (CheckBox) findViewById(R.id.soundcheckbox);
        sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GamePreferences pref = GamePreferences.get();
                if (isChecked) {
                    pref.setSound(1);
                } else {
                    pref.setSound(0);
                }
                savePreferences();
            }
        });

        CheckBox vibrate = (CheckBox) findViewById(R.id.vibratecheckbox);
        vibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GamePreferences pref = GamePreferences.get();
                if (isChecked) {
                    pref.setVibrate(1);
                } else {
                    pref.setVibrate(0);
                }
                savePreferences();
            }
        });
        
    }

    // TODO: make this into a function that can be used by all views...
    protected void setFontOnText() {
        TextView[] textIds = new TextView[] {
                (CheckBox) findViewById(R.id.soundcheckbox),
                (CheckBox) findViewById(R.id.vibratecheckbox),
                (TextView) findViewById(R.id.settings_title),
                (TextView) findViewById(R.id.about_title),
                (TextView) findViewById(R.id.about_text)
        };

        for (int ct = 0; ct < textIds.length; ct++) {
            textIds[ct].setTypeface(unispace);
        }
    }
    
    protected void makeLinksClickable() {
        TextView about = (TextView) findViewById(R.id.about_text);
        about.setText(Html.fromHtml(getString(R.string.about_text)));
        about.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
