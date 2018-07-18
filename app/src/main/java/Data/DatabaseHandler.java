package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import Model.TimeRecord;
import Utils.Util;

/**
 * Created by ashwin on 2/24/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    static DatabaseHandler singleton = null;

    // do not call method this directly.
    public DatabaseHandler(Context context) {
        super(context,Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    public static DatabaseHandler get(Context context) {
        if (singleton == null) {
            // not initialized. create it
            singleton = new DatabaseHandler(context);
        }
        return singleton;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_NAME +
                "(" + Util.KEY_ID + " INTEGER PRIMARY KEY," +
                Util.KEY_TIME + " TEXT," +
                Util.KEY_IS_FIRST + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    public void addTimeRecords(TimeRecord timeRecord){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(Util.KEY_TIME, timeRecord.getTimeMillis());
        value.put(Util.KEY_IS_FIRST, timeRecord.isFirst());

        db.insert(Util.TABLE_NAME,null,value);
        db.close();
    }

    public TimeRecord getTimeRecord(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME,new String[]
                        {Util.KEY_ID, Util.KEY_TIME, Util.KEY_IS_FIRST}, Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        TimeRecord timeRecord = new TimeRecord(Integer.parseInt(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                Boolean.parseBoolean(cursor.getString(2)));

        return timeRecord;
    }

    public List<TimeRecord> getAllTimeRecords(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<TimeRecord> timeRecordList = new ArrayList<>();

        String selectALL = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor  = db.rawQuery(selectALL,null);

        if(cursor.moveToFirst() ){
            do {
                TimeRecord timeRecord = new TimeRecord();
                timeRecord.setId(Integer.parseInt(cursor.getString(0)));
                timeRecord.setTimeMillis(Long.parseLong(cursor.getString(1)));
                if(cursor.getString(2).equals("1")){
                    timeRecord.setFirst(true);
                } else{
                    timeRecord.setFirst(false);
                }

                timeRecordList.add(timeRecord);
            } while( cursor.moveToNext());
        }

        return timeRecordList;
    }

    public int updateTimeRecord(TimeRecord timeRecord){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_TIME, timeRecord.getTimeMillis());
        values.put(Util.KEY_IS_FIRST,timeRecord.isFirst());

        return db.update(Util.TABLE_NAME,values,Util.KEY_ID + "=?",
                new String[] {String.valueOf(timeRecord.getId())});
    }

    public void deleteTimeRecord(TimeRecord timeRecord){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?",
                new String[] {String.valueOf(timeRecord.getId())});

        db.close();
    }

    public int getTimeRecordsCount(){
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        //cursor.close();
        return cursor.getCount();
    }
}
