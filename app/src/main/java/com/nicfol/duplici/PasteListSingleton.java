package com.nicfol.duplici;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class PasteListSingleton extends Observable {
    //TODO Add check for initialization
    //TODO Throw errors

    private static final PasteListSingleton thisInstance = new PasteListSingleton();

    static PasteListSingleton getInstance() {
        return thisInstance;
    }

    private PasteListSingleton() { }

    private static Context appContext;
    private static List<Paste> pasteList = new ArrayList<>();
    private static DBHelper db;


    protected void init(Context context) {
        //Initialize to get context for the DBHelper and load data from DB to memory
        if(context != null && appContext == null){
            appContext = context;

            db = new DBHelper(appContext);
            pasteList = db.getListOfPastesFromDb();
        } else {
            //TODO fail gracefully
        }
    }

    protected void insertPaste(String cLabel, String cText, int cIcon) {
        //Add new paste to DB
        db = new DBHelper(appContext);

        try {
            db.insertPasteToDb(cLabel, cText, cIcon);
        } catch (Exception e) { //TODO fail gracefully
            e.printStackTrace();
            Log.e("PasteListSingleton","Failed to insert to DB");
        }

        //Add new paste to memory list
        pasteList.add(new Paste(db.getLastInsertID()+1, cLabel, cText, cIcon));

        notifyChanges();
        db.close();
    }

    protected void deletePaste(int dbID) {
        int listIndex = findPasteByDBid(dbID);

        if(listIndex != -1) {
            //Delete the paste from the DB
            db = new DBHelper(appContext);
            db.deletePasteFromDb(dbID);
            db.close();

            //Delete from memory
            pasteList.remove(listIndex);
            notifyChanges();
        } else {
            Log.e("PasteListSingleton", "deletePaste received -1 from findPasteById");
            //TODO Throw error
            //TODO fail gracefully
        }
    }

    protected int findPasteByDBid(int dbID) {
        for(int i = 0; i <= pasteList.size()-1; i++) {
            if(pasteList.get(i).getDbID() == dbID) {
                return i;
            }
        }
        return -1; //TODO fail gracefully
    }

    protected void updatePaste(int position, int dbID, String cLabel, String cText, int cIcon) {
        //Update DB
        db = new DBHelper(appContext);
        db.updatePasteInDb(dbID, cLabel, cText, cIcon);
        db.close();

        //Update memory
        pasteList.set(position, new Paste(dbID, cLabel, cText, cIcon));

        notifyChanges();
    }

    public List getPasteList() {
        return pasteList;
    }

    public Paste getPaste(int i) {
        if(pasteList.size() > 0 || pasteList.size() < i) {
            return pasteList.get(i);
        } else {
            return new Paste(-1, "ERROR", "ERROR", -1); //TODO Fix this
        }
    }

    private void notifyChanges() {
        setChanged();
        notifyObservers(pasteList);
    }
}