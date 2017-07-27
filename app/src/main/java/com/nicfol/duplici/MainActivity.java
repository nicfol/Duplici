package com.nicfol.duplici;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    DBHelper db = new DBHelper(this);
    private List<Paste> pasteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FB Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        //Save database to list of pastes
        pasteList = db.getListOfPastes();

        final RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        final RVAdapter adapter = new RVAdapter(this, pasteList);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        for(int i = 1; i < adapter.getItemCount(); i++) {
            Log.d("Paste: ", pasteList.get(i).getLabel());
        }

        //Hide RV if there's nothing to show
        if(pasteList.isEmpty()) {
            TextView noData = (TextView)findViewById(R.id.noData);
            noData.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            TextView noData = (TextView)findViewById(R.id.noData);
            noData.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }

        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        //Fab
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                deletePaste(rv, adapter, 0);

                //TODO Remove entry from DB
            }
        });



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
*/
    }

    public void deletePaste(RecyclerView rv, RVAdapter adapter, int indexToDelete) {
        pasteList.remove(indexToDelete);
        rv.removeViewAt(indexToDelete);
        adapter.notifyItemChanged(indexToDelete);
        adapter.notifyItemRangeChanged(indexToDelete, pasteList.size());

        adapter.notifyDataSetChanged();
    }


    private void updateClip(String label, String text) {
        ClipData clip = ClipData.newPlainText(label, text);
        ClipboardManager clipboard = null;
        clipboard.setPrimaryClip(clip);
        Log.d("Updated Clip:", String.valueOf(clipboard.getPrimaryClipDescription().getLabel()) + " : " + String.valueOf(clipboard.getPrimaryClip().getItemAt(0).getText()));
    }

}
