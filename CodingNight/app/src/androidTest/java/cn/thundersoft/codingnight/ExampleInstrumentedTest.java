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
import cn.thundersoft.codingnight.util.MyRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertEquals("cn.thundersoft.codingnight", appContext.getPackageName());
    }

    public void testDB() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Uri uri = Uri.parse("content://" + AwardAndEmployeeInfoProvider.AUTH + "/award");
        ContentValues cv = new ContentValues();
        cv.put("name", "nameTest");
        ContentResolver cr = appContext.getContentResolver();
        appContext.getContentResolver().insert(uri, cv);
        Cursor c = appContext.getContentResolver().query(uri, null, null, null, null);
        assertTrue(c != null && c.getCount() > 0);
        cr.delete(uri, null, null);
        c = appContext.getContentResolver().query(uri, null, null, null, null);
        assertTrue(c != null && c.getCount() == 0);
        c.close();
        Log.d("TS", "useAppContext: " + Uri.withAppendedPath(uri, "123"));
    }

    @Test
    public void testRandom() {
        double sum = 0.0;
        for (int i = 0; i < 100000; i++) {
            sum += MyRandom.getRandom_0_1();
        }
        assertTrue((sum / 100000 - 0.5) < 0.001);
    }
}
