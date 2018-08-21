package com.ashwinsreevatsacom.chessclock;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.ashwinsreevatsacom.chessclock.Data.ChessClockContract.ChessClockEntry;
import com.ashwinsreevatsacom.chessclock.Model.TimeRecord;

public class Archive extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private TextView ArchiveTextWhite;
    private TextView ArchiveTextBlack;
    private List<TimeRecord> timeRecordList;


    private static final int CHESS_TIME_LOADER = 0;

    ChessTimeCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        ListView ArchiveListView = (ListView) findViewById(R.id.List);
        mCursorAdapter = new ChessTimeCursorAdapter(this, null,false); //TODO why is this false?
        ArchiveListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(CHESS_TIME_LOADER,null,this);

    }


    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ChessClockEntry.OPPONENT,
                ChessClockEntry._ID,
                ChessClockEntry.DATE
        };

        return new CursorLoader(this,
                ChessClockEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
