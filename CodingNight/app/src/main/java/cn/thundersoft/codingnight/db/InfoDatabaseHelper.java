package cn.thundersoft.codingnight.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wy on 16-11-25.
 */

public class InfoDatabaseHelper extends SQLiteOpenHelper{
    private static final String CREATE_AWARDINFO =
            "CREATE TABLE award (\n" +
                    "    _id    INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                    "    name   CHAR (128)   NOT NULL,\n" +
                    "    count  INTEGER (32) DEFAULT (0),\n" +
                    "    detail CHAR (256),\n" +
                    "    picuri CHAR (256) \n" +
                    ");";
    public InfoDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

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
