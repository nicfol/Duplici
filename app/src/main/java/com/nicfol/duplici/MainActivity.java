package com.nicfol.duplici;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ClipboardManager clipboard;

    DBHelper db = new DBHelper(this);
    private List<Paste> pasteList = new ArrayList<>();

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


        pasteList = db.getPasteList();

        updateClip("Initial clip update: ", "Success");


        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        RVAdapter adapter = new RVAdapter(this, pasteList);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        TextView noData = (TextView)findViewById(R.id.noData);

        for(int i = 1; i < adapter.getItemCount(); i++) {
            Log.d("Paste: ", pasteList.get(i).getLabel());
        }


        if(pasteList.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            noData.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }

        rv.setLayoutManager(llm);
        rv.hasFixedSize();
        rv.setAdapter(adapter);



/*
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
*/
    }


    private void updateClip(String label, String text) {
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Log.d("Updated Clip:", String.valueOf(clipboard.getPrimaryClipDescription().getLabel()) + " : " + String.valueOf(clipboard.getPrimaryClip().getItemAt(0).getText()));
    }

    public void fabClick(View view) {

    }
}
