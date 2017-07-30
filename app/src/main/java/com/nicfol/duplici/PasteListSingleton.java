package com.nicfol.duplici;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


class PasteListSingleton extends Observable {
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
            Log.d("","");
        }
    }

    protected void insertPaste(String cLabel, String cText, int cIcon) {
        //Add new paste to DB
        db = new DBHelper(appContext);
        db.insertPaste(cLabel, cText, cIcon);

        //Add new paste to memory list
        Paste tempPaste = new Paste(db.getLastInsertID()+1, cLabel, cText, cIcon);
        pasteList.add(tempPaste);

        notifyChanges();
        db.close();
    }

    protected void deletePaste(int dbID) {
        for(int i = 0; i <= pasteList.size()-1; i++) {
            Log.d("pastelist for counter","" + i );
            if(pasteList.get(i).getDbID() == dbID) {
                db = new DBHelper(appContext);
                db.deletePaste(dbID);
                db.close();
                pasteList.remove(i);
                notifyChanges();
            }
        }
    }

    protected void updatePaste(int dbID, String cLabel, String cText, int cIcon) {
        db = new DBHelper(appContext);
        db.updatePaste(dbID, cLabel, cText, cIcon);
        db.close();
        notifyChanges();
    }

    protected List getPasteList() {
        return pasteList;
    }

    protected int getLastInsertId() {
        db = new DBHelper(appContext);
        return db.getLastInsertID();
    }

    public Paste getPaste(int i) {
        return pasteList.get(i);
    }

    public void notifyChanges() {
        setChanged();
        notifyObservers(pasteList);
    }
}