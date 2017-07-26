package com.nicfol.duplici;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "duplici_pastes.db";
    private static final int DATABASE_VERSION = 1;
    public static final String PASTE_TABLE_NAME = "pastes";
    public static final String PASTE_COLUMN_ID = "_id";
    public static final String PASTE_COLUMN_LABEL = "label";
    public static final String PASTE_COLUMN_TEXT = "text";
    public static final String PASTE_COLUMN_ICON = "icon";

    public DBHelper(Context context)

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

    }
}
