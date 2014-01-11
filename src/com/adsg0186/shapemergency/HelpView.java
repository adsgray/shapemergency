package com.adsg0186.shapemergency;

import com.adsg0186.blobmergency.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class HelpView extends Activity {

    Typeface unispace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("trace", "HelpView onCreate!");

        setContentView(R.layout.activity_help);
        initFont();
        setFontOnText();
    }

    protected void initFont() {
        unispace = Typeface.createFromAsset(getAssets(),"data/unispace.ttf");
    }

    // TODO: make this into a function that can be used by all views...
    protected void setFontOnText() {
        TextView[] textIds = new TextView[] {
                (TextView) findViewById(R.id.help_title),
                (TextView) findViewById(R.id.help_text),
        };

        for (int ct = 0; ct < textIds.length; ct++) {
            textIds[ct].setTypeface(unispace);
        }
    }

}
