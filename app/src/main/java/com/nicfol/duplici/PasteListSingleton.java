package com.nicfol.duplici;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


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

    protected static void insertPaste(String cLabel, String cText, int cIcon) {
        db = new DBHelper(appContext);
        db.insertPaste(cLabel, cText, cIcon);

        int dbID = db.getLastInsertID();
        Paste tempPaste = new Paste(dbID, cLabel, cText, cIcon);
        pasteList.add(tempPaste);

        db.close();
    }

    protected static void deletePaste(int dbID) {
        db = new DBHelper(appContext);
        db.deletePaste(dbID);
        db.close();
    }

    protected static void updatePaste(int dbID, String cLabel, String cText, int cIcon) {
        db = new DBHelper(appContext);
        db.updatePaste(dbID, cLabel, cText, cIcon);
        db.close();
    }

    protected List getPasteList() {
        return pasteList;
    }

}