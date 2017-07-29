package com.nicfol.duplici;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolai on 29-07-2017.
 */

class PasteListSingleton {
    private static final PasteListSingleton ourInstance = new PasteListSingleton();

    static PasteListSingleton getInstance() {
        return ourInstance;
    }

    private PasteListSingleton() { }



    private static Context appContext;
    private static List<Paste> pasteList = new ArrayList<>();
    static DBHelper db;


    protected void init(Context context) {
        if(context != null){
            appContext = context;
            db = new DBHelper(appContext);
            pasteList = db.getListOfPastes();
        }
    }

    protected static void insertData(String cLabel, String cText, int cIcon) {
        Paste tempPaste = new Paste(cLabel, cText, cIcon);
        pasteList.add(tempPaste);

        db = new DBHelper(appContext);
        db.insertPaste(cLabel, cText, cIcon);
        db.close();
    }



    protected List getPasteList() {
        return pasteList;
    }

}
