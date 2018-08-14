package com.ashwinsreevatsacom.chessclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import com.ashwinsreevatsacom.chessclock.Model.TimeRecord;

public class Archive extends AppCompatActivity {
    private TextView ArchiveTextWhite;
    private TextView ArchiveTextBlack;
    private List<TimeRecord> timeRecordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);







        ArchiveTextWhite = (TextView) findViewById(R.id.archiveTextWhite);
        ArchiveTextBlack = (TextView) findViewById(R.id.archiveTextBlack);
        ArchiveTextWhite.setText(whiteTimeFormat());
        ArchiveTextBlack.setText(blackTimeFormat());

    }

    public String stringArchive(boolean iswhite){
        String archiveTime = "";
        int i = 1;
        for(TimeRecord t: timeRecordList){
            if(t.isFirst() == iswhite){
                archiveTime+= i+ ". " +formatTime(t.getTimeMillis()/1000) + "\n";
                i++;
            }

        }
        return  archiveTime;
    }

    public String whiteTimeFormat(){
        String whiteTimeFormat = "White \n";
        whiteTimeFormat += stringArchive(false);
        return whiteTimeFormat;
    }

    public String blackTimeFormat(){
        String blackTimeFormat = "Black \n";
        blackTimeFormat += stringArchive(true);
        return blackTimeFormat;
    }

    public String formatTime(long timeSecs){
        String hours = String.valueOf(timeSecs / 60 / 60);
        if(timeSecs/60/60 <10){
            hours = 0 +"" +hours;
        }

        String minutes = String.valueOf(timeSecs / 60 % 60);
        if(timeSecs/60%60 <10){
            minutes = 0 +"" +minutes;
        }

        String seconds = String.valueOf(timeSecs % 60);
        if(timeSecs%60 <10){
            seconds = 0 +"" +seconds;
        }

        return hours + ":" + minutes + ":" + seconds;

    }
}
