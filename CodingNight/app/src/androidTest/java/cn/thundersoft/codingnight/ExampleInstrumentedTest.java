package cn.thundersoft.codingnight;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.thundersoft.codingnight.db.AwardAndEmployeeInfoProvider;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "TEST TAG";
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Uri uri = Uri.parse("content://" + AwardAndEmployeeInfoProvider.AUTH + "/award");
        ContentValues cv = new ContentValues();
        cv.put("name","fuck");
        ContentResolver cr = appContext.getContentResolver();
        appContext.getContentResolver().insert(uri,cv);
        Cursor c = appContext.getContentResolver().query(uri,null,null,null,null);
        cr.delete(uri,null,null);
        Log.d("TS", "useAppContext: " + Uri.withAppendedPath(uri,"123"));
        assertEquals("cn.thundersoft.codingnight", appContext.getPackageName());
    }
}
