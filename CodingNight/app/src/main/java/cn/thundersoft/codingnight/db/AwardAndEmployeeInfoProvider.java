package cn.thundersoft.codingnight.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class AwardAndEmployeeInfoProvider extends ContentProvider {
    SQLiteOpenHelper databaseHelper;
    public static final String URI = "content://tscodingnight";
    public static final String DEBUG_TAG = "TS";
    public static final String AUTH = "tscodingnight";
    private static final String TABLE_AWARD = "award";
    private static final String TABLE_WIN_INFO = "wininfo";
    private static final String TABLE_INFO = "info";
    private static final int AWARD_ALL = 0;
    private static final int AWARD_ID = 1;
    private static final int INFO_ALL = 2;
    private static final int INFO_ID = 3;
    private static final int WIN_ALL = 4;
    private static final int WIN_ID = 5;
    private static final UriMatcher sUriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);


    private static final int SEARACH_INFO = 6;

    static {
        sUriMatcher.addURI(AUTH, "award", AWARD_ALL);
        sUriMatcher.addURI(AUTH, "award/#", AWARD_ID);
        sUriMatcher.addURI(AUTH, "info", INFO_ALL);
        sUriMatcher.addURI(AUTH, "info/#", INFO_ID);
        sUriMatcher.addURI(AUTH, "wininfo", WIN_ALL);
        sUriMatcher.addURI(AUTH, "wininfo/#", WIN_ID);
        sUriMatcher.addURI(AUTH, "search/#",SEARACH_INFO);
    }

    public AwardAndEmployeeInfoProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case AWARD_ALL:
                db.delete(TABLE_AWARD, selection, selectionArgs);
                break;
            case AWARD_ID:
                Log.d(DEBUG_TAG, "delete: " + uri.getPathSegments().get(0));
                break;
            case INFO_ALL:
                db.delete(TABLE_INFO, selection, selectionArgs);
                break;
            case INFO_ID:
                break;
            case WIN_ALL:
                break;
            case WIN_ID:
                break;
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d("TS", "insert: " + uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String table = null;
        switch (sUriMatcher.match(uri)) {
            case AWARD_ALL:
                table = TABLE_AWARD;
                break;
            case AWARD_ID:
                break;
            case INFO_ALL:
                table = TABLE_INFO;
                break;
            case INFO_ID:
                break;
            case WIN_ALL:
                table = TABLE_WIN_INFO;
                break;
            case WIN_ID:
                break;
        }
        if (table != null) {
            db.insert(table, null, values);
        }
        return uri;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = InfoDatabaseHelper.getsInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("TS", "query: " + uri);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case AWARD_ALL:
                return db.query(TABLE_AWARD, null, null, null, null, null, null);
            case AWARD_ID:
                String aid = uri.getPathSegments().get(0);
                return db.query(TABLE_AWARD, null, "id = ?", new String[]{aid}, null, null, null);
            case INFO_ALL:
                return db.query(TABLE_INFO, null, null, null, null, null, null);
            case INFO_ID:
                String iid = uri.getPathSegments().get(0);
                return db.query(TABLE_AWARD, null, "id = ?", new String[]{iid}, null, null, null);
            case WIN_ALL:
                return db.query(TABLE_WIN_INFO, null, null, null, null, null, null);
            case WIN_ID:
//                String query = "select info._id,award.name,award.detail,award.picuri,award.count\n" +
//                        "from info join wininfo on (info._id = wininfo.info_id) join award on (award._id=wininfo.award_id)\n" +
//                        "where info._id = ?";
//                String wid = uri.getPathSegments().get(0);
//                return db.rawQuery(query,new String[] {wid});
                String wid = uri.getPathSegments().get(1);
                String q = "select wininfo._id,info.info\n" +
                        "from info join wininfo on (info._id = wininfo.info_id) join award on (award._id=wininfo.award_id)\n" +
                        "where wininfo.award_id = ?";
                return db.rawQuery(q,new String[]{wid});
            case SEARACH_INFO:
                String sid = uri.getPathSegments().get(1);
                String searchQuery = "select * from info where info like '%" + sid +"%'";
                Log.d(DEBUG_TAG, "query: " + searchQuery);
                return db.rawQuery(searchQuery,null);
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case AWARD_ALL:
            case AWARD_ID:
            case INFO_ID:
            case WIN_ALL:
            case WIN_ID:
                break;
            case INFO_ALL:
                return db.update(TABLE_INFO, values, selection, selectionArgs);
        }
        return 0;
    }
}
