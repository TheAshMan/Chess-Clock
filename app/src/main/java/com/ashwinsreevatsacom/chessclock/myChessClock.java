package com.ashwinsreevatsacom.chessclock;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.widget.Button;

import com.dd.processbutton.FlatButton;
import com.dd.processbutton.ProcessButton;

import java.util.List;
import java.util.TimerTask;

import Model.TimeRecord;

/**
 * Created by ashwin on 2/19/2018.
 */

public class myChessClock {
    private long startTimeInMillis = 300000;
    private long timeLeftInMilliseconds;
    private boolean isRunning = false;
    private CountDownTimer countDownTimer;

    private Button countDownButton; //TODO make sure that the flatbutton references work

    private static boolean anyTimerStops = false; //to tell if any of the timers have hit 0

    private boolean isFirst = false; //was this button selected first >> white

    private static List<TimeRecord> timeRecordList;

    public myChessClock(){
        timeLeftInMilliseconds = startTimeInMillis;
    }

    public myChessClock(long timeMill, Button newCountDownbutton){
        startTimeInMillis = timeMill;
        timeLeftInMilliseconds = startTimeInMillis;
        countDownButton = newCountDownbutton;
        updateTimer();


    }



    public void startstop(){
        if(isRunning){
            stopTimer();
        }
        else{
            startTimer();
        }
    }

    public void startTimer(){
        if(!isRunning&&!anyTimerStops) {
            isRunning = true;
            setButtonColorBlue();
            countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1) {
                @Override
                public void onTick(long l) {
                    timeLeftInMilliseconds = l;
                    //TODO could be altered so that only every 1000th millisecond the timer updates
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    isRunning = false;
                    setButtonColorRed();
                    anyTimerStops = true;

                }
            }.start();
        }
    }

    public void stopTimer(){
        if(isRunning) {
            isRunning = false;
            countDownTimer.cancel();
            setButtonColorGray();
//        updateTimer();
        }
    }


    public void updateTimer(){
        int hours = (int) (timeLeftInMilliseconds/1000)/60/60;
        int minutes = (int) (timeLeftInMilliseconds / 1000) / 60 %60;
        int seconds = (int) (timeLeftInMilliseconds/1000) %60;


        countDownButton.setText(convertToFormattedTime(hours,minutes,seconds));
    }

    public void reset(){
        stopTimer();
        setButtonColorGray();
        timeLeftInMilliseconds = startTimeInMillis;
        updateTimer();
        anyTimerStops = false;
        isFirst = false;
    }

    public String convertToFormattedTime(int hours, int minutes, int seconds){
        String finalDisplay = "";
        String displayHours = String.valueOf(hours);
        String displayMinutes = String.valueOf(minutes);
        String displaySeconds = String.valueOf(seconds);

        if(seconds <10){
            displaySeconds = 0+""+displaySeconds;
        }

        if(minutes<10){
            displayMinutes = 0+""+displayMinutes;
        }

        if(hours < 10){
            displayHours = 0+""+displayHours;
        }

        finalDisplay = displayHours + ":" + displayMinutes + ":" + displaySeconds;

        return finalDisplay;
    }

    public long getStartTimeInMillis(){
        return startTimeInMillis;
    }

    public long getTimeLeftInMilliseconds(){
        return timeLeftInMilliseconds;
    }

    public void setTime(long millis){
        timeLeftInMilliseconds = millis;
    }

    public void setStartTime(long millis){
        startTimeInMillis = millis;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public static boolean isAnyTimerStops() {
        return anyTimerStops;
    }

    public static void setAnyTimerStops(boolean anyTimerStops) {
        myChessClock.anyTimerStops = anyTimerStops;
    }

    public void setButtonColorGray(){
        countDownButton.getBackground().setColorFilter(countDownButton.getContext().getResources().
                getColor(R.color.fbutton_color_concrete), PorterDuff.Mode.MULTIPLY);
        countDownButton.setTextColor(Color.BLACK);
    }

    public void setButtonColorRed(){
        countDownButton.getBackground().setColorFilter(countDownButton.getContext().getResources().
                getColor(R.color.fbutton_color_alizarin), PorterDuff.Mode.MULTIPLY);
    }

    public void setButtonColorBlue(){
        countDownButton.getBackground().setColorFilter(countDownButton.getContext().getResources().
                getColor(R.color.DefaultFlatButtonColorBlue), PorterDuff.Mode.MULTIPLY);
        countDownButton.setTextColor(Color.WHITE);
    }

    public static List<TimeRecord> getTimeRecordList() {
        return timeRecordList;
    }

}
