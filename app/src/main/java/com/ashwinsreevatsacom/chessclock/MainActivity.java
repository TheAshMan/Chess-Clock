package com.ashwinsreevatsacom.chessclock;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ashwinsreevatsacom.chessclock.Data.ChessClockContract;
import com.ashwinsreevatsacom.chessclock.Data.ChessClockContract.ChessClockEntry;
import com.ashwinsreevatsacom.chessclock.Data.ChessClockDbHelper;

public class MainActivity extends AppCompatActivity {

    private Button bottomButton;
    private Button topButton;
    private myChessClock bottomClock;
    private myChessClock topClock;

    private Button pauseButton;
    private Button settingsButton;
    private Button resetButton;
    private Button archiveButton;

    private Button insertDummyDataButton; //TODO Delete once database has been set up
    private ChessClockDbHelper mDbHelper;

    private final int REQUEST_CODE = 2;

    private static final String PREFS_NAME = "myPrefsFile";
    private SharedPreferences myPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        DatabaseHandler db = new DatabaseHandler(this);





        //Set up all the buttons needed in the main activity
        setUpBottomButton();
        setUpTopButton();
        setUpPauseButton();
        setUpResetButton();
        setUpSettingsButton();
        setUpDummyDataButton();
        setUpArchiveButton();




        //Display database
        mDbHelper = new ChessClockDbHelper(this);//TODO Delete later on
        displayDatabase();
    }

    /**
     * Abstracted out the setting up of the bottom button in the onCreate method
     */
    private void setUpBottomButton() {
        bottomButton = (Button) findViewById(R.id.BottomButton);
        bottomClock = new myChessClock(300000,bottomButton);
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomButtonPressed();

            }
        });
        bottomButton.getBackground().setColorFilter(getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Abstracted out the setting up of the top button in the onCreate method
     */
    private void setUpTopButton(){
        topButton = (Button) findViewById(R.id.TopButton);
        topClock = new myChessClock(300000,topButton);
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topButtonPressed();
            }
        });
        topButton.getBackground().setColorFilter(getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Abstracted out the setting up of the pause button in the onCreate method
     */
    private void setUpPauseButton(){
        pauseButton = (Button) findViewById(R.id.PauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseClocks();
            }
        });
        pauseButton.getBackground().setColorFilter(getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Abstracted out the setting up of the reset button in the onCreate method
     */
    private void setUpResetButton(){
        resetButton = (Button) findViewById(R.id.ResetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomClock.reset();
                topClock.reset();

            }
        });
        resetButton.getBackground().setColorFilter(getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Abstracted out the setting up of the settings button in the onCreate method
     */
    private void setUpSettingsButton(){
        settingsButton = (Button) findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topClock.stopTimer();
                bottomClock.stopTimer();

                myPrefs = getSharedPreferences(PREFS_NAME,0);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putLong("BottomTimeLeft",bottomClock.getTimeLeftInMilliseconds());
                editor.putLong("BottomStartTime",bottomClock.getStartTimeInMillis());
                editor.putLong("TopTimeLeft",topClock.getTimeLeftInMilliseconds());
                editor.putLong("TopStartTime",topClock.getStartTimeInMillis());
                editor.commit();

                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        settingsButton.getBackground().setColorFilter(getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Abstracted out the setting up of the dummy data button in the onCreate method
     */
    private void setUpDummyDataButton(){
        insertDummyDataButton = (Button) findViewById(R.id.InsertDummyDataButton); //TODO Delete all examples of this button
        insertDummyDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDummyData();
                displayDatabase();
            }
        });
    }

    /**
     * Abstracted out the setting up of the archive button in the onCreate method
     */
    private void setUpArchiveButton(){
        archiveButton = (Button) findViewById(R.id.archiveButton);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    protected void onPause() { //To pause clocks when the app closes
        pauseClocks();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            //TODO separate this into smaller methods
            long oldBottomMillis = 0;
            long oldTopMillis = 0;

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME,0);
            if(prefs.contains("BottomTimeLeft")){
                oldBottomMillis = prefs.getLong("BottomTimeLeft",0);
            }
            if(prefs.contains("TopTimeLeft")){
                oldTopMillis = prefs.getLong("TopTimeLeft",0);
            }

            int newHours = data.getIntExtra("Hours",0);
            int newMinutes = data.getIntExtra("Minutes",0);
            int newSeconds = data.getIntExtra("Seconds",0);
            long newMillis = ((((newHours * 60) + newMinutes) * 60) + newSeconds)*1000;


            SettingsUpdate(oldTopMillis,oldBottomMillis,newMillis);
        }
    }

    public void SettingsUpdate(long oldTopMillis, long oldBottomMillis, long newMillis){


        if(newMillis==0){
            topClock.setTime(oldTopMillis);
            bottomClock.setTime(oldBottomMillis);

        } else {
            topClock.setStartTime(newMillis);
            bottomClock.setStartTime(newMillis);
            topClock.reset();
            bottomClock.reset();
        }
        topClock.updateTimer();
        bottomClock.updateTimer();
    }

    public void pauseClocks(){
        topClock.stopTimer();
        bottomClock.stopTimer();
    }

    public void topButtonPressed(){
        if (!topClock.isFirst() && !bottomClock.isFirst()) {
            topClock.setFirst(true);
        }
        topClock.stopTimer();
        bottomClock.startTimer();

    }

    public void bottomButtonPressed(){
        if (!topClock.isFirst() && !bottomClock.isFirst()) {
            bottomClock.setFirst(true);
        }
        bottomClock.stopTimer();
        topClock.startTimer();


    }

    /**
     * Displays the number of rows in the archive database as a log error message
     */
    private void displayDatabase(){


        String[] projection = {
                ChessClockEntry._ID,
                ChessClockEntry.GAME_ID,
                ChessClockEntry.DATE,
                ChessClockEntry.OPPONENT,
                ChessClockEntry.PIECE_COLOR,
                ChessClockEntry.TIME};


        Cursor cursor = getContentResolver().query(
                ChessClockEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        try{
            Log.v("Database", "Number of rows in archive databse: " + cursor.getCount());
        } finally {
            cursor.close();
        }//TODO Causing some error of some sort- some null object reference

    }

    /**
     * Insert dummy data into archive database //TODO Delete when finished
     */
    private void insertDummyData(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ChessClockEntry.GAME_ID, "1");
        values.put(ChessClockEntry.DATE, "02/23/2018");
        values.put(ChessClockEntry.PIECE_COLOR, ChessClockEntry.PIECE_COLOR_WHITE);
        values.put(ChessClockEntry.TIME, "24.2");
        values.put(ChessClockEntry.OPPONENT, "Joe");


        Uri newUri = getContentResolver().insert(ChessClockEntry.CONTENT_URI,values);

        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_chess_time_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_chess_time_successful),
                    Toast.LENGTH_SHORT).show();
        }    }
}
