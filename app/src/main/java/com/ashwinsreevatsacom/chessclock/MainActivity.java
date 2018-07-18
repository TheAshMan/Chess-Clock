package com.ashwinsreevatsacom.chessclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dd.processbutton.ProcessButton;

import java.sql.Time;
import java.util.List;

import Data.DatabaseHandler;
import Model.TimeRecord;

public class MainActivity extends AppCompatActivity {

    private Button bottomButton;
    private Button topButton;
    private myChessClock bottomClock;
    private myChessClock topClock;

    private Button pauseButton;
    private Button settingsButton;
    private Button resetButton;
    private Button archiveButton;



    private final int REQUEST_CODE = 2;

    private static final String PREFS_NAME = "myPrefsFile";
    private SharedPreferences myPrefs;


    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        DatabaseHandler db = new DatabaseHandler(this);
        db = DatabaseHandler.get(this);








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

        pauseButton = (Button) findViewById(R.id.PauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseClocks();
            }
        });
        pauseButton.getBackground().setColorFilter(getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);

        resetButton = (Button) findViewById(R.id.ResetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomClock.reset();
                topClock.reset();
                deleteTimeRecords();

            }
        });
        resetButton.getBackground().setColorFilter(getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);

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


        archiveButton = (Button) findViewById(R.id.archiveButton);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Archive.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        archiveButton.getBackground().setColorFilter(getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);

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

    public void deleteTimeRecords(){
        List<TimeRecord> timeRecordList = db.getAllTimeRecords();
        for(TimeRecord t: timeRecordList){
            db.deleteTimeRecord(t);
        }
    }

}
