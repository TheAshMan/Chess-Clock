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
import android.util.Log;

import com.ashwinsreevatsacom.chessclock.Data.ChessClockContract.ChessClockEntry;

import static android.icu.text.UnicodeSet.CASE;
import static com.ashwinsreevatsacom.chessclock.Data.ChessClockDbHelper.LOG_TAG;

public class ChessClockProvider extends ContentProvider {


    //Database helper object
    private ChessClockDbHelper mDbHelper;

    //URI matcher
    private static final int CHESS_ARCHIVE = 100; //TODO insert a third int for CHESS_GAME
    private static final int CHESS_ID = 102;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ChessClockContract.CONTENT_AUTHORITY, ChessClockContract.PATH_CHESS_CLOCK_TIMES, CHESS_ARCHIVE);
        sUriMatcher.addURI(ChessClockContract.CONTENT_AUTHORITY, ChessClockContract.PATH_CHESS_CLOCK_TIMES + "/#", CHESS_ID);
    }


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
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CHESS_ARCHIVE:
                return ChessClockEntry.CONTENT_LIST_TYPE;
            case CHESS_ID:
                return ChessClockEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI" + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case CHESS_ARCHIVE:
                return insertChessTime(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion not supported for " + uri);
        }
    }

    private Uri insertChessTime(Uri uri, ContentValues values){
        //TODO sanity checks
        Integer game_ID = values.getAsInteger(ChessClockEntry.GAME_ID);
        Boolean pieceColor = values.getAsBoolean(ChessClockEntry.PIECE_COLOR);
        String time = values.getAsString(ChessClockEntry.TIME);

        if(pieceColor == null){
            throw new IllegalArgumentException("Chess Time requires a color");
        }
        if (time == null) {
            throw new IllegalArgumentException("Chess Time requires a time");
        }
        if (game_ID == null) {
            throw new IllegalArgumentException("Chess Time requires a game id");
        }

        //Insert data into database using ContentUris
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(ChessClockEntry.TABLE_NAME,null,values);

        if(id == -1){
            Log.v(LOG_TAG, "Failed to insert score for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(ChessClockEntry.CONTENT_URI,id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHESS_ARCHIVE:
                // Delete all rows that match the selection and selection args
                return database.delete(ChessClockEntry.TABLE_NAME, selection, selectionArgs);
            case CHESS_ID:
                // Delete a single row given by the ID in the URI
                selection = ChessClockEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(ChessClockEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CHESS_ARCHIVE:
                return updateChessTime(uri, contentValues, selection, selectionArgs);
            case CHESS_ID:
                selection = ChessClockEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateChessTime(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);

        }
    }

    private int updateChessTime(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //sanity checks
        if(values.containsKey(ChessClockEntry.PIECE_COLOR)) {
            Boolean pieceColor = values.getAsBoolean(ChessClockEntry.PIECE_COLOR);
            if (pieceColor == null) {
                throw new IllegalArgumentException("Chess Time requires a color");
            }
        }

        if(values.containsKey(ChessClockEntry.TIME)) {
            String time = values.getAsString(ChessClockEntry.TIME);
            if (time == null) {
                throw new IllegalArgumentException("Chess Time requires a time");
            }
        }

        if(values.containsKey(ChessClockEntry.GAME_ID)) {
            Integer game_ID = values.getAsInteger(ChessClockEntry.GAME_ID);
            if (game_ID == null) {
                throw new IllegalArgumentException("Chess Time requires a game id");
            }
        }

        //Logic
        if(values.size()==0){
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(ChessClockEntry.TABLE_NAME,values, selection, selectionArgs);
    }
}
