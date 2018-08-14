package com.ashwinsreevatsacom.chessclock.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChessClockDbHelper extends SQLiteOpenHelper{

    //name of database file
    private static final String DATABASE_NAME = "archive.db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    public ChessClockDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_SCORE_ENTRIES = "CREATE TABLE "
                + ChessClockContract.ChessClockEntry.TABLE_NAME + " ("
                + ChessClockContract.ChessClockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ChessClockContract.ChessClockEntry.GAME_ID + " INTEGER NOT NULL, " //TODO Make sure this works
                + ChessClockContract.ChessClockEntry.DATE + " TEXT, "
                + ChessClockContract.ChessClockEntry.OPPONENT + " TEXT, "
                + ChessClockContract.ChessClockEntry.PIECE_COLOR + " INTEGER NOT NULL, "
                + ChessClockContract.ChessClockEntry.TIME + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_SCORE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
