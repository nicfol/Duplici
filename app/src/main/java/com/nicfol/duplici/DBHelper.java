package com.nicfol.duplici;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

class DBHelper extends SQLiteOpenHelper {
    //TODO Sanitize all inputs
    //TODO Move logic away from Main thread
    //TODO Store priority
    //TODO Start throwing a lot of exceptions

    static final String DATABASE_NAME = "duplici_pastes.db";
    private static final int DATABASE_VERSION = 1;
    static final String PASTE_TABLE_NAME = "pastes";
    static final String PASTE_COLUMN_ID = "_id";
    static final String PASTE_COLUMN_LABEL = "label";
    static final String PASTE_COLUMN_TEXT = "text";
    static final String PASTE_COLUMN_ICON = "icon";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PASTE_TABLE_NAME + "(" +
                PASTE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                PASTE_COLUMN_LABEL + " TEXT, " +
                PASTE_COLUMN_TEXT + " TEXT, " +
                PASTE_COLUMN_ICON + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(db != null && oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + PASTE_TABLE_NAME);
            onCreate(db);
        }
    }

    protected void insertPasteToDb(String cLabel, String cText, int cIcon) throws Exception { //TODO Add priority to DB and sort getListOfPAste by Prio
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(PASTE_COLUMN_LABEL, cLabel);
            contentValues.put(PASTE_COLUMN_TEXT, cText);
            contentValues.put(PASTE_COLUMN_ICON, cIcon);

            db.insert(PASTE_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to insert to database", e);
        }
    }

    protected boolean updatePasteInDb(Integer id, String cLabel, String cText, int cIcon) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(PASTE_COLUMN_LABEL, cLabel);
            contentValues.put(PASTE_COLUMN_TEXT, cText);
            contentValues.put(PASTE_COLUMN_ICON, cIcon);

            db.update(PASTE_TABLE_NAME, contentValues, PASTE_COLUMN_ID + " = ? ", new String[] {Integer.toString(id)});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected Cursor getPasteFromDb(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + PASTE_TABLE_NAME + " WHERE " + PASTE_COLUMN_ID + "=?",
                new String[] {Integer.toString(id)});
        return res;
    }


    protected List<Paste> getListOfPastesFromDb() {
        List<Paste> pasteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        int rows = getNoOfRows();
        int lastRowNo = getLastInsertID() - rows; //TODO Catch if getLastInsertID returns -1
        for(int i = 1; i < rows + lastRowNo + 1; i++) {
            try {
                Cursor res = getPasteFromDb(i);

                if(res != null &&  res.moveToFirst()) {
                    int dbID = res.getInt(res.getColumnIndex(DBHelper.PASTE_COLUMN_ID));
                    String label = res.getString(res.getColumnIndex(DBHelper.PASTE_COLUMN_LABEL));
                    String text = res.getString(res.getColumnIndex(DBHelper.PASTE_COLUMN_TEXT));
                    int icon = res.getInt(res.getColumnIndex(DBHelper.PASTE_COLUMN_ICON));

                    Paste tempPaste = new Paste(dbID, label, text, icon);
                    pasteList.add(tempPaste);
                    res.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        db.close();
        return pasteList;
    }

    protected Cursor getAllPastesFromDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + PASTE_TABLE_NAME, null);
        return res;
    }

    protected void deletePasteFromDb(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PASTE_TABLE_NAME, PASTE_COLUMN_ID + " =? ",
                new String[]{Long.toString(id)} );
        db.close();
    }

    protected int getNoOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            return (int) DatabaseUtils.queryNumEntries(db, PASTE_TABLE_NAME);
        } catch (Exception e) {
                e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }

    protected int getLastInsertID() { //TODO Catch some edge cases here
        int insertID = -1;
        Cursor res = getAllPastesFromDb();
        if(res != null && res.moveToLast()) {
            insertID = res.getInt(0);
        }

        res.close();
        return insertID;
    }
}
