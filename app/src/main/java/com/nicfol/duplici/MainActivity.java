package com.nicfol.duplici;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    PasteListSingleton pasteListSingleton = PasteListSingleton.getInstance();

    RecyclerView rv;
    RVAdapter adapter;

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

        pasteListSingleton.init(this);

        pasteListSingleton.addObserver(this);

        rv = (RecyclerView)findViewById(R.id.rv);
        adapter = new RVAdapter(this, pasteListSingleton.getPasteList());

        LinearLayoutManager llm = new LinearLayoutManager(this);

        for(int i = 1; i < adapter.getItemCount(); i++) {
            Paste tmpPaste = (Paste) pasteListSingleton.getPasteList().get(i);
            Log.d("Paste: " + i + " ", String.valueOf(tmpPaste.getLabel()));
        }

        //Hide RV if there's nothing to show
        updateUIifEmptyList(rv);

        //Assign layout manager to the RV Adapter
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.isInEditMode();

        pasteListSingleton.insertPaste("new", String.valueOf(Math.random()), 10);

        //Fab
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            updateRV(rv, adapter);
            }
        });
    }

    public void updateRV(RecyclerView rv, RVAdapter adapter) {
        adapter.notifyItemChanged(pasteListSingleton.getPasteList().size());
        adapter.notifyItemRangeChanged(0, pasteListSingleton.getPasteList().size());

        adapter.notifyDataSetChanged();
        updateUIifEmptyList(rv);
    }

    public void updateRV(RecyclerView rv, RVAdapter adapter, int indexToDelete) {
        if(!pasteListSingleton.getPasteList().isEmpty()) {
            rv.removeViewAt(indexToDelete);
            adapter.notifyItemChanged(indexToDelete);
            adapter.notifyItemRangeChanged(indexToDelete, pasteListSingleton.getPasteList().size());

            adapter.notifyDataSetChanged();
            updateUIifEmptyList(rv);
        }
    }

    private void updateUIifEmptyList(RecyclerView rv) {
        if(pasteListSingleton.getPasteList().isEmpty()) {
            TextView noData = (TextView)findViewById(R.id.noData);
            noData.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            TextView noData = (TextView)findViewById(R.id.noData);
            noData.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ((PasteListSingleton) o ).getPasteList();
        Log.d("Observer update", "change found");
        updateRV(rv, adapter);
    }
}
