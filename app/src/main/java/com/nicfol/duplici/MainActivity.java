package com.nicfol.duplici;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
        //pasteList = db.getListOfPastes();

        PasteListSingleton PS = PasteListSingleton.getInstance();
        PS.init(this);
        pasteList = PS.getPasteList();

        final RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        final RVAdapter adapter = new RVAdapter(this, pasteList);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        for(int i = 1; i < adapter.getItemCount(); i++) {
            Log.d("Paste: ", pasteList.get(i).getLabel());
        }

        //Hide RV if there's nothing to show
        updateUIifEmptyList(rv);

        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        //final MaterialDialog.Builder dialog = new MaterialDialog.Builder(this)
          //      .title(R.string.modifyPasteTitle)
            //      .content("dothislater")//TODO add content to dialog
              //    .positiveText(R.string.modifyPasteAccept)
                //  .negativeText(R.string.modifyPasteDiscard);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.fragment_dialog);
        dialog.setTitle("Title Goes rigt here!");

        TextView text = (TextView)dialog.findViewById(R.id.textView3);
        TextView text2 = (TextView)dialog.findViewById(R.id.textView4);

        ImageButton button = (ImageButton)dialog.findViewById(R.id.imageButton2);

        Button dialogButton = (Button)dialog.findViewById(R.id.button);
        Button dialogButton2 = (Button)dialog.findViewById(R.id.button2);

        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Fab
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            dialog.show();



            deletePaste(rv, adapter, 0);
            }
        });



    }

    public void deletePaste(RecyclerView rv, RVAdapter adapter, int indexToDelete) {
        if(!pasteList.isEmpty()) {
            pasteList.remove(indexToDelete);
            rv.removeViewAt(indexToDelete);
            adapter.notifyItemChanged(indexToDelete);
            adapter.notifyItemRangeChanged(indexToDelete, pasteList.size());

            adapter.notifyDataSetChanged();
            updateUIifEmptyList(rv);
        }
    }

    private void updateUIifEmptyList(RecyclerView rv) {
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

}
