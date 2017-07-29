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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Paste> pasteList = new ArrayList<>();
    PasteListSingleton pasteListSingleton = PasteListSingleton.getInstance();

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
        updatePasteList();

        final RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        final RVAdapter adapter = new RVAdapter(this, pasteList);
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

        //Fab
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //deletePasteInRV(rv, adapter, 0);
                //pasteListSingleton.deletePaste(0);
            }
        });
    }

    public void deletePasteInRV(RecyclerView rv, RVAdapter adapter, int indexToDelete) {
        updatePasteList();
        if(!pasteList.isEmpty()) {
            rv.removeViewAt(indexToDelete);
            adapter.notifyItemChanged(indexToDelete);
            adapter.notifyItemRangeChanged(indexToDelete, pasteListSingleton.getPasteList().size());

            adapter.notifyDataSetChanged();
            updateUIifEmptyList(rv);
        }
    }

    private void updateUIifEmptyList(RecyclerView rv) {
        updatePasteList();
        if(pasteList.isEmpty()) {
            TextView noData = (TextView)findViewById(R.id.noData);
            noData.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            TextView noData = (TextView)findViewById(R.id.noData);
            noData.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    public void updatePasteList() {
        pasteList = pasteListSingleton.getPasteList();
    }
}
