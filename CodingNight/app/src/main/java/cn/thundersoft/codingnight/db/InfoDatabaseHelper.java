package cn.thundersoft.codingnight.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wy on 16-11-25.
 */

public class InfoDatabaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 2;
    private static InfoDatabaseHelper sInstance;
    private static final String CREATE_AWARDINFO =
            "CREATE TABLE award (\n" +
                    "    _id    INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                    "    name   CHAR (128)   NOT NULL,\n" +
                    "    count  INTEGER (32) DEFAULT (0),\n" +
                    "    detail CHAR (256),\n" +
                    "    picuri CHAR (256) \n" +
                    ");";
    public static InfoDatabaseHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new InfoDatabaseHelper(context);
        }
        return sInstance;
    }
    private InfoDatabaseHelper(Context context) {
        super(context,"tscn",null,VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TS", "onCreate: " + "create database");
        db.execSQL(CREATE_AWARDINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
