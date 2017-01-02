package cn.thundersoft.codingnight.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wy on 16-11-25.
 */

public class InfoDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static InfoDatabaseHelper sInstance;
    private static final String CREATE_AWARD_INFO =
            "CREATE TABLE award (\n" +
                    "    _id           INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                    "    name          CHAR (128)   NOT NULL,\n" +
                    "    count         INTEGER (32) DEFAULT (0),\n" +
                    "    detail        CHAR (256),\n" +
                    "    picuri        CHAR (256),\n" +
                    "    order_index   INTEGER,\n" +
                    "    total_times   INTEGER,\n" +
                    "    drawed_times  INTEGER,\n" +
                    "    can_repeat    INTEGER\n" +
                    ");";
    private static final String CREATE_INFO =
            "CREATE TABLE info (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "info CHAR (256) NOT NULL, " +
                    "award_id INTEGER DEFAULT(0));";
    private static final String CREATE_WIN_INFO =
            "CREATE TABLE wininfo (\n" +
                    "    _id      INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    info_id  INTEGER,\n" +
                    "    award_id INTEGER\n" +
                    ");";
    private static final String CREATE_WININFO_TRIGGER="CREATE TRIGGER wininfo_trigger\n" +
            "AFTER INSERT\n" +
            "ON wininfo\n" +
            "BEGIN\n" +
            "    UPDATE info\n" +
            "       SET award_id = new.award_id\n" +
            "    WHERE new.info_id = info._id;\n" +
            "END;";
    private static final String CREATE_TRIGGER_AFTER_AWARD_DELETE=
            "CREATE TRIGGER update_info_wininfo_after_award_delete\n" +
            "AFTER DELETE\n" +
            "ON award\n" +
            "BEGIN\n" +
            "    UPDATE info\n" +
            "       SET award_id = 0\n" +
            "     WHERE info.award_id = old._id;\n" +
            "    DELETE FROM wininfo\n" +
            "          WHERE wininfo.award_id = old._id;\n" +
            "END;\n";
    private static final String CREATE_TRIGGER_AFTER_INFO_DELETE=
            "CREATE TRIGGER delete_wininfo_after_info_delete\n" +
                    "         AFTER DELETE\n" +
                    "            ON info\n" +
                    "BEGIN\n" +
                    "    DELETE FROM wininfo\n" +
                    "          WHERE info_id = old._id;\n" +
                    "END;\n";

    public static InfoDatabaseHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new InfoDatabaseHelper(context);
        }
        return sInstance;
    }

    private InfoDatabaseHelper(Context context) {
        super(context, "tscn", null, VERSION);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TS", "onCreate: " + "create database");
        db.execSQL(CREATE_AWARD_INFO);
        db.execSQL(CREATE_INFO);
        db.execSQL(CREATE_WIN_INFO);
        db.execSQL(CREATE_WININFO_TRIGGER);
        db.execSQL(CREATE_TRIGGER_AFTER_AWARD_DELETE);
        db.execSQL(CREATE_TRIGGER_AFTER_INFO_DELETE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
