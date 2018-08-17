package com.ashwinsreevatsacom.chessclock.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ashwinsreevatsacom.chessclock.Data.ChessClockContract.ChessClockEntry;

public class ChessClockProvider extends ContentProvider {

    public static final String LOG_TAG = ChessClockProvider.class.getSimpleName();

    //URI matcher
    private static final int CHESS_ARCHIVE = 100; //TODO insert a third int for CHESS_GAME
    private static final int CHESS_ID = 102;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ChessClockContract.CONTENT_AUTHORITY, ChessClockContract.PATH_CHESS_CLOCK_TIMES, CHESS_ARCHIVE);
        sUriMatcher.addURI(ChessClockContract.CONTENT_AUTHORITY, ChessClockContract.PATH_CHESS_CLOCK_TIMES + "/#", CHESS_ID);
    }

    //Database helper object
    private ChessClockDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ChessClockDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match){ //TODO add case for CHESS_GAME
            case CHESS_ARCHIVE:
                cursor = database.query(ChessClockEntry.TABLE_NAME, projection,selection,
                        selectionArgs,null, null, sortOrder);
                break;
            case CHESS_ID:
                selection = ChessClockEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ChessClockEntry.TABLE_NAME, projection, selection,
                        selectionArgs,null,null,sortOrder);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
