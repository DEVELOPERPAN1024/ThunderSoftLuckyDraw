package cn.thundersoft.codingnight.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class AwardAndEmployeeInfoProvider extends ContentProvider {
    SQLiteOpenHelper databaseHelper;
    public static final String URI = "content://tscodingnight";
    public static final String DEBUG_TAG = "Provider";
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
    private static final int SEARACH_INFO = 6;
    private static final int UNAWARD = 7;
    private static final int WINNER = 8;
    private static final int PERSON_AWARD = 10;
    private static final int CLEAN_WININFO = 11;
    private static final int MONEY_LIST = 12;
    private static final int RED_PACKET = 13;
    private static final int NO_MONEY_LIST = 14;
    private static final int NO_MONEY_AND_NO_AWARD_LIST = 15;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        sUriMatcher.addURI(AUTH, "award", AWARD_ALL);
        sUriMatcher.addURI(AUTH, "award/#", AWARD_ID);
        sUriMatcher.addURI(AUTH, "info", INFO_ALL);
        sUriMatcher.addURI(AUTH, "info/#", INFO_ID);
        sUriMatcher.addURI(AUTH, "wininfo", WIN_ALL);
        sUriMatcher.addURI(AUTH, "wininfo/#", WIN_ID);
        sUriMatcher.addURI(AUTH, "wininfo/person/#", PERSON_AWARD);
        sUriMatcher.addURI(AUTH, "search/*", SEARACH_INFO);
        sUriMatcher.addURI(AUTH, "unaward", UNAWARD);
        sUriMatcher.addURI(AUTH, "winner", WINNER);
        sUriMatcher.addURI(AUTH, "wininfo/clean", CLEAN_WININFO);
        sUriMatcher.addURI(AUTH, "wininfo/money_list", MONEY_LIST);
        sUriMatcher.addURI(AUTH, "award/red_packet/#", RED_PACKET);
        sUriMatcher.addURI(AUTH, "wininfo/no_money_list", NO_MONEY_LIST);
        sUriMatcher.addURI(AUTH, "wininfo/no_money_no_award_list", NO_MONEY_AND_NO_AWARD_LIST);
    }

    public AwardAndEmployeeInfoProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case AWARD_ALL:
                return db.delete(TABLE_AWARD, selection, selectionArgs);
            case AWARD_ID:
                Log.d(DEBUG_TAG, "delete: " + uri.getPathSegments().get(0));
                break;
            case INFO_ALL:
                return db.delete(TABLE_INFO, selection, selectionArgs);
            case INFO_ID:
                break;
            case WIN_ALL:
                break;
            case WIN_ID:
                break;
            case CLEAN_WININFO:
                ContentValues cv = new ContentValues();
                cv.put("drawed_times", 0);
                db.update(TABLE_AWARD, cv, null, null);
                int affectedRows = db.delete(TABLE_WIN_INFO, null, null);
                cv.clear();
                cv.put("seq", 0);
                db.update("sqlite_sequence", cv, "name = ?", new String[]{TABLE_WIN_INFO});
                return affectedRows;
        }
        return 0;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.d(DEBUG_TAG, "insert: " + uri);
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
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        Log.d("TS", "query: " + uri);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case AWARD_ALL:
                return db.query(TABLE_AWARD, null, null, null, null, null, null);
            case AWARD_ID:
                String aid = uri.getPathSegments().get(1);
                return db.query(TABLE_AWARD, null, "_id = ?", new String[]{aid}, null, null, null);
            case INFO_ALL:
                return db.rawQuery("select info._id as _id, info.info as info, info.award_id as award_id, award.name as name\n" +
                        "from info left join award on (info.award_id=award._id)", null);
            case INFO_ID:
                String iid = uri.getPathSegments().get(0);
                return db.rawQuery("select info._id as _id, info.info as info, info.award_id as award_id, award.name as name\n" +
                        "from info left join award on (info.award_id=award._id)\n" +
                        "where _id=?", new String[]{iid});
            case WIN_ALL:
                return db.query(TABLE_WIN_INFO, null, null, null, null, null, null);
            case WIN_ID:
                String wid = uri.getPathSegments().get(1);
                String q = "select wininfo._id,info.info,award.name,wininfo.money\n" +
                        "from info join wininfo on (info._id = wininfo.info_id) join award on (award._id=wininfo.award_id)\n" +
                        "where wininfo.award_id = ?";
                return db.rawQuery(q, new String[]{wid});
            case SEARACH_INFO:
                String keyWord = uri.getPathSegments().get(1);
                keyWord = DbUtil.sqliteEscape(keyWord);
                return db.rawQuery("select info._id as _id, info.info as info, info.award_id as award_id, award.name as name\n" +
                        "from info left join award on (info.award_id=award._id)\n" +
                        "where info like '%" + keyWord + "%'", null);
            case UNAWARD:
                return db.rawQuery("select *\n" +
                        "from info\n" +
                        "where info._id not in\n" +
                        "(select info_id\n" +
                        "from wininfo where money < 0 or money is null);", null);
            case WINNER:
                return db.rawQuery("select *\n" +
                        "from info\n" +
                        "where info._id in\n" +
                        "(select info_id\n" +
                        "from wininfo where money < 0 or money is null);", null);
            case PERSON_AWARD:
                String personId = uri.getLastPathSegment();
                return db.rawQuery("select *\n" +
                        "from award\n" +
                        "where award._id in\n" +
                        "(select wininfo.award_id\n" +
                        "from wininfo\n" +
                        "where wininfo.info_id = ?)", new String[]{personId});
            case MONEY_LIST:
                return db.rawQuery("select info._id, info, sum(money) " +
                        "from info inner join wininfo on (info._id = wininfo.info_id) " +
                        "where money > 0 " +
                        "group by info._id", null);
            case NO_MONEY_LIST:
                uri.getQueryParameter("alsoNoAward");
                return db.rawQuery("select info._id, info\n" +
                        "from info left join wininfo on (info._id = wininfo.info_id)\n" +
                        "where money = -1 or money is null group by info._id", null);
            case NO_MONEY_AND_NO_AWARD_LIST:
                return db.rawQuery("select info._id, info\n" +
                        "from info left join wininfo on (info._id = wininfo.info_id)\n" +
                        "wininfo.award_id is null\n" +
                        "group by info._id", null);
        }
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String table = null;
        switch (sUriMatcher.match(uri)) {
            case AWARD_ALL:
                table = TABLE_AWARD;
                break;
            case AWARD_ID:
            case INFO_ID:
            case WIN_ALL:
            case WIN_ID:
                break;
            case INFO_ALL:
                table = TABLE_INFO;
                break;
        }
        if (table != null)
            db.update(table, values, selection, selectionArgs);
        return 0;
    }
}
