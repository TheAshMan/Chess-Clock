package com.ashwinsreevatsacom.chessclock.Data;

import android.provider.BaseColumns;

/**
 * Defining database details: name, columns
 */
public final class ChessClockContract { //TODO Add details about the time settings for the chess game

    public static abstract class ChessClockEntry implements BaseColumns{
        public static final String TABLE_NAME = "ChessClockArchive";

        public static final String _ID = BaseColumns._ID; //ID for each data input
        public static final String GAME_ID = "gameID"; //ID for each game to differentiate games in table
        public static final String DATE = "date"; //Date of game
        public static final String OPPONENT = "opponent"; //Opponent name
        public static final String PIECE_COLOR = "color"; //Color of time inserted
        public static final String TIME = "time"; //Time when button is pressed

        public static final boolean PIECE_COLOR_BLACK = false;
        public static final boolean PIECE_COLOR_WHITE = true;
    }
}
