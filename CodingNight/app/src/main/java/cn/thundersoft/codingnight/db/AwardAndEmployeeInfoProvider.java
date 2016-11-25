package cn.thundersoft.codingnight.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class AwardAndEmployeeInfoProvider extends ContentProvider {
    private static final int VERSION = 1;
    SQLiteOpenHelper databaseHelper;
    public AwardAndEmployeeInfoProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Log.d("TS", "insert: " + uri);
        if (uri.getPath().equals("award"));
            db.insert("award",null,values);
        return null;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new InfoDatabaseHelper(getContext(),"tscn",null,VERSION);
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("TS", "query: " + uri);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor c = db.query("award",projection,selection,selectionArgs,sortOrder,null,null);
            db.endTransaction();
            return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
