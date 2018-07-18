package com.ashwinsreevatsacom.chessclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class Settings extends AppCompatActivity {
    private Button cancelButton;
    private Button saveButton;
    private NumberPicker npHours, npMinutes, npSeconds;

    private int hours,minutes,seconds = 0;

    private static final String PREFS_NAME = "myPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = getIntent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = getIntent();
                returnIntent.putExtra("Hours",hours);
                returnIntent.putExtra("Minutes", minutes);
                returnIntent.putExtra("Seconds",seconds);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,0);
        int totalMillis = (int) prefs.getLong("TopStartTime",0);
        hours = totalMillis/1000/60/60;
        minutes = totalMillis/1000/60 %60;
        seconds = totalMillis/1000 %60;

        npHours = (NumberPicker) findViewById(R.id.npHours);
        npHours.setMaxValue(10);
        npHours.setValue(hours);
        npHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                hours = i1;
            }
        });
        npMinutes = (NumberPicker) findViewById(R.id.npMinutes);
        npMinutes.setMaxValue(59);
        npMinutes.setValue(minutes);
        npMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                minutes = i1;
            }
        });
        npSeconds = (NumberPicker) findViewById(R.id.npSeconds);
        npSeconds.setMaxValue(59);
        npSeconds.setValue(seconds);
        npSeconds.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                seconds = i1;
            }
        });



    }
}
