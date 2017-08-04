package com.nicfol.duplici;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


class PasteListSingleton extends Observable {
    private static final PasteListSingleton thisInstance = new PasteListSingleton();

    static PasteListSingleton getInstance() {
        return thisInstance;
    }

    private PasteListSingleton() { }



    private static Context appContext;
    private static List<Paste> pasteList = new ArrayList<>();
    static DBHelper db;


    protected void init(Context context) {
        if(context != null){
            appContext = context;
            db = new DBHelper(appContext);
            pasteList = db.getListOfPastesFromDb();
            Log.d("","");
        }
    }

    protected void insertPaste(String cLabel, String cText, int cIcon) {
        //Add new paste to DB
        db = new DBHelper(appContext);
        db.insertPasteToDb(cLabel, cText, cIcon);

        //Add new paste to memory list
        pasteList.add(new Paste(db.getLastInsertID()+1, cLabel, cText, cIcon));

        notifyChanges();
        db.close();
    }

    protected void deletePaste(int dbID) {
        int listIndex = findPasteByDBid(dbID);

        db = new DBHelper(appContext);
        db.deletePasteFromDb(dbID);
        db.close();

        pasteList.remove(listIndex);
        notifyChanges();
    }

    protected int findPasteByDBid(int dbID) {
        for(int i = 0; i <= pasteList.size()-1; i++) {
            if(pasteList.get(i).getDbID() == dbID) {
                return i;
            }
        }
        return -1;
    }

    protected void updatePaste(int position, int dbID, String cLabel, String cText, int cIcon) {
        db = new DBHelper(appContext);
        db.updatePasteInDb(dbID, cLabel, cText, cIcon);
        db.close();

        pasteList.set(position, new Paste(dbID, cLabel, cText, cIcon));

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