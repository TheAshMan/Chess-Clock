package com.ashwinsreevatsacom.chessclock.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defining database details: name, columns
 */
public final class ChessClockContract { //TODO Add details about the time settings for the chess game

    public static final String CONTENT_AUTHORITY = "com.ashwinsreevatsacom.chessclock";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CHESS_CLOCK_TIMES = "ChessClockArchive";


    public static abstract class ChessClockEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CHESS_CLOCK_TIMES);

        public static final String TABLE_NAME = "ChessClockArchive";

        public static final String _ID = BaseColumns._ID; //ID for each data input
        public static final String GAME_ID = "gameID"; //ID for each game to differentiate games in table
        public static final String DATE = "date"; //Date of game
        public static final String OPPONENT = "opponent"; //Opponent name
        public static final String PIECE_COLOR = "color"; //Color of time inserted
        public static final String TIME = "time"; //Time when button is pressed

        public static final boolean PIECE_COLOR_BLACK = false;
        public static final boolean PIECE_COLOR_WHITE = true;


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHESS_CLOCK_TIMES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHESS_CLOCK_TIMES;
    }
}
