package com.nicfol.duplici;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    private ClipboardManager clipboard;

    DBHelper db = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        Log.d("Check DB insertion", String.valueOf(db.insertPaste("savedLabel233", "sa3vedText2", "ic23o")));
        updateClip("Initial clip update: ", "Success");


        final Button updateClipboard = (Button) findViewById(R.id.updateClip);
        updateClipboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText clipLabel = (EditText) findViewById(R.id.label);
                EditText clipText = (EditText) findViewById(R.id.clip);

                if(clipLabel != null && clipText != null) {
                    try {
                        String savedLabel = clipLabel.getText().toString();
                        String savedText = clipText.getText().toString();

                        db.insertPaste(savedLabel, savedText, "ico");

                        updateClip(savedLabel, savedText);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        final Button btn2 = (Button) findViewById(R.id.getPrefs);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Log.d("", "?!?!?!?");

                Cursor dbCursor = db.getPaste(1);
                dbCursor.moveToFirst();
                Log.d("", String.valueOf(dbCursor));
                String label = dbCursor.getString(dbCursor.getColumnIndex(DBHelper.PASTE_COLUMN_LABEL));
                String text = dbCursor.getString(dbCursor.getColumnIndex(DBHelper.PASTE_COLUMN_TEXT));
                Log.d(label, text);

            }
        });

    }

    private void updateClip(String label, String text) {
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Log.d("Updated Clip:", String.valueOf(clipboard.getPrimaryClipDescription().getLabel()) + " : " + String.valueOf(clipboard.getPrimaryClip().getItemAt(0).getText()));
    }
}
