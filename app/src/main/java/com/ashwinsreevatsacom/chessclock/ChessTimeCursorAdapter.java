package com.ashwinsreevatsacom.chessclock;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ashwinsreevatsacom.chessclock.Data.ChessClockContract.ChessClockEntry;

public class ChessTimeCursorAdapter extends CursorAdapter {

    public ChessTimeCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.archive_list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);

        int opponentColumnIndex = cursor.getInt(cursor.getColumnIndexOrThrow(ChessClockEntry.OPPONENT));
        int dateColumnIndex = cursor.getInt(cursor.getColumnIndexOrThrow(ChessClockEntry.DATE));

        String chessTimeOpponent = cursor.getString(opponentColumnIndex);
        String chessTimeDate = cursor.getString(dateColumnIndex);

        tvBody.setText(chessTimeOpponent);
        tvPriority.setText(String.valueOf(chessTimeDate));
    }
}
