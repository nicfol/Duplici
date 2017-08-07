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

        //Initialize singleton and give it an observer
        pasteListSingleton.init(this);
        pasteListSingleton.addObserver(this);

        //Recycleview and adapter
        rv = (RecyclerView)findViewById(R.id.rv);
        adapter = new RVAdapter(this, pasteListSingleton.getPasteList());
        LinearLayoutManager llm = new LinearLayoutManager(this);

        //Hide RV if there's nothing to show
        updateUIifEmptyList(rv);

        //Assign layout manager to the RV Adapter
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.isInEditMode();

        //Fab Listener
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); //TODO Focus on new card when FAB is pressed
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pasteListSingleton.insertPaste("Paste Label","Text",10); //TODO Replace strings and icon
                updateRV(rv, adapter);
                adapter.notifyItemInserted(pasteListSingleton.getPasteList().size()-1);
            }
        });
    }

    //Disables the cardview and shows noData if there's no pastes
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

    //Updates the RV
    public void updateRV(RecyclerView rv, RVAdapter adapter) { //TODO Collapse into observer update()?
        adapter.notifyItemChanged(pasteListSingleton.getPasteList().size());
        adapter.notifyItemRangeChanged(0, pasteListSingleton.getPasteList().size());

        adapter.notifyDataSetChanged();
        updateUIifEmptyList(rv);
    }

    //Observer for pastListSingleton changes
    @Override
    public void update(Observable o, Object arg) {
        ((PasteListSingleton) o ).getPasteList();
        Log.d("Observer update", String.valueOf(pasteListSingleton.getPasteList().size()));
        updateRV(rv, adapter);
        updateUIifEmptyList(rv);
    }
}